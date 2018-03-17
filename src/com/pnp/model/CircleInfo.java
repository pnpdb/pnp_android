package com.pnp.model;

import java.util.ArrayList;
import java.util.List;

public class CircleInfo {

	private String name;
	private List<CircleImgs> ui = new ArrayList<CircleImgs>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CircleImgs> getUi() {
		return ui;
	}

	public void setUi(List<CircleImgs> ui) {
		this.ui = ui;
	}

}
