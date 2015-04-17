abstract class FunOp extends TreeNode
{
}

class FunOp_Id extends FunOp {

	String id;

	FunOp_Id(String ident)
	{
		id = ident;
	}

	void printParseTree(String indent)
	{
		IO.displayln(indent + indent.length() + " <funop id> " + id);
	}
}

class FunOp_pair extends FunOp
{
	void printParseTree(String indent)
	{
		IO.displayln(indent + indent.length() + " pair");
	}
}

class FunOp_first extends FunOp
{
	void printParseTree(String indent)
	{
		IO.displayln(indent + indent.length() + " first");
	}
}

class FunOp_second extends FunOp
{
	void printParseTree(String indent)
	{
		IO.displayln(indent + indent.length() + " second");
	}
}
