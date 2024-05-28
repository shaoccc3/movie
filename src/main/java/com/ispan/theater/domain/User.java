package com.ispan.theater.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"user\"")
public class User {
    @Override
	public String toString() {
		return "[id=" + id + ", userFirstname=" + userFirstname + ", userLastname=" + userLastname + ", email="
				+ email + ", phone=" + phone + ", consumption=" + consumption + ", userlevel=" + userlevel + ", birth="
				+ birth + ", gender=" + gender + ", isverified=" + isverified + "]";
	}


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "user_firstname" , columnDefinition = "nvarchar(20)")
    private String userFirstname;
    
    @Column(name = "user_lastname",  columnDefinition = "nvarchar(20)")
    private String userLastname;
    
    @JsonIgnore
    @Lob
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "phone", nullable = false,length = 20)
    private String phone;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "registrationDate", nullable = false)
    private Date registrationDate;
    
    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modifiedDate", nullable = false)
    private Date modifiedDate;

    //尚未定義消費
    @Column(name = "consumption")
    private Double consumption;
    
    //尚未定義會員等級與功能
    @Column(name = "userlevel", nullable = false)
    private Integer userlevel;

    @Column(name = "birth")
    private LocalDate birth;
    
    
    //gender(性別)
    @Column(name = "gender")
    private String gender;
    
    
    //Is_verified(是否已驗證)
    @Column(name = "is_verified", nullable = false	)
    private Boolean isverified;
    
    
    @JsonIgnore
    @Lob
    @Column(name = "user_photo")
    private byte[] userPhoto;
    
}