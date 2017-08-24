package scheduleProcesses;

import java.util.*;

public class ShortestRemainingTime 
{
	
	public static void main(String[] args) 
	{
		String processName;
		int processArrivalTime, processCPUBurstTime, processBlockingProbability, numberOfProcess, currentTime = 0, executionTime = 1, randomNumber;
		ArrayList<ProcessDetails> processList = new ArrayList<ProcessDetails>();//user input list
		ArrayList<ProcessDetails> newQueue;//when process created
		ArrayList<ProcessDetails> readyQueue = new ArrayList<ProcessDetails>();//process becomes ready
		ArrayList<ProcessDetails> resourceQueue = new ArrayList<ProcessDetails>();//when process blocked & waiting for resource
		ProcessDetails currentProcess;
		Random randNum = new Random(); //generate random number
		
		Scanner scanProcessDetails = new Scanner(System.in); //scan input
		
		System.out.println("Process states demonstration, using of Preemptive SJF scheduling algorithm...");
		
		System.out.println("Enter number of jobs: ");
		numberOfProcess = scanProcessDetails.nextInt(); //scan number of jobs
		
		//scan process name, arrival time, CPU burst time and blocking probability 'p'
		System.out.printf("Enter job name, job start time, number of seconds required to complete the job & blocking probability:");
		for(int processDetailscounter = 0; processDetailscounter < numberOfProcess; processDetailscounter++)
		{
		   	processName = scanProcessDetails.next();
		   	processArrivalTime = scanProcessDetails.nextInt();
		   	processCPUBurstTime = scanProcessDetails.nextInt();
		   	processBlockingProbability = scanProcessDetails.nextInt();
		   	processList.add(new ProcessDetails(processName, processArrivalTime, processCPUBurstTime, processBlockingProbability));
		}
		
		scanProcessDetails.close(); //close scanning
		
		//run program until all user input processes terminated
		while(!processList.isEmpty())
		{
	       System.out.println("Current time: " + currentTime);
	       newQueue = ProcessDetails.findAll(processList, currentTime); //created processes ---> new state 
	       readyQueue = ProcessDetails.sendFromNewState(newQueue, readyQueue, resourceQueue); //new state ---> ready state
	       if(!resourceQueue.isEmpty()) //check in resource queue if I/O received by waiting process every 3 seconds
	    	  ProcessDetails.threeSecondsTimer(resourceQueue, readyQueue); //Uses FCFS algorithm to transfer process from resource queue to ready queue if the process stays in resourceQueue waiting for 3s
	       ProcessDetails.checkProcessesWaiting(resourceQueue);
	       //Preemptive SJF algorithm to run the processes - running state
	       if(ProcessDetails.checkProcessesReady(readyQueue))  //check ready processes
	       {
	    	   currentProcess = ProcessDetails.implementPreemptiveSJFAlgo(readyQueue);
	    	   System.out.println("Executing job: "+ currentProcess.getProcessName() + " for " + executionTime + " seconds. " + "Time remaining: " + currentProcess.getProcessCPUBurstTime());
	    	   currentProcess.setProcessCPUBurstTime(currentProcess.getProcessCPUBurstTime() - executionTime);
	    	   randomNumber = (randNum.nextInt(100 - 1) + 1);
	    	   System.out.println("Process " + currentProcess.getProcessName() + " generated random number " + randomNumber);
	    	   if(currentProcess.getProcessCPUBurstTime() == 0)//if true then ---> Termination state
	    	   {
	    		   System.out.println("Process " + currentProcess.getProcessName() + " Terminated");
	    		   processList.remove(currentProcess);
	    		   readyQueue.remove(currentProcess);   
	    	   }
	    	   else if(randomNumber <= currentProcess.getBlockingProbability())
	    	   {
	    		   resourceQueue.add(currentProcess); //blocked process ---> resource queue / wait queue 
	    		   readyQueue.remove(currentProcess);
	    	   }
	       }
	       currentTime+= executionTime; //increment current time
	       System.out.println();
		}
		System.out.printf("Current time: " + currentTime + ". No jobs to run.");
	}
}
