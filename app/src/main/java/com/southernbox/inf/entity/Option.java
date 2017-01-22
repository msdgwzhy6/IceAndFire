package com.southernbox.inf.entity;

import java.util.List;

public class Option {
	private int id;
	private int type;
	private String icon;
	private String title;
	private List<SecondOption> secondOptionList;

	public Option(int id, int type, String icon, String title,
			List<SecondOption> secondOptionList) {
		super();
		this.id = id;
		this.type = type;
		this.icon = icon;
		this.title = title;
		this.secondOptionList = secondOptionList;
	}

	public Option(int id, int type, String icon, String title) {
		super();
		this.id = id;
		this.type = type;
		this.icon = icon;
		this.title = title;
	}

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

	public List<SecondOption> getSecondOptionList() {
		return secondOptionList;
	}

	public void setSecondOptionList(List<SecondOption> secondOptionList) {
		this.secondOptionList = secondOptionList;
	}

}
