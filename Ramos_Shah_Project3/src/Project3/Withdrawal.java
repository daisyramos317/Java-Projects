package Project3;
import javax.persistence.*;

@Entity
public class Withdrawal extends Transaction{
	int with_id;
	
	public Withdrawal(int Wid,int transid, float amount, int Aid, String TDate, long Ttime){
		with_id= Wid;
		TransId= transid;
		TransAmount= amount;
		AccountId= Aid;
		DateOfTrans= TDate;
		transTime= Ttime;
	}
}
