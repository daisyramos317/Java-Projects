class FunHeader extends TreeNode
{
	String function_name; // cannot be null
	ParameterList parameter_list; // can be null (when there are no parameters)
	
	FunHeader (String n, ParameterList params)
	{
		function_name = n;
		parameter_list = params;
	}
	
	void printParseTree(String indent)
	{
		String indent1 = indent + " ";
		
		IO.displayln(indent + indent.length() + " <header>");
		IO.displayln(indent1 + indent1.length() + " <fun name> " + function_name);
		
		if (parameter_list != null)
			parameter_list.printParseTree(indent1);
		else
			IO.displayln(indent1 + indent1.length() + " <parameter list>");
	}	
}
