package com.southernbox.inf.bean;

import java.util.List;

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
