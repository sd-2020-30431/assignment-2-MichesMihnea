package com.data;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // This tells Hibernate to make a table out of this class
@Table(name="item")
public class Item {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
     
    @Column(name="name")
    private String name;
     
    @Column(name="quantity")
    private int quantity;
     
    @Column(name="calories")
    private int calories;
    
    @Column(name="purchase")
    private Date purchase;
    
    @Column(name="expiration")
    private Date expiration;
     
    //Setters and getters left out for brevity.
 
    @Override
    public String toString() {
        return "ItemEntity [id=" + id + ", name =" + name + 
                ", quantity=" + quantity + ", purchase=" + purchase.toString()   + "]";
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public Date getPurchase() {
		return purchase;
	}

	public void setPurchase(Date purchase) {
		this.purchase = purchase;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
}