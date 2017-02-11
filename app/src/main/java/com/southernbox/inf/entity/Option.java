package com.southernbox.inf.entity;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Option extends RealmObject {

    @PrimaryKey
    private int id;
    private int type;
    private String icon;
    private String title;

    private RealmList<SecondOption> secondOptionList;

    public Option() {
        super();
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RealmList<SecondOption> getSecondOptionList() {
        return secondOptionList;
    }

    public void setSecondOptionList(RealmList<SecondOption> secondOptionList) {
        this.secondOptionList = secondOptionList;
    }

}
