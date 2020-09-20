package com.utd.lcr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;

public class LCRAlgorithm {
	private int numberOfProcesses;
	private int[] IDs;
	private Node[] processes;
	private static final String CONFIG_FILE = "input.dat";
	
	public LCRAlgorithm(int numberOfProcesses, int[] IDs) {
		this.numberOfProcesses = numberOfProcesses;
		this.IDs = IDs;
		this.processes = new Node[numberOfProcesses];
		
		//Spawn n threads passing its ID, location in ring and shared memory object.
		SharedMemory memory = new SharedMemory(numberOfProcesses);
		for(int i=0;i<numberOfProcesses;i++) {
			Node node = new Node(IDs[i], i, memory);
			processes[i] = node;
			node.start();
		}
		synchronizeNodes();
	}
	
	public void synchronizeNodes() {
		int runningProcesses = getNumberOfProcesses();
		int round = 0;
		
		// Intiating LCR algorithm at all processes
		for(int i=0;i<getNumberOfProcesses();i++) {
			Node node = getProcesses()[i];
			node.initiateLeaderElection();
		}
		
		while(runningProcesses > 0) {
			//1. Check if previous round is executed by all the running processes.
			checkRoundCompletion(round, runningProcesses);
			
			//2. Start the next round.
			round++;
			for(int i=0;i<getNumberOfProcesses();i++) {
				Node node = getProcesses()[i];
				if(!node.isInterrupted()) {
					if(node.getLeader() == -1) {
						node.beginNextRound();
					} else {
						node.interrupt();
						runningProcesses--;
					}
				}
			}
		}
	}

	private void checkRoundCompletion(int round, int runningProcesses) {
		while(true) {
			int completedProcesses = 0;
			for(int i=0;i<getNumberOfProcesses();i++) {
				Node node = getProcesses()[i];
				if(!node.isInterrupted()) {
					if(node.getRound() == round+1)
						completedProcesses++;
				}
			}
			if(completedProcesses == runningProcesses)
				return;
		}
	}

	public int getNumberOfProcesses() {
		return numberOfProcesses;
	}

	public void setNumberOfProcesses(int numberOfProcesses) {
		this.numberOfProcesses = numberOfProcesses;
	}

	public int[] getIDs() {
		return IDs;
	}

	public void setIDs(int[] iDs) {
		IDs = iDs;
	}
	
	public Node[] getProcesses() {
		return processes;
	}

	public void setProcesses(Node[] processes) {
		this.processes = processes;
	}

	public static void main(String[] args) {
		//Read Input file
		try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE))) {
			int n = Integer.parseInt(br.readLine());
			int[] IDs = new int[n];
			String str = br.readLine();
			String[] list = str.split(" ");
			for(int i=0;i<n;i++) {
				IDs[i] = Integer.parseInt(list[i]);
			}
			new LCRAlgorithm(n, IDs);
		} catch(IOException ie) {
			
		}
			
	}

}
