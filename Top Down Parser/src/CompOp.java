abstract class CompOp extends FunOp
{
}

class Comp_Eq extends CompOp
{
	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " =");
	}
}

class Comp_Gt extends CompOp
{
	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " >");
	}

}
class Comp_Ge extends CompOp
{
	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " >=");
	}

}
class Comp_Lt extends CompOp
{
	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " <");
	}

}
class Comp_Le extends CompOp
{
	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " <=");
	}

}
