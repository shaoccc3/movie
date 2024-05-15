package com.ispan.theater.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "\"Order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date", nullable = false)
    private Date modifyDate;

    @Column(name = "order_amount", nullable = false)
    private Double orderAmount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
    
    @OneToMany(cascade = CascadeType.ALL,mappedBy ="order",fetch=FetchType.LAZY)
    private List<OrderDetail> orderDetails;
    
    @PrePersist
    public void onCreate() {
    	if(createDate==null) {
    		createDate=new Date();
    	}
    	if(modifyDate==null) {
    		modifyDate=new Date();
    	}
    }
 
	@Override
	public String toString() {
		return "Order [id=" + id + ", user_id=" + user.getId() + ", createDate=" + createDate + ", modifyDate=" + modifyDate
				+ ", orderAmount=" + orderAmount + ", movie=" + movie.getName() + "]";
	}
	
	
}