package com.hu.assignment.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Account {
    @Id
    Long id;
    @Column
    String IBAN;
    @Column
    String Saldo;
    @Column
    boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getSaldo() {
        return Saldo;
    }

    public void setSaldo(String saldo) {
        Saldo = saldo;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
