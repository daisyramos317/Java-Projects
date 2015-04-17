public class FunExp extends Exp
{
	FunOp op; // cannot be null
	ExpList exp_list; // can be null
	
	FunExp (FunOp o, ExpList l)
	{
		op = o;
		exp_list = l;
	}
	
	void printParseTree(String indent)
	{
		String indent1 = indent + ' ';
		
		IO.displayln(indent + indent.length() + " <fun exp>");
		op.printParseTree(indent1);
		
		if (exp_list != null)
			exp_list.printParseTree(indent1);		
	}
}
