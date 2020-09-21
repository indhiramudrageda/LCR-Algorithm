package com.utd.lcr;

public class Node extends Thread{
	private int ID;
	private int loc;
	private int leader;
	private int round;
	private SharedMemory memory;
	private volatile boolean beginNextRound;
	
	public Node(int ID, int loc, SharedMemory memory) {
		this.ID = ID;
		this.loc = loc;
		this.leader = -1;
		this.round = 0;
		this.beginNextRound = false;
		this.setMemory(memory);
	}
	
	public void beginNextRound() {
		// Set flag to true indicating the process can start the next round.
		setBeginNextRound(true);
	}
	
	public void initiateLeaderElection() {
		getMemory().addMessage(loc+1, loc, new Message(getID(), Message.LEADER_ELECTION));
		round++; // signifies end of previous round.
	}
	
	@Override
	public void run() {
		System.out.println("[NODE ID: "+ getID() +"] Started executing..");
		
		while(true) {
			if(canBeginNextRound()) {
				// Reset the beginNextRound flag to false.
				setBeginNextRound(false);
				
				Message message = getMemory().accessMessage(loc);
				if(message != null) {
					if(message.getType().equals(Message.LEADER)) {
						//set leader and terminate.
						setLeader(message.getID());
						getMemory().addMessage(loc+1, loc, new Message(message.getID(), Message.LEADER));
						System.out.println("[NODE ID: "+ getID() +"] Leader is found to be "+getLeader()+" in round "+ getRound());
					} else if(message.getType().equals(Message.LEADER_ELECTION)) {
						if(message.getID() < getID()) {
							// pass it to neighbor
							getMemory().addMessage(loc+1, loc, new Message(message.getID(), Message.LEADER_ELECTION));
						} else if(message.getID() == getID()) {
							// I am the leader.
							setLeader(getID());
							System.out.println("[NODE ID: "+ getID() +"] Leader is found to be "+getLeader()+" in round "+ getRound());
							getMemory().addMessage(loc+1, loc, new Message(message.getID(), Message.LEADER));
						}
					}
				}
				round++;
			}
		}
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getLoc() {
		return loc;
	}
	public void setLoc(int loc) {
		this.loc = loc;
	}
	public int getLeader() {
		return leader;
	}
	public void setLeader(int leader) {
		this.leader = leader;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public SharedMemory getMemory() {
		return memory;
	}

	public void setMemory(SharedMemory memory) {
		this.memory = memory;
	}

	public boolean canBeginNextRound() {
		return beginNextRound;
	}

	public void setBeginNextRound(boolean beginNextRound) {
		this.beginNextRound = beginNextRound;
	}

}
