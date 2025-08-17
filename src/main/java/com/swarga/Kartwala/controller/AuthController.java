package com.swarga.Kartwala.controller;

import com.swarga.Kartwala.exception.UserNotFoundException;
import com.swarga.Kartwala.model.AppRole;
import com.swarga.Kartwala.model.Role;
import com.swarga.Kartwala.model.User;
import com.swarga.Kartwala.repository.RoleRepository;
import com.swarga.Kartwala.repository.UserRepository;
import com.swarga.Kartwala.security.jwt.JwtUtils;
import com.swarga.Kartwala.security.request.LoginRequest;
import com.swarga.Kartwala.security.request.SignupRequest;
import com.swarga.Kartwala.security.response.MessageResponse;
import com.swarga.Kartwala.security.response.UserInfoResponse;
import com.swarga.Kartwala.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<UserInfoResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails.getUserId(),userDetails.getUsername()
                ,roles);
        //Now we have to pass the cookie to the web browser/postman client
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,jwtCookie.toString()).body(userInfoResponse);

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("Error: The username already exists!!")
            );
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest().body(
                    new MessageResponse("Error: The email already exists!!")
            );
        }
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        Set<String> strRoles = signupRequest.getRoles();
        if (strRoles == null || strRoles.isEmpty()) {
            Role role = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not Valid!!"));
            roles.add(role);
        } else {
            strRoles.forEach(role -> {
                switch (role){
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role admin is not Valid!!"));
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role seller is not Valid!!"));
                        roles.add(sellerRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role "+role+" is not Valid!!"));
                        roles.add(userRole);
                        break;
                }
            });
        }
        logger.info(roles.toString());
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("The User is registered!!"));
    }

    @GetMapping("/username")
    public String getUsername(Authentication authentication){
        if(authentication!=null){
            return authentication.getName();
        }
        else {
            return "";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserInfoResponse> getUserDetails(Authentication authentication){
        if(authentication!=null){
            logger.info(""+authentication.getPrincipal());
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).toList();
            UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails.getUserId(),userDetails.getUsername(),
                    roles);
            return ResponseEntity.ok(userInfoResponse);
        }else{
            throw new UserNotFoundException("There is no logged in user!!");
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<MessageResponse> signOut(){
        ResponseCookie cookie = jwtUtils.getCleanCookieJwt();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You have been Signed Out!"));
    }
}
