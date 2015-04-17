package Project3;
import javax.persistence.Entity;

@Entity
public class Deposit extends Transaction{
	int dep_id;
	
	public Deposit (int Did,int transid, float amount, int Aid, String TDate, long Ttime ){
		dep_id= Did;
		TransId= transid;
		TransAmount= amount;
		AccountId= Aid;
		DateOfTrans= TDate;
		transTime= Ttime;
		
	}

}
