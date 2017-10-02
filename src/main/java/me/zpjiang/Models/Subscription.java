package me.zpjiang.Models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Author: zhipeng
 * Date: 9/28/17 8:52 PM
 */
@Entity
public class Subscription {

    @Id
    private String id;

    private String userID;

    private String feedID;

    public Subscription() {
    }

    public Subscription(String id, String userID, String feedID) {
        this.id = id;
        this.userID = userID;
        this.feedID = feedID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFeedID() {
        return feedID;
    }

    public void setFeedID(String feedID) {
        this.feedID = feedID;
    }
}
