abstract class ArithOp extends FunOp
{
}

class Arith_Plus extends ArithOp
{
	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " +");
	}

}

class Arith_Minus extends ArithOp
{
	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " -");
	}

}

class Arith_Mul extends ArithOp
{
	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " *");
	}
}

class Arith_Div extends ArithOp
{
	void printParseTree(String indent)
	{
		LexArithArray.displayln(indent + indent.length() + " /");
	}
}
