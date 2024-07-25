package com.cogent.bank_customer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CustomerAccountDaoImp implements CustomerAccountDao {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String user;
    private String pass;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


    @Override
    public String toString() {
        return "Customer Account :" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", pass='" + "*****" + '\'';
    }
}