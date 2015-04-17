class ExpList extends TreeNode
{
	Exp exp; // cannot be null
	ExpList list; // can be null
	
	ExpList (Exp e)
	{
		exp = e;
	}
	
	ExpList (Exp e, ExpList l)
	{
		exp = e;
		list = l;
	}
	
	void printTerms(String indent)
	{
		exp.printParseTree(indent);
		if (list != null)
			list.printTerms(indent);
	}
	
	void printParseTree(String indent)
	{
		String indent1 = indent + ' ';
		
		IO.displayln(indent + indent.length() + " <exp list>");

		printTerms(indent1);
	}
}
