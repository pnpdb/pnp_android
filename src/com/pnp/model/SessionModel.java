package com.pnp.model;

import com.pnp.widget.SlideView;

public class SessionModel {

	private String sessionName;
	private String sessionRecord;
	private String sessionDate;
	private String userGuid;
	private SlideView slideView;

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	public String getSessionRecord() {
		return sessionRecord;
	}

	public void setSessionRecord(String sessionRecord) {
		this.sessionRecord = sessionRecord;
	}

	public String getSessionDate() {
		return sessionDate;
	}

	public void setSessionDate(String sessionDate) {
		this.sessionDate = sessionDate;
	}

	public String getUserGuid() {
		return userGuid;
	}

	public void setUserGuid(String userGuid) {
		this.userGuid = userGuid;
	}

	public SlideView getSlideView() {
		return slideView;
	}

	public void setSlideView(SlideView slideView) {
		this.slideView = slideView;
	}

}
