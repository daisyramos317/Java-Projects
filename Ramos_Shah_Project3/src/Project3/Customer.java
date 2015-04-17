package Project3;
import java.util.*;

import javax.persistence.Entity;

@Entity
public class Customer {

	int Customerid;
	String CustName;
	String CustAddress;
	
	public String toString()
	{
		return "Customerid"+ "CustName"+ CustAddress;
	}
	

   //inverse
	Transaction transaction;
	
	HashSet <Transaction> trans = new HashSet <Transaction>();
	HashSet <Branch> branch= new HashSet<Branch>();
}
