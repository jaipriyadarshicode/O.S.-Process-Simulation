package scheduleProcesses;

import java.util.ArrayList;

public class ProcessDetails
{
	String processName;
	int processArrivalTime;
	int processCPUBurstTime;
	int blockingProbability;
	int counter;
	
	public ProcessDetails(String name, int arrivalTime, int CPUBurstTime, int blockingProbability)
	{
	      this.processName = name;
	      this.processArrivalTime = arrivalTime;
	      this.processCPUBurstTime = CPUBurstTime;
	      this.blockingProbability = blockingProbability;
	      this.counter = 0; //time counter = 0s
	}
	//getters and set methods
	public String getProcessName()
	{
		return this.processName;
	}
	
	public int getProcessArrivalTime()
	{
		return this.processArrivalTime; 
	}
	
	public int getProcessCPUBurstTime()
	{
		return this.processCPUBurstTime;
	}
	public int getBlockingProbability()
	{
		return this.blockingProbability;
	}
	public void setProcessCPUBurstTime(int CPUBurstTime)
	{
		this.processCPUBurstTime = CPUBurstTime;
	}
	//filter process with arrival time <= current time
	public static ArrayList<ProcessDetails> findAll (ArrayList<ProcessDetails> processList, int currentTime)
	{
		ArrayList<ProcessDetails> newQueue = new ArrayList<ProcessDetails>();
		for(ProcessDetails process : processList)
		{
		   if(process.getProcessArrivalTime() <= currentTime)
			   newQueue.add(process);
		}
		return newQueue;
	}
	//transfer process from new state to the ready state
	public static ArrayList<ProcessDetails> sendFromNewState(ArrayList<ProcessDetails> newQueue, ArrayList<ProcessDetails> readyQueue, ArrayList<ProcessDetails> resourceQueue)
	{
		if (!newQueue.isEmpty())
		{
			for(ProcessDetails process : newQueue)
			{
			   if(!readyQueue.contains(process) && !resourceQueue.contains(process))
				   readyQueue.add(process);   
			}
			newQueue.clear();
		}
		return readyQueue;	
	}
	//Timer for checking waiting process in resource queue after every 3 seconds
	public static ArrayList<ProcessDetails> threeSecondsTimer(ArrayList<ProcessDetails> resourceQueue, ArrayList<ProcessDetails> readyQueue )
	{
		ArrayList<ProcessDetails> tempResourceQueue = new ArrayList<ProcessDetails>();
		for(ProcessDetails process : resourceQueue)
		{
		  if(process.counter == 3)
		  {
			  System.out.println("process " + process.getProcessName() + " time counter = " + process.counter);
			  process.counter = 0;
			  readyQueue.add(process);
			  tempResourceQueue.add(process);  
		  }
		}
		for(ProcessDetails process : tempResourceQueue)
			resourceQueue.remove(process);
		for(ProcessDetails process : resourceQueue)
           process.counter++;
		return readyQueue;
	}
	public static void checkProcessesWaiting(ArrayList<ProcessDetails> resourceQueue)
	{
		if(!resourceQueue.isEmpty())
		{
	       for(ProcessDetails process : resourceQueue) //print process in waiting in resource queue
			   System.out.println("Process " + process.getProcessName() + " is in waiting state");
		}
		else
			System.out.println("No process is in waiting state");
	}
	public static boolean checkProcessesReady(ArrayList<ProcessDetails> readyQueue)
	{
		if(!readyQueue.isEmpty())
		{
		   for(ProcessDetails process : readyQueue) //print process in ready queue
			   System.out.println("Process " + process.getProcessName() + " is in ready state");
		   return true;
		}
		else
		{
			System.out.println("No process is in ready state");
			return false;
		}
	}
	//get selected process from ready queue on the basis of preemptive SJF algorithm
	public static ProcessDetails implementPreemptiveSJFAlgo(ArrayList<ProcessDetails> readyQueue)
	{
		int leastCPUBurst = readyQueue.get(0).getProcessCPUBurstTime();
		int leastArrivalTime = readyQueue.get(0).getProcessArrivalTime();
		ProcessDetails selectedProcess = null;
		for(ProcessDetails process : readyQueue)
		{
		   if(process.getProcessCPUBurstTime() == leastCPUBurst && process.getProcessArrivalTime() <= leastArrivalTime)
		   {
			   selectedProcess = process; 
		   }
		   else if(process.getProcessCPUBurstTime() < leastCPUBurst)
		   {
			   leastArrivalTime = process.getProcessArrivalTime();
			   leastCPUBurst = process.getProcessCPUBurstTime();
			   selectedProcess = process;
		   }
		}
		return selectedProcess;
	}
}
