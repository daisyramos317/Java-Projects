import java.util.Arrays;   
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Forest Class is where the competitor will have to find in the forest a map that 
 * contains a magic word. The forest will be a file of more than 300 but less 
 * than 600 randomly generated words of 5 letters chosen from the set {a,b,c,d}. 
 * The length of the magic word will also be 5 letters long, containing letters from the same set {a,b,c,d}
 *  When the map with the magic word is found, the competitor can leave the forest. 
 *  If the competitor searched the entire forest, and still didn’t find the 
 * magic word, he will be penalized (by having to yield( ) the CPU twice).
 * 
 * @author Daisy Ramos
 * 
 */
public class Forest{
	static String alphabet = "abcd";
	String comp = "Competitor No. ";
	String magicword;
	String[] forestArray;
	Object[] All;
	Random rand;	
	boolean isFound;
	int max;
	String[] magicwords;
	String[][] forestArrays;
	boolean isFoundList[];
	Object a;
	Object b;
	int rank[];
	int points[];
	int arrivedCount=0;
	int count = 0;
	long st;
	
	Semaphore mutexEnter = new Semaphore(1);
	Semaphore mutexSearch = new Semaphore(1);
	Semaphore mutexCompleted = new Semaphore(1);
	Semaphore mutexExit = new Semaphore(0);
	Semaphore mutexDecreaseCount = new Semaphore(1);
	

	
	public Forest(int x){
		
		max = x;
		magicword = "";
		a = new Object();
		b = new Object();
		magicwords = new String[max];
		forestArrays = new String[max][600];
		isFoundList = new boolean[max];
		rank = new int[max];
		Arrays.fill(rank, -1);
		points = new int[max];
		rand = new Random();
		this.generateRandom();
	}
	
	void decreasecount(){
		count--;
		if(count==0){
			printage();
			System.out.println();
		}
	}
	
	void enter(int id){
		
			count++;
			if(count == 1){
				System.out.println();
				printage();
			}
		
		System.out.println(
				"[" + (System.currentTimeMillis() - st) + "]  "+
				comp+id+" entered the forest.");
	}
	
	void search(int id){
		System.out.println(
				
				"[" + (System.currentTimeMillis() - st) + "]  "+
				"Competitor No. " + id + " is searching for the magicword.");
		boolean foundornot = false;
		int i = 0;
		while(forestArrays[id][i] != null){
			if(magicwords[id].equals(forestArrays[id][i])) foundornot = true;
			i++;
		}
		
		if(foundornot){
			isFoundList[id] = true;
			int w = 0;
			
			while(rank[w]!=-1){
				w++;
			}
			
			rank[w]=id;
			
			System.out.println(
					"[" + (System.currentTimeMillis() - st) + "]  "+
					comp + id + " found the magicword.");
		}else{
			isFoundList[id] = false;
			
			System.out.println(
					"[" + (System.currentTimeMillis() - st) + "]  "+
					comp + id + " has failed to find the magicword.");
		}
	}
	
	
	void completed(int id){
		
		arrivedCount++;
		if(arrivedCount == max){
			printage();
			System.out.println("All competitors have left the forest.");
			
			int count = 0;
			for(int i : rank){
				if (i!=-1) {
					count++;
				}
			}
			
			for(int i = 0; i < max; i++){
				for(int q=0; q<max;q++){
					if (i == rank[q]){
						points[i] = count-q;
					}	
				}
			
			}
			this.mutexExit.release();
		}
		
	}
		

	
	void generateRandom(){
		
		String tempWord = "";
		
		for (int j = 0; j < max; j++){
			for (int i = 0; i < 5; i++){
				int tempran = rand.nextInt(4);
				tempWord = tempWord + alphabet.charAt(tempran);
			}
			magicwords[j] = tempWord;
			tempWord = "";
		}
		
		int countOfForest = rand.nextInt(301) + 300;
		
		for(int j = 0; j < max; j++){
			for(int i = 0 ; i < countOfForest ; i++){
				tempWord ="";
				for (int q = 0; q < 5; q++){
					int tempran = rand.nextInt(4);
					tempWord = tempWord + alphabet.charAt(tempran);
				}			
				forestArrays[j][i] = tempWord;
			}
		}
	}
	
	
	public void printage(){
		System.out.print("[" + (System.currentTimeMillis() - st) + "]  ");
	}

}
 