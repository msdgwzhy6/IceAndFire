package com.southernbox.inf.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class SecondOption extends RealmObject {

    @PrimaryKey
    private int id;
    private int type;
    private String title;
    private String jsonUrl;

    public SecondOption(int id, int type, String title, String jsonUrl) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.jsonUrl = jsonUrl;
    }

    public SecondOption() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJsonUrl() {
        return jsonUrl;
    }

    public void setJsonUrl(String jsonUrl) {
        this.jsonUrl = jsonUrl;
    }

}
