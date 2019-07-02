package co.edu.unal.krunko.sitespins.dataAccess.models;

import java.util.Date;

public class Comment {

	private int pinId;
	private Date date;
	private String content;

	public Comment(int pinId, Date date, String content) {
		this.pinId = pinId;
		this.date = date;
		this.content = content;
	}

	public int getPinId() {
		return pinId;
	}

	public void setPinId(int pinId) {
		this.pinId = pinId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
