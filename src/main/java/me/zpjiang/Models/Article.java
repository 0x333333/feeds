package me.zpjiang.Models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Author: zhipeng
 * Date: 9/28/17 8:51 PM
 */
@Entity
public class Article {
    @Id
    private String id;

    private String title;

    private String body;

    public Article() {
    }

    public Article(String id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
