import java.util.Random;     
import java.util.concurrent.Semaphore;

/**
 * The Mountain Class is where only one competitor can be on the passage at a given time.
   If the passage is free, the competitor will proceed and pass the passage (sleep of random time).

 * 
 * @author Daisy Ramos
 *
 */


public class Mountain {
	
	long st;
	String compn = "Competitor No. ";
	
	boolean allArrived = false;
	boolean pointsReady = false;
	
	
	int numOfComplete = 0;
	int numOfComp;
	int whosturn = 0;	

	boolean[] completionCheck;
	int[] timeElapsed;	
	int[] line;	
	int[] pointsArray;	
	int count = 0;
	
	Semaphore mutex_enter = new Semaphore(1);
	Semaphore signalCompletePassage = new Semaphore(0);
	Semaphore mutex_decreasecount = new Semaphore(1);
	Semaphore sem_waitforpointgeneration = new Semaphore(0);
	
	public Mountain(int numOfCompetitors){
		
		line = new int[numOfCompetitors];
		numOfComp = numOfCompetitors;
		completionCheck = new boolean[numOfComp];
		timeElapsed = new int[numOfComp];
		pointsArray = new int[numOfComp];
		
	}
	
	

	int enter(int id) throws InterruptedException{
		
		printage();
		System.out.println(compn + id + " has entered the passage.");
		Random rand = new Random();
		int randtime = rand.nextInt(100);
		Thread.sleep(randtime);
		timeElapsed[id] = randtime;
		printage();
		System.out.println(compn + id + " completed with runtime of " + randtime + "ms.");
		return randtime;
	}	
	
	/**
	 * The competitor with the shortest traversal time will receive 8 points,
    the second will receive 6 points, and the third will receive 3 points. 
    The last ones will get no points. After everyone has passed the passage, 
    they’ll all move on to the river obstacle.
	 * @param id
	 * @throws InterruptedException
	 */
	void generateAndRewardPoints(int id) throws InterruptedException{
		
		if(numOfComplete == numOfComp){
			
			int[] tempArray = timeElapsed.clone();
			int shortloc=0;
			int shorttime=9999;
			
			for(int j = 0; j<3; j++){
				for (int i = 0 ; i < numOfComp; i++){
					if (tempArray[i] < shorttime){
						shortloc = i;
						shorttime = timeElapsed[i];
					}
				}
				tempArray[shortloc]=999;
				if(j ==0) pointsArray[shortloc] = 8;
				if(j ==1) pointsArray[shortloc] = 6;
				if(j ==2) pointsArray[shortloc] = 3;
				shorttime = 999;
				shortloc = 0;
			}

			printage();
			System.out.println("All competitors have completed the mountain.");
			sem_waitforpointgeneration.release();
			
		}else{
			sem_waitforpointgeneration.acquire();
			sem_waitforpointgeneration.release();
		}
	}
	
	
	void decreasecount(){
		count++;
		if(count==numOfComp){
			printage();			
			System.out.println();
		}
	}
	public void printage(){
		System.out.print("[" + (System.currentTimeMillis() - st) + "]  ");
	}

}
