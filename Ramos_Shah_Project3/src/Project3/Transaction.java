package Project3;

import java.util.*; 
import javax.persistence.*;

@Entity
public abstract class Transaction {
	
protected long TransId;
float TransAmount;
long AccountId;
String DateOfTrans;
long transTime;

//inverses
//Account account;
Terminal terminal;
Customer customer
; 
//multiplicities
HashSet<ATM> atm = new HashSet<ATM>();



}
