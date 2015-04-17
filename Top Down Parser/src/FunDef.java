class FunDef extends TreeNode
{
	FunHeader header; // cannot be null
	Exp exp; // cannot be null
	
	FunDef (FunHeader h, Exp e)
	{
		header = h;
		exp = e;
	}
	
	void printParseTree(String indent)
	{
		String indent1 = indent + " ";
		
		IO.displayln(indent + indent.length() + " <fun def>");
		header.printParseTree (indent1);
		exp.printParseTree (indent1);
		IO.displayln("");
	}	
}
