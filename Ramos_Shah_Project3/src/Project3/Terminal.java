package Project3;
import java.util.*;  

import javax.persistence.*;

@Entity
public class Terminal {
	protected int TerminalID;
	String DateAccessed;
	
	public HashSet<Transaction> transaction= new HashSet<Transaction>();
	//inverse
	Terminal terminal;
	
}
