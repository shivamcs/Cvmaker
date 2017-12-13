package com.kodecamp.form.fragment;

public class Introduction {

	private String name;
	private String profession;

	public Introduction(final String name, final String profession) {
		this.name = name;
		this.profession = profession;
	}

	public String getName() {
		return name;
	}

	public String getProfession() {
		return profession;
	}

	@Override
	public String toString() {
		return "Name : "+this.name+" Profession :  "+profession;
	}
	
	
}
