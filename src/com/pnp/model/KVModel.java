package com.pnp.model;

public class KVModel {

	private String key;
	private String value;
	private boolean isAvator;

	public KVModel(String k, String v, boolean i) {
		this.key = k;
		this.value = v;
		this.isAvator = i;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isAvator() {
		return isAvator;
	}

	public void setAvator(boolean isAvator) {
		this.isAvator = isAvator;
	}

}
