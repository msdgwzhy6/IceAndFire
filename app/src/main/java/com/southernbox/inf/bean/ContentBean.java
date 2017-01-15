package com.southernbox.inf.bean;

import java.io.Serializable;
import java.util.List;

public class ContentBean implements  Serializable{
	public List<Content> data;

	public class Content implements Serializable{
		public String id;
		public int type;
		public String pic;
		public String name;
		public String intro;
		public String htmlUrl;
	}
}
