package com.tweetapp.dto;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



public class Comment {
     @Id
	private String username;
	private String comment;

	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Comment(String username, String comment) {
		super();
		this.username = username;
		this.comment = comment;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
