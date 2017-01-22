package com.southernbox.inf.entity;

import java.io.Serializable;

public class Content implements Serializable {
    private String id;
    private int type;
    private String pic;
    private String name;
    private String intro;
    private String htmlUrl;

    public Content(String id, int type, String pic, String name, String intro,
                   String htmlUrl) {
        this.id = id;
        this.type = type;
        this.pic = pic;
        this.name = name;
        this.intro = intro;
        this.htmlUrl = htmlUrl;
    }

    public Content() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

}
