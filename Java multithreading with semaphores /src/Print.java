import java.util.concurrent.Semaphore;

/**
 * The Print class is for generating each competitors duration time in each obstacle and 
 * how many gold points they received. 
 * @author Daisy Ramos
 *
 */

public class Print {
	
	//Semaphores
	Semaphore mutex = new Semaphore(1);
	Semaphore reportFinished = new Semaphore(0);
	Semaphore wp = new Semaphore(0);
	
  	String comp = "Competitor No. ";	
	int max, count;
	int[][] durationTime; 
	boolean printdone;
	int[] gold;
	long st;
	

	
	public Print(int x){
		max=x;
		count = 0;
		durationTime = new int[max][5];
		gold = new int[max];
	}
	
	void printreportone(){
		printage();
		for(int i=0; i< max; i++){
			printage();
			System.out.println(comp+i+" took total of " + durationTime[i][3]+"ms.");
		}
	}
	
	void printreporttwo(){
		printage();	
		for(int i=0; i< max; i++){
				printage();
				System.out.println(comp+i+" took total of " + durationTime[i][0]+"ms for Forest.");
				printage();
				System.out.println(comp+i+" took total of " + durationTime[i][1]+"ms for Mountain.");
				printage();
				System.out.println(comp+i+" took total of " + durationTime[i][2]+"ms for River.");
		}
		
	
		
		printdone = true;
		
	}
	
	void printFinal(){
		
		int winner = 0;

		for(int j = 0; j< 5; j++){
			int[][] temp = durationTime.clone();
			int shortest = temp[0][j];
			int location = 0;
			for(int i = 0; i < max;i++){
				if(shortest > temp[i][j]){
					shortest = temp[i][j];
					location = i; 
				}
			}
			if(j==4){
				winner = location;
			}else{
			if(gold[location] ==0) gold[location]=1000;
			}
		}
		
		
		for(int i=0;i<max;i++){
			printage();
			System.out.println(comp+i+" received " + gold[i] +" gold points.");
		}
		
		System.out.println("The prince is: " + comp+ winner +".");
		System.out.println();
		
	}
	
	void ready(int id, long ftime, long mtime, long rtime, long totaltime){
		count++;
		durationTime[id][0] = (int) ftime;
		durationTime[id][1] = (int) mtime;
		durationTime[id][2] = (int) rtime;
		durationTime[id][3] = (int) totaltime;
		
		if(count==max){
			reportFinished.release();
		}
		
		
	}
	public void printage(){
		System.out.print("[" + (System.currentTimeMillis() - st) + "]  ");
	}

}
