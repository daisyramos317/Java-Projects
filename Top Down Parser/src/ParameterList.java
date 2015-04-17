class ParameterList
{
	String id; // cannot be null (the empty list is represented by a null ParameterList object)
	ParameterList list; // can be null
	
	ParameterList (String first_param, ParameterList params)
	{
		id = first_param;
		list = params;
	}
	
	void printParameters(String indent)
	{
		IO.displayln(indent + indent.length() + " " + id);
		if (list != null)
			list.printParameters(indent);
	}
	
	void printParseTree(String indent)
	{
		String indent1 = indent + " ";
		
		IO.displayln(indent + indent.length() + " <parameter list>");
		printParameters(indent1);
	}
}
