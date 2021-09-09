package com.hu.assignment.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AccountHolder {
    @Id
    Long id;
    String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
