package me.zpjiang.Models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Author: zhipeng
 * Date: 9/28/17 8:49 PM
 */
@Entity
public class User {
    @Id
    private String id;

    private String name;

    public User() {
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String userID) {
        this.id = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
