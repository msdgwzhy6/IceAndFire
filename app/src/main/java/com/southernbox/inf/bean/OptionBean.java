package com.southernbox.inf.bean;

import java.util.List;

/**
 * Created by SouthernBox on 2016/3/27.
 * 首页选项实体类
 */

public class OptionBean {
    public List<Option> data;

    public class Option {
        public int id;
        public int type;
        public String icon;
        public String title;
        public List<SecondOption> secondOptionList;

        public class SecondOption {
            public int id;
            public int type;
            public String title;
            public String jsonUrl;
        }
    }

}
