/**
 * Competitor class to pass thread constructor subclass 
 * @author Daisy Ramos
 * 
 */

import java.util.*;

public class Competitor implements Runnable{

	String comp = "Competitor No. ";
	int ID;
	Thread thread = new Thread();
	Random rand;
	int points;
	Mountain mt;
	Forest f;
	River r;
	Print rp;
	
	boolean foundMatchInForest;
	Wizard w;
	Thread[] t;
	
	long beg;
	long end;
	long total;
	
	long duration_forest;
	long duration_wizard;
	long duration_river;
	long duration_mountain;
	
	long tempbeg;
	long tempend;
	
	long st;
	
	Competition c;
	
	public Competitor(int id, Competition d){
		c = d;
		beg = System.currentTimeMillis();
		ID = id;
		rand = new Random();
		mt = c.mt;
		f = c.f;
		r  = c.r;
		rp = c.rp;
		points = 0;
		st = c.st;
	}
	
	public void rest() throws InterruptedException{
		int temp  = rand.nextInt(21)+40;
		Thread.sleep(temp);
	}
	
	public void printpoints() throws InterruptedException{
		c.mutexprint.acquire();
		c.printage();
		System.out.println(comp+ID+" has "+points+" points.");
		c.mutexprint.release();
	}
	
	public void run() {
		
			try{
			c.mutex_ready.acquire();
				c.group++;
				if(c.group == c.x){
					c.mutex_startrace.release();
				}
				
				c.mutexprint.acquire();
				c.printage();
				System.out.println(comp + ID + " awake.");
				c.mutexprint.release();
				
			c.mutex_ready.release();
			c.sem_ready.acquire();
			c.sem_ready.release();
			
			//resting before the race
			this.rest();
			
           //Forest	
			
			int temp = rand.nextInt(5) + Thread.currentThread().getPriority();
			this.thread.setPriority(temp);
			
			tempbeg = System.currentTimeMillis();
			
			f.mutexEnter.acquire();
			f.enter(ID);
			f.mutexEnter.release();
			
			f.mutexSearch.acquire();
			f.search(ID);
			f.mutexSearch.release();
			
			if(f.isFoundList[ID]){
				Thread.currentThread().setPriority(5);
			}else{
				Thread.currentThread().setPriority(5);
				Thread.currentThread();
				Thread.yield();
				Thread.currentThread();
				Thread.yield();
			}
			
			f.mutexCompleted.acquire();
			f.completed(ID);
			f.mutexCompleted.release();
			
			f.mutexExit.acquire();
			f.mutexExit.release();
			
			points =+ f.points[ID];			
			printpoints();
			
			tempend = System.currentTimeMillis();
			duration_forest = tempend - tempbeg;
			
			
			f.mutexDecreaseCount.acquire();
			f.decreasecount();
			f.mutexDecreaseCount.release();
			
			//End of Forest
			
			this.rest();
			
            //Mountain
			tempbeg = System.currentTimeMillis();
			
			mt.mutex_enter.acquire();
			
			duration_mountain = mt.enter(ID);
			
			mt.numOfComplete++;
			mt.signalCompletePassage.release();
			
			tempend = System.currentTimeMillis();
	
			
			
			mt.generateAndRewardPoints(ID);			
			points +=mt.pointsArray[ID];
			
			printpoints();
			
			mt.mutex_decreasecount.acquire();
			mt.decreasecount();
			mt.mutex_decreasecount.release();
			
            //End of Mountain
			
			this.rest();
		
            //River	
			tempbeg = System.currentTimeMillis();
			r.cmutex.acquire();
			r.checkin();
			
			c.mutexprint.acquire();
			c.printage();
			System.out.println(comp + ID + " started crossing the river.");
			c.mutexprint.release();
			
			r.mutexcr.acquire();
			r.passthrueriver(ID);
			r.mutexcr.release();
			
			tempend = System.currentTimeMillis();
			duration_river = r.rivercrossingtime[ID];
			total = tempend-c.st;
			
			
			r.mutexpr.acquire();
			points += r.pointsArray[ID];
			r.printpoints(ID, points);
			r.mutexpr.release();
		    //End of River
		
			
        //Report
		rp.mutex.acquire();
		rp.ready(ID, duration_forest, duration_mountain, duration_river, total);
		rp.mutex.release();
		
		c.rp.wp.acquire();
		c.rp.wp.release();
		
		
		}catch(InterruptedException e){
		}
	}
	
	
	
	public void printage(){
		System.out.print("[" + (System.currentTimeMillis() - st) + "]  ");
	}
	
	}
		