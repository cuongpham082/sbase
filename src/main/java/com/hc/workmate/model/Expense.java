package com.hc.workmate.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
 
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String item;
    private float amount;
     
    public Expense() {
    }
 
    public Expense(String item, float amount) {
        this.item = item;
        this.amount = amount;
    }
 
    // getters and setters are hidden for brevity
 
    @Override
    public String toString() {
        return id + ". " + item + " - " + amount + " USD";     
    }  
}
