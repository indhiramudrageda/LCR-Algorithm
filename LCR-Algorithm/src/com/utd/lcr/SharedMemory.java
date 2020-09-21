package com.utd.lcr;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class SharedMemory {
	// List of queues to hold messages 
	private List<Deque<Message>> queues;
	private List<Deque<Message>> prevQueues;
	
	public SharedMemory(int n) {
		queues = new ArrayList<>();
		while(n-- > 0) {
			queues.add(new LinkedList<>());
		}
	}
	
	public Message accessMessage(int loc) {
		if(prevQueues.get(loc).size() > 0) {
			Message message = prevQueues.get(loc).poll();
			queues.get(loc).remove(message);
			return message;
		}
		return null;
	}
	
	public void addMessage(int to, int from, Message message) {
		to = to % queues.size();
		// Access to add message to a queue is given only to previous process in the ring.
		if(to == (from+1) % queues.size()) {
			queues.get(to).offer(message);
		}
	}

	public List<Deque<Message>> getQueues() {
		return queues;
	}

	public void setQueues(List<Deque<Message>> queues) {
		this.queues = queues;
	}
	
	public List<Deque<Message>> getPrevQueues() {
		return prevQueues;
	}

	public void copyToPrevQueues() {
		this.prevQueues = new ArrayList<>();
		int n = queues.size();
		for(int i=0;i<n;i++) {
			prevQueues.add(new LinkedList<>(queues.get(i)));
		}
	}
}
