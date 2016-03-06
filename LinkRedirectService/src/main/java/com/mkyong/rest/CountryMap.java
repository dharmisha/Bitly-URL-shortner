package com.mkyong.rest;

public class CountryMap {
	
	String url;
	String country;
	int count;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Country [url=" + url + ", country=" + country + ", count=" + count + "]";
	}
}
