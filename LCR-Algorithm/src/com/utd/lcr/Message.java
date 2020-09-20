package com.utd.lcr;

public class Message {
	private int ID;
	private String type;
	public static final String LEADER = "LEADER";
	public static final String LEADER_ELECTION = "LEADER_ELECTION";
	
	public Message(int ID, String type) {
		this.ID = ID;
		this.type = type;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
