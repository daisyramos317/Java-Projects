package Project3;

import java.util.*;
import javax.jdo.*;
//import javax.jdo.PersistenceManager; 
import com.objectdb.Utilities;




public abstract class CreateData {
	public static void main (String args[]){
		Customer c1 = new Customer ();
		
		PersistenceManager pm = Utilities.getPersistenceManager("airport.odb");
		pm.currentTransaction().begin();
		pm.makePersistent(c1);
	}

}
