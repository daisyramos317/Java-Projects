package Project3;

import javax.persistence.*;

@Entity
public class Transfer extends Transaction {
protected int Account2_ID;
protected int transferid;

public Transfer(int n, int Tid, int transid, float amount, int Aid, String TDate, long Ttime){
	Account2_ID=n;
	transferid= Tid;
    TransId= transid;
    TransAmount= amount;
    AccountId= Aid;
    DateOfTrans= TDate;
    transTime= Ttime;
    
}
}