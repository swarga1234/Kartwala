package com.swarga.Kartwala.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int roleId;


    @Enumerated(EnumType.STRING) //Enum in database is automatically represented as Integer.
    // For it to be represented as String set EnumType.String
    @Column(length = 20, name = "role_name")
    @ToString.Exclude
    private AppRole roleName;
}
