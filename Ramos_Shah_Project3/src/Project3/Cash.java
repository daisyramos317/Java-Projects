package Project3;
import javax.persistence.*;

@Entity
public class Cash extends Transaction
{

	protected float counted_Amount;
	protected int cash_counter_id;
	
	public Cash(int counted, int cash_counted,int transid, float amount, int Aid, String TDate, long Ttime  ){
		counted_Amount= counted;
		cash_counter_id= cash_counted;
		TransId= transid;
		TransAmount= amount;
		AccountId= Aid;
		DateOfTrans= TDate;
		transTime= Ttime;
		
	}
}	
