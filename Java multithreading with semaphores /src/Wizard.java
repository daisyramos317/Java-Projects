import java.util.*;
/**
 * 
The Wizard class presents the two reports from the print class (duration time including resting time) and
the time it took for each competitor to pass each of the obstacles. (excluding the resting time between obstacles)
The wizard will present these reports and announce the winner.
 * @author Daisy Ramos
 *
 */
public class Wizard implements Runnable{
	String comp = "Competitor No. ";
	
	boolean allArrived = false;
	boolean pointsReady = false;
	
	int max;	
	Random rand;
	Object a;
	boolean iswizardbusy;
	Queue<Integer>arrivalorder;
	
	boolean[] completionCheck;

	int[][] answerlist;
	int[] pointsArray;
	
	int[] totalpoints;

	Competition c;
	long st;
	
	
	
	public Wizard(int x, Competition c){
		this.c = c;
		max = x;
		rand = new Random();
		pointsArray = new int[x];
		answerlist = new int[x][3];
		for (int i = 0; i < max; i++){
			Arrays.fill(answerlist[i], -1);
		}
		a= new Object();
		arrivalorder = new LinkedList<Integer>();
		completionCheck = new boolean[x]; 
		totalpoints = new int[x];
		st = c.st;
	}
	
	public void run() {
	
		try {
			
			c.mutex_startrace.acquire();
			c.printage();
			System.out.println("Wizard signals everyone to start race.");
			c.sem_ready.release();
			
			c.group = 0;
			c.printage();
			System.out.println("Wizard has arrived at the Mountain and is waiting.");
			
			//Mountain
			
			while(true){
				
				c.mt.signalCompletePassage.acquire();
				c.mt.mutex_enter.release();
				
				if(c.mt.numOfComplete == max){
					break;
				}
			}
			//Mountain Complete.
			
		//river
		while(true){
		c.r.tellwizard.acquire();
		
		for(int i = 1; i< c.r.gs; i++)
			c.r.sem_go.release();
		
		c.r.cmutex.release();
		
		if(c.r.wizardDone)
			break;
		}
		
		
		
		
		//Print
		c.rp.reportFinished.acquire();
		
		c.rp.printreportone();
		c.rp.printreporttwo();
		
		
		
		int[][] tempdurations = c.rp.durationTime.clone();
		int shortloc = 0;
		int temp = tempdurations[0][3];
		
		for(int i =0; i < max; i++){
			if(temp > tempdurations[i][3]){
				shortloc = i;
				temp = tempdurations[i][3];
			}
		}
		printage();
		System.out.println("The winner is: " + comp + shortloc + ".");
		
		c.rp.wp.release();
		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	void declaretheorder(){
		System.out.println();
		for(int i = 0; i < max; i++){
			int temp1 = arrivalorder.peek();
			System.out.println(comp + temp1 + " has ended the race with Rank no. "+(i+1));
			arrivalorder.remove();
		}
		System.out.println();
		
	}
	
	void printpoints(){
		for(int i =0; i < max; i++){
			System.out.println(comp+i+" has " + totalpoints[i]+ " points.");
		}
		
	}

	public void printage(){
		System.out.print("[" + (System.currentTimeMillis() - st) + "]  ");
		}
	
	
	
	


}
