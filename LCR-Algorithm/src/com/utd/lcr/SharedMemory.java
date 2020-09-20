package com.utd.lcr;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class SharedMemory {
	private List<Deque<Message>> queues;
	
	public SharedMemory(int n) {
		queues = new ArrayList<>();
		while(n-- > 0) {
			queues.add(new LinkedList<>());
		}
	}
	
	public Message accessMessage(int loc) {
		if(queues.get(loc).size() > 0)
			return queues.get(loc).poll();
		return null;
	}
	
	public void addMessage(int to, int from, Message message) {
		to = to % queues.size();
		if(to == (from+1) % queues.size()) {
			queues.get(to).add(message);
		}
	}

	public List<Deque<Message>> getQueues() {
		return queues;
	}

	public void setQueues(List<Deque<Message>> queues) {
		this.queues = queues;
	}
}
