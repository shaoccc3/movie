package com.ispan.theater.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "username", nullable = false, length = 20)
    private String username;

    @Lob
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "RegistrationDate", nullable = false)
    private Date registrationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifiedDate", nullable = false)
    private Date modifiedDate;

    @Column(name = "Consumption")
    private Double consumption;

    @Column(name = "userlevel", nullable = false)
    private Integer userlevel;

    @Column(name = "birth", nullable = false)
    private LocalDate birth;

}