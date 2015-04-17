abstract class BoolOp extends FunOp
{
}

class Bool_And extends BoolOp
{
	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " and");
	}

}

class Bool_Or extends BoolOp
{
	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " or");
	}
}

class Bool_Not extends BoolOp
{
	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " not");
	}
}
