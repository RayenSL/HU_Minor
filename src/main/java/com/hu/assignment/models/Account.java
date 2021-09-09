package com.hu.assignment.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Account {
    @Id
    Long id;
    String IBAN;
    String Saldo;
    boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
