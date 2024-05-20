package com.ispan.theater.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table
public class Food {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="food_id", nullable = false)
	private Integer id;
	
	@Column(name="food_name", nullable = false)
	private String name;
	
	@Column(name = "food_ename", length = 50, nullable = false)
    private String name_eng;
	
	@Column(name="food_price", nullable = false)
	private Double price;
	
	@Column(name="food_count", nullable = false)
	private Integer count;
	
	@Column(name = "food_description", nullable = false, length = 500)
    private String description;
	
	@Column(name="foodCategory_code", nullable = false)
	private String foodCategory_code;
	
	@Column(name="make", nullable = false)
	private Date make;
	
	@Column(name="expired", nullable = false)
	private Date expired;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date", nullable = false)
	private Date createDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="modify_date", nullable = false)
	private Date modifyDate;
	
	@Lob
	@JsonIgnore
	private byte[] image;
	
	@Override
    public String toString() {
        return "Food{" +
                "food_name='" + name + '\'' +
                ", food_ename='" + name_eng + '\'' +
                ", food_price='" + price + '\'' +
                ", food_count='" + count + '\'' +
                ", food_description='" + description + '\'' +
                ", foodCategory_code='" + foodCategory_code + '\'' +
                ", make=" + make +
                ", expired=" + expired +
                ", create_date=" + createDate +
                ", modify_date=" + modifyDate +
                '}';
    }

}
