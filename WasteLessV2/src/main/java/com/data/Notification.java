package com.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity // This tells Hibernate to make a table out of this class
@Table(name="notification")

public class Notification {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
     
    @Column(name="message")
    private String message;
    
    public String toString() {
        return "EmployeeEntity [id=" + id + ", firstName=" + message + "]";
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
