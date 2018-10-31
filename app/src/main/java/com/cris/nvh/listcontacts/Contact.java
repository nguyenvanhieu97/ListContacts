package com.cris.nvh.listcontacts;


public class Contact {
	private String mName;
	private String mPhoneNumber;
	private String mPhotoUri;

	public Contact(String name, String phoneNumber, String photoUri) {
		mName = name;
		mPhoneNumber = phoneNumber;
		mPhotoUri = photoUri;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getName() {
		return mName;
	}

	public void setPhoneNumber(String number) {
		mPhoneNumber = number;
	}

	public String getPhoneNumber() {
		return mPhoneNumber;
	}

	public void setPhotoUri(String photoUri) {
		mPhotoUri = photoUri;
	}

	public String getPhotoUri() {
		return mPhotoUri;
	}
}
