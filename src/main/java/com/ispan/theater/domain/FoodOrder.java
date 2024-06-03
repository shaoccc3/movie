package com.ispan.theater.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Food_Order")
public class FoodOrder {

//	@MapsId
//    @ManyToOne
//    @JoinColumns({
//        @JoinColumn(name = "cartId", referencedColumnName = "cartId"),
//        @JoinColumn(name = "userId", referencedColumnName = "userId")
//    })
//    private FoodCart foodCart;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "foodOrder_id")
	private Integer id;
	
//	@ManyToOne(fetch = FetchType.LAZY, optional = false)
//	@JoinColumn(name = "cart_id", nullable = false)
//	private Cart cart; 
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
	
//	@ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "food_id", nullable = false)
//    private Food food;
//	
//	@Column(name = "buy_number", nullable = false)
//	private Integer buyNumber;
	
//	@Column(name = "total_price", nullable = false)
//    private Integer totalPrice;
	
//	@OneToMany(mappedBy = "cartOrder", fetch = FetchType.LAZY)
//    private List<FoodCart> foodCarts;
	
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date", nullable = false)
    private Date modifyDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date", nullable = false)
    private Date orderDate;

	@Override
	public String toString() {
		return "FoodOrder [id=" + id + ", user=" + user + ", createDate=" + createDate + ", modifyDate=" + modifyDate
				+ ", orderDate=" + orderDate + "]";
	}
    
    

}
