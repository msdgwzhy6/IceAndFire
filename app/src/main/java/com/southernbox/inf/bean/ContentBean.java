package com.southernbox.inf.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by SouthernBox on 2016/3/27.
 * 列表实体类
 */

public class ContentBean implements Serializable {
    public List<Content> data;

    public class Content implements Serializable {
        public String id;
        public int type;
        public String pic;
        public String name;
        public String intro;
        public String htmlUrl;
    }
}
