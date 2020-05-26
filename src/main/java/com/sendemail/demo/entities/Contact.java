package com.sendemail.demo.entities;

import java.util.ArrayList;
import java.util.HashMap;


public class Contact {
	private String email;
	private Long id;
	private Boolean emailBlacklisted;
	private Boolean smsBlacklisted;
	private String createdAt;
	private String modifiedAt;
	private ArrayList<Long> listIds;
	private HashMap<String, String> attributes;

	public Contact(String email, Long listid) {
		this.email = email;
		this.listIds = new ArrayList<Long>();
		this.listIds.add(listid);
	}

	public Contact() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getEmailBlacklisted() {
		return emailBlacklisted;
	}

	public void setEmailBlacklisted(Boolean emailBlacklisted) {
		this.emailBlacklisted = emailBlacklisted;
	}

	public Boolean getSmsBlacklisted() {
		return smsBlacklisted;
	}

	public void setSmsBlacklisted(Boolean smsBlacklisted) {
		this.smsBlacklisted = smsBlacklisted;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public ArrayList<Long> getListIds() {
		return listIds;
	}

	public void setListIds(ArrayList<Long> listIds) {
		this.listIds = listIds;
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}
	
	@Override
	public String toString() {
		return "email: " + email + "\nid: " + id + "\nemailBlacklisted: " + emailBlacklisted + "\nsmsBlacklisted: "
				+ smsBlacklisted + "\ncreatedAt: " + createdAt + "\nmodifiedAt: " + modifiedAt + "\nlistIds: " + listIds
				+ "\nattributes: " + attributes + "\n\n";
	}
}
