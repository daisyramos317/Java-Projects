import java.util.*;  
import java.util.concurrent.Semaphore;
/**
 * The River class extends the Thread class and generates once a group is created, 
 * the last competitor to form the group will signal the wizard.
 *   
 * @author Daisy Ramos 
 *
 */
public class River extends Thread{

	
	long st;
	String compn = "Competitor No. ";
	
	boolean allArrived = false;
	boolean pointsReady = false;
	
	int max;	
	Random rand;
	int arrivedcount;
	int printpointscount = 0;
	
	long[] arrivaltimelist;
	long[] differencelist;
	
	boolean[] completionCheck;
	int[] timeelapsed;
	int[] rivercrossingtime;
	
	
	int[] pointsArray;	
	boolean[] checkinlist;
	int[] totalpoints;
	
	int checkin = 0;
	int gs = 3;
	int donecrossing = 0;
	
	boolean wizardDone = false;
	
	Semaphore mutexpr = new Semaphore(0);
	Semaphore sem_go = new Semaphore(0);
	Semaphore cmutex = new Semaphore(1);
	Semaphore mutexcr = new Semaphore(1);
	Semaphore tellwizard = new Semaphore(0);
	Semaphore wizardinteraction = new Semaphore(0);
	

	
	
	public River(int x){
		max = x;
		checkinlist = new boolean[x];
		arrivaltimelist = new long[x];
		differencelist = new long[x];
		rivercrossingtime = new int[x];
		pointsArray = new int[x];
		totalpoints = new int[x];
		rand = new Random();
		arrivedcount = 0;
		
	}
/**
 * Once the group is created the last competitor who forms the group will signal the wizard will release the
 *  group and its competitors will cross the river.
 * @throws InterruptedException
 */
	
	void checkin() throws InterruptedException{
		checkin++;
		if((checkin % gs) != 0 && (checkin != max)){
			cmutex.release();			
			sem_go.acquire();
			
		}else{
			if(checkin==max){
				wizardDone = true;
			}			
			tellwizard.release();			
		}
	}

	void passthrueriver(int id) throws InterruptedException{
		
		int tempt = rand.nextInt(50);
		Thread.sleep(tempt);
		rivercrossingtime[id] = tempt;
		donecrossing++;
		if(donecrossing==max){
			rewardpoints();
			mutexpr.release();
		}
	}
	
/**
 *   The competitor with the shortest traversal time will receive 8 points, 
 *   the second will receive 6 points, 
     and the third will receive 3 points. The last ones will get no points.
 * 
 */
	void rewardpoints(){
		
		int[] temp2 = rivercrossingtime.clone();
		
		for(int j = 0 ; j < 3; j++){
			int shortest = temp2[0];
			int loc = 0;
			for(int i = 0; i < max; i++){
				
				if(shortest > temp2[i]){
					shortest = temp2[i];
					loc = i;
				}
			}
			if(j == 0) pointsArray[loc] = 8;
			if(j == 1) pointsArray[loc] = 6;
			if(j == 2) pointsArray[loc] = 3;
			temp2[loc] = 99999;	
		}
		
		pointsReady = true;
	}
	
	
	 void printpoints(int id, int points){
		
		 printpointscount++;
		 totalpoints[id] = points;
		
		 if(printpointscount==max){
			 
			for (int i = 0; i<max;i++){
				printage();
				System.out.println(compn+i+"'s river crossing time is: "+rivercrossingtime[i]+"ms.");
			} 
			
			for(int i =0; i < max; i++){
				printage();
				System.out.println(compn+i+" has " + totalpoints[i]+ " points.");
			}
		 }
	}

	 
		public void printage(){
			System.out.print("[" + (System.currentTimeMillis() - st) + "]  ");
		}

	 
}