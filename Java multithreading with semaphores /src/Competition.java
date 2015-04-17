import java.util.concurrent.*;
/**
 * Competition class to call Competitor class in main method as a thread 
 * to start the race
 * @author Daisy Ramos
 * 
 */


public class Competition {
	
	Semaphore mutexprint = new Semaphore(1);
	Semaphore mutex_ready;
	Semaphore mutex_startrace;
	Semaphore mutex_competitor_completed_forest;	
	
	Semaphore sem_ready;
	

	int group = 0;
	
	Forest f;
	Mountain mt;
	River r;
	Print rp;
	int x;
	Thread[] b;
	long st;

	
	public Competition(int n){
		
		//Semaphores
		mutex_ready = new Semaphore(1);
		mutex_startrace = new Semaphore(0);
		sem_ready = new Semaphore(0);
		
		x = n;
		f = new Forest(x);
		mt = new Mountain(x);
		r = new River(x);
		rp = new Print (x);		
		
		b = new Thread[x];
		
		st = System.currentTimeMillis();
		f.st = st;
		mt.st = st;
		r.st = st;
		rp.st= st;
		
		
	}
	

	public static void main(String[] args) throws InterruptedException {
		
		int numberofcompetitor = 8;
		
		Competition c = new Competition(numberofcompetitor);
		
		for (int i = 0; i < c.x; i++){
			c.b[i] = new Thread(new Competitor(i,c));
			c.b[i].start();
		}
		
		Thread TW = new Thread(new Wizard(numberofcompetitor,c));
		TW.start();
		
	}
	
	
	public void printage(){
		System.out.print("[" + (System.currentTimeMillis() - st) + "]  ");
	}
	
	

}
