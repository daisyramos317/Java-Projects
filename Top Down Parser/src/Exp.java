abstract class Exp extends TreeNode
{
}

class Exp_Id extends Exp {

	String id;

	Exp_Id(String ident)
	{
		id = ident;
	}

	void printParseTree(String indent)
	{
		IO.displayln(indent + indent.length() + " <exp id> " + id);
	}
}

class Exp_Int extends Exp
{
	int value;

	Exp_Int(int n)
	{
		value = n;
	}

	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " <exp int> " + value);
	}

}

class Exp_Float extends Exp
{
	float value;

	Exp_Float(float f)
	{
		value = f;
	}

	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " <exp float> " + value);
	}
}

class Exp_If extends Exp
{
	Exp expr1, expr2, expr3; 
	
	Exp_If (Exp e1, Exp e2, Exp e3)
	{
		expr1 = e1;
		expr2 = e2;
		expr3 = e3;
	}
	
	void printParseTree(String indent)
	{
		String indent1 = indent + ' ';
		
		LexArithArray.displayln(indent + indent.length() + " if");
		expr1.printParseTree(indent1);
		LexArithArray.displayln(indent + indent.length() + " then");
		expr2.printParseTree(indent1);
		LexArithArray.displayln(indent + indent.length() + " else");
		expr3.printParseTree(indent1);
	}
}

class Fun_Exp extends Exp
{
	FunOp operator;
	ExpList exp_list;
	
	void printParseTree(String indent)
	{
		String indent1 = indent + ' ';
		
		IO.displayln(indent + indent.length() + " <fun exp>");
		operator.printParseTree(indent1);
		exp_list.printParseTree(indent1);
	}
}

class Exp_Nil extends Exp
{
	void printParseTree(String indent)
	{
		IO.displayln(indent + indent.length() + " <exp nil>");
	}	
}
