package Project3;
import java.util.*; 

import javax.persistence.*;

@Entity
public class Branch {
 int BranchID;
 int BranchNum;
 String Address;
 String PhoneNum; 
 
 HashSet<Transaction> transaction = new HashSet<Transaction>();

}
