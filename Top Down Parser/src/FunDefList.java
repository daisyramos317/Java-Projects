class FunDefList extends TreeNode
{
	FunDef def; // cannot be null
	FunDefList list; // can be null
	
	FunDefList (FunDef d)
	{
		def = d;
	}
	
	FunDefList (FunDef d, FunDefList l)
	{
		def = d;
		list = l;
	}
	
	void printParseTree(String indent)
	{
		def.printParseTree(indent);
		
		if (list != null)
			list.printParseTree(indent);
	}
}
