/**
 * CSCI 316 Project2 : Top Down Parser

 * @author Daisy Ramos
 * 
 * This class is a top-down, recursive-descent parser.
 * 
 * The definitions of the tokens are given in the lexical analyzer class file
 * "LexArithArray.java".
 * 
 * The following variables/functions of "IO"/"LexArithArray" classes are used:
 * 
 * static void display(String s) 
 * static void displayln(String s)
 * static void setIO(String inFile, String outFile) 
 * static void closeIO()
 * 
 * static void setLex() 
 * static String t // holds an extracted token 
 * static State state // the current state of the finite automaton
 *  static int getToken() //extracts the next token
 * 
 * An explicit parse tree is constructed in the form of nested class objects.
 * 
 * The main function will display the parse tree in linearly indented form. Each
 * syntactic category name labeling a node is displayed on a separate line,
 * prefixed with the integer i representing the node's depth and indented by i
 * blanks.
 **/

public abstract class Parser extends LexArithArray {
	static boolean errorFound = false;

	// <parameter list>   =>   <> | <id> <parameter list>
	static ParameterList readParamList ()
	{
		if (state == State.Eq)
			return null; // empty parameter list/empty string
		
		if (state == State.Id)
		{
			String param = t;
			getToken (); // Skip the first parameter
			ParameterList list = readParamList (); // can be null if there is only one parameter
			
			if (errorFound)
				return null;
			
			return new ParameterList (param, list);
		}
		
		errorMsg ("[id] or \"=\" expected");
		return null;
	}

	
	// <header>   =>   <fun name> <parameter list>
	static FunHeader readFunHeader ()
	{
		if (state != State.Id)
		{
			errorMsg ("[id] expected");
			return null;
		}
		
		String function_name = t;
		getToken (); // Skip the function name		
		
		ParameterList param_list = readParamList (); // can be null
		
		if (errorFound)
			return null;
		
		return new FunHeader (function_name, param_list);
	}
	
	// <fun op>   =>   <id> | "pair" | "first" | "second" |  <arith op> | <bool op> | <comp op>
	
	static FunOp readFunOp ()
	{
		switch (state)
		{
			case Id:
			{
				String id = t;
				getToken (); // Skip id
				return new FunOp_Id (id);
			}
			
			// Keywords which can be function operators
			case Keyword_pair:
				getToken (); // Skip "pair"
				return new FunOp_pair ();
			case Keyword_first:
				getToken (); // Skip "first"
				return new FunOp_first ();
			case Keyword_second:
				getToken (); // Skip "second"
				return new FunOp_second ();
			
			// Arithmetic operators
			case Add:
				getToken (); // Skip the keyword
				return new Arith_Plus ();
			case Sub:
				getToken (); // Skip the keyword
				return new Arith_Minus ();
			case Mul:
				getToken (); // Skip the keyword
				return new Arith_Mul ();
			case Div:
				getToken (); // Skip the keyword
				return new Arith_Div ();
			
			// Boolean operators
			case Keyword_and:
				getToken (); // Skip the keyword
				return new Bool_And ();
			case Keyword_or:
				getToken (); // Skip the keyword
				return new Bool_Or ();
			case Keyword_not:
				getToken (); // Skip the keyword
				return new Bool_Not ();
				
			// Comparison operators
			case Lt:
				getToken (); // Skip the keyword
				return new Comp_Lt ();
			case Le:
				getToken (); // Skip the keyword
				return new Comp_Le ();
			case Gt:
				getToken (); // Skip the keyword
				return new Comp_Gt ();
			case Ge:
				getToken (); // Skip the keyword
				return new Comp_Ge ();
			case Eq:
				getToken (); // Skip the keyword
				return new Comp_Eq ();
			default:
				// Nothing found, generate error message
				errorMsg ("<fun op> expected");		
		}
		
		return null;
	}

	// <exp list>   =>   <> | <exp> <exp list>
	static ExpList readExpList ()
	{
		if (state == State.RParen) 
			return null;
		
		Exp e = readExp (); 
		
		if (e == null || errorFound)
		{
			errorMsg ("<exp> expected");
			return null;
		}
		
		ExpList list = readExpList (); 
		
		if (errorFound)
			return null;
		
		return new ExpList (e, list);
	}
	
	// <fun exp>   =>   <fun op> <exp list>

	static FunExp readFunExp ()
	{
		FunOp op = readFunOp (); // cannot be null
		
		if (op == null || errorFound)
		{
			errorMsg ("<fun exp> expected");
			return null;
		}
		
		ExpList exp_list = readExpList (); // can be null
		
		if (errorFound)
			return null;

		return new FunExp (op, exp_list);
	}
	
	// <exp>  =>   <id> | <int> | <float> | <floatE> | "nil" | "(" <fun exp> ")" | "if" <exp> "then" <exp> "else" <exp>
	
	static Exp readExp ()
	{
		switch (state)
		{
			case Id:
			{
				String id = t;
				getToken (); // Skip id
				return new Exp_Id (id);
			}
			case Int:
			{
				int value = Integer.parseInt(t);
				getToken (); // Skip the value
				return new Exp_Int (value);
			}
			case Float:
			case FloatE:
			{
				float value = Float.parseFloat(t);
				getToken (); // Skip value
				return new Exp_Float (value);
			}
			case Keyword_nil:
			{
				getToken (); // Skip "nil"
				return new Exp_Nil ();
			}
			case LParen: // <fun exp>
			{
				getToken (); // Skip "("
				FunExp fun_exp = readFunExp ();
				
				if (fun_exp == null || errorFound)
				{
					errorMsg ("<fun exp> expected");
					return null;
				}
				if (state != State.RParen)
				{
					errorMsg ("\")\" expected");
					return null;
				}
				getToken (); // Skip ")"
				return fun_exp;
			}
			
			case Keyword_if:
			{
				getToken (); // Skip "if"
				Exp e1 = readExp ();
				
				if (state != State.Keyword_then)
				{
					errorMsg("\"then\" expected");
					return null;
				}
				
				getToken (); // Skip "then"
				Exp e2 = readExp ();
				
				if (state != State.Keyword_else)
				{
					errorMsg("\"else\" expected");
					return null;
				}
				
				getToken (); // Skip "else"
				Exp e3 = readExp ();
				
				if (errorFound)
					return null;
				
				return new Exp_If (e1, e2, e3);
			}
			default:
		}
		
		errorMsg ("<exp> expected");
		return null;
	}
	
	// <fun def>   =>   "def" <header> = <exp>

	static FunDef readFunDef()
	{
		if (t.isEmpty ()) // when at the end of the file
			return null;
		
		if (state != State.Keyword_def)
		{
			errorMsg ("<def> expected");
			return null;
		}
		
		getToken (); // Skip "def"
		FunHeader header = readFunHeader (); // The header cannot be null
		
		if (state != State.Eq || errorFound)
		{
			errorMsg ("= expected");
			return null;
		}
		
		getToken (); // Skip "="		
		Exp exp = readExp (); // The expression cannot be  null
		
		if (exp == null || errorFound)
		{
			errorMsg ("<exp> expected");
			return null;
		}
		
		return new FunDef (header, exp);
	}

	// <fun def list>   =>   <fun def> | <fun def> <fun def list>
	
	static FunDefList readFunDefList ()
	{
		FunDef fd = readFunDef (); // can be null when we are at the end of the file 
		
		if (fd == null || errorFound)
			return null;
		
		if (t.isEmpty ())
			return new FunDefList (fd);
		
		FunDefList list = readFunDefList (); // can be null
		
		if (errorFound)
			return null;

		return new FunDefList (fd, list);
	}
	
	public static void errorMsg(String msg) 
	{
		if (!errorFound) // print only the first error, ignore the rest
		{
			errorFound = true;
			displayln(t + " : Syntax Error, Unexpected symbol " + msg);
		}
	}
	
	void print_tokens ()
	{
		for (; a != -1;)
		{
			getToken();			
			LexArithArray.displayln (t);
		}
	}
			
	public static void main(String argv[])
	{
		// argv[0]: input file containing an assignment list
		// argv[1]: output file displaying the parse tree
		try {
			   String current = new java.io.File (".").getCanonicalPath();
			   System.out.println("Current dir:"+current);//displays current directory
			  } catch (Exception e)
			  {
			  }
		
		if (argv.length != 2)
		  {
		   System.out.println ("Please check the program arguments, they should be: <input_file> <output_file>");
		   return;
		  }

		  java.io.File input = new java.io.File (argv [0]);
		  if (!input.exists ())
		  {
		   System.out.println ("Input file not found (please check the running directory): " + argv [0]);
		   return;
		  }
		
		setIO(argv[0], argv[1]);
		setLex();
		
		getToken ();

		FunDefList funList = readFunDefList ();
		
		if (funList == null || !t.isEmpty() || errorFound)
			errorMsg ("<def> expected");
		else
			funList.printParseTree(""); // print the parse tree in argv[1] file
		
		closeIO();
	}
}
