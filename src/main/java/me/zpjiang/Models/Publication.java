package me.zpjiang.Models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Author: zhipeng
 * Date: 9/28/17 8:54 PM
 */
@Entity
public class Publication {
    @Id
    private String id;

    private String feedID;

    private String articleID;

    public Publication() {}

    public Publication(String id, String feedID, String articleID) {
        this.id = id;
        this.feedID = feedID;
        this.articleID = articleID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeedID() {
        return feedID;
    }

    public void setFeedID(String feedID) {
        this.feedID = feedID;
    }

    public String getArticleID() {
        return articleID;
    }

    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }
}
