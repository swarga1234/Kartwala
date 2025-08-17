package com.swarga.Kartwala.security;

import com.swarga.Kartwala.model.AppRole;
import com.swarga.Kartwala.model.Role;
import com.swarga.Kartwala.model.User;
import com.swarga.Kartwala.repository.RoleRepository;
import com.swarga.Kartwala.repository.UserRepository;
import com.swarga.Kartwala.security.jwt.AuthEntryPointJwt;
import com.swarga.Kartwala.security.jwt.AuthTokenFilter;
import com.swarga.Kartwala.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter getAuthTokenFilter() {
        return new AuthTokenFilter();
    }

    /*
        This method is used to configure the authentication provider.
        The DaoAuthenticationProvider is used to retrieve user details from a userDetailsService
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /*
        Attempts to authenticate the passed Authentication object,
        returning a fully populated Authentication object (including granted authorities) if successful.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::sameOrigin
                )).exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement( httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests( requests ->
                    requests.requestMatchers("/api/auth/**").permitAll()
                            .requestMatchers("/v3/api-docs/**").permitAll()
                            .requestMatchers("/h2-console/**").permitAll()
                            .requestMatchers("/favicon.ico").permitAll()
                            .requestMatchers("/swagger-ui/**").permitAll()
//                            .requestMatchers("/api/public/**").permitAll()
//                            .requestMatchers("/api/admin/**").permitAll()
                            //.requestMatchers("api/auth/**").permitAll()
                            .requestMatchers("/api/test/**").permitAll()
                            .requestMatchers("/images/**").permitAll()
                            .anyRequest().authenticated());
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(getAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // Add all urls which you want to completely bypass the spring security
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web -> {
            web.ignoring().requestMatchers("/v2/api-docs",
                    "/configuration/ui", "/swagger-resources/**",
                    "/configuration/security",
                    "/swagger-ui.html",
                    "/webjars/**"
                    );
        });
    }

    @Bean
    public CommandLineRunner initData(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        return args -> {
            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setRoleName(AppRole.ROLE_USER);
                        return roleRepository.save(role);
                    });
            Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setRoleName(AppRole.ROLE_SELLER);
                        return roleRepository.save(role);
                    });
            Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setRoleName(AppRole.ROLE_ADMIN);
                        return roleRepository.save(role);
                    });

            Set<Role> userRoles = Set.of(userRole);
            Set<Role> sellerRoles = Set.of(sellerRole);
            Set<Role> adminRoles = Set.of(userRole, sellerRole, adminRole);

            if(!userRepository.existsByUsername("user1")){
                User user1 = new User();
                user1.setUsername("user1");
                user1.setEmail("user1@example.com");
                user1.setPassword(passwordEncoder.encode("password1"));
                userRepository.save(user1);
            }
            if(!userRepository.existsByUsername("seller1")){
                User seller1 = new User();
                seller1.setUsername("seller1");
                seller1.setEmail("seller1@example.com");
                seller1.setPassword(passwordEncoder.encode("password2"));
                userRepository.save(seller1);
            }
            if(!userRepository.existsByUsername("admin")){
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("adminPass"));
                userRepository.save(admin);
            }

            //Update roles for existing users
            userRepository.findByUsername("user1").ifPresent(
                    user -> {
                        user.setRoles(userRoles);
                        userRepository.save(user);
                    }
            );
            userRepository.findByUsername("seller1").ifPresent(
                    user -> {
                        user.setRoles(sellerRoles);
                        userRepository.save(user);
                    }
            );
            userRepository.findByUsername("admin").ifPresent(
                    user -> {
                        user.setRoles(adminRoles);
                        userRepository.save(user);
                    }
            );

        };

    }
}
