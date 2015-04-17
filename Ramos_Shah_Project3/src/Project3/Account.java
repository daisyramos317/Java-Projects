package Project3;
import java.util.*;

import javax.persistence.*;

@Entity
public abstract class Account {
	int AccountID;
	float Balance;
	String DateOpened;
	String DateClosed;
	
	
	HashSet<Transaction> transaction= new HashSet<Transaction>();
	HashSet<Branch> branch= new HashSet<Branch>();
	
}
