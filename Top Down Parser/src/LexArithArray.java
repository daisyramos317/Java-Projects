/**
 
This class is a lexical analyzer for the 15 token categories <id> through <RParen> defined by: 

<letter> --> a | b | ... | z | A | B | ... | Z
<digit> --> 0 | 1 | ... | 9
<basic id> --> <letter> { <letter> | <digit> }
<letters and digits> --> { <letter> | <digit> }+
<id> --> <basic id> { "_" <letters and digits> }
<int> --> [+|-] {<digit>}+
<float> --> [+|-] ( {<digit>}+ "." {<digit>} | "." {<digit>}+ )
<floatE> --> (<float> | <int>) (e|E) [+|-] {<digit>}+
<add> --> +
<sub> --> -
<mul> --> *
<div> --> /
<lt> --> "<"
<le> --> "<="
<gt> --> ">"
<ge> --> ">="
<eq> --> "="
<LParen> --> "("
<RParen> --> ")"

This class implements a DFA that will accept the above tokens.
The DFA states are represented by the Enum type "State".
The DFA has 15 final and 5 non-final states represented by enum-type literals.

There are also seven special states for the keywords
"def", "if", "then", "else", "and", "or", "not", "pair", "first", "second", "nil".
The keywords are initially extracted as identifiers.
The keywordCheck() function checks if the extracted token is a keyword,
and if so, moves the DFA to the corresponding special state.

The function "driver" operates the DFA. 
The array "nextState" returns the next state given the current state and the input character.

To recognize a different token set, modify the following:

  enum type "State" and function "isFinal"
  size of array "nextState"
  functions "setNextState", "keywordCheck"

The functions "driver", "getToken", "setLex" remain the same.

**/


import java.util.*;

public abstract class LexArithArray extends IO
{
	public enum State 
       	{ 
	  // final states     ordinal number  token accepted 

		Add,             // 0          +
		Sub,             // 1          -
		Mul,             // 2          *
		Div,             // 3          /
		Lt,              // 4          <
		Le,              // 5          <=
		Gt,              // 6          >
		Ge,              // 7          >=
		Eq,              // 8          =
		Id,              // 9          identifiers
		Int,             // 10         integers
		Float,           // 11         floats without exponentiation part
		FloatE,          // 12         floats with exponentiation part
		LParen,          // 13         (
		RParen,          // 14         )

	  // non-final states                 string recognized    

		Start,           // 15         the empty string
		Period,          // 16         ".", "+.", "-."
		E,               // 17         float parts ending with E or e
		EPlusMinus,      // 18         float parts ending with + or - in exponentiation part
		Underscore,      // 19         identifiers followed by "_"

	  // keyword states

		Keyword_def,
		Keyword_if,
		Keyword_then,
		Keyword_else,
		Keyword_and,
		Keyword_or,
		Keyword_not,
		Keyword_pair,
		Keyword_first,
		Keyword_second,
		Keyword_nil,

		UNDEF;

		private boolean isFinal()
		{
			return ( this.compareTo(State.RParen) <= 0 );  
		}	
	}

	// By enumerating the final states first and then the non-final states,
	// test for a final state can be done by testing if the state's ordinal number
	// is less than or equal to that of RParen.

	// The following variables are inherited from "IO" class:

	//   static int a; the current input character
	//   static char c; used to convert the variable "a" to the char type whenever necessary

	public static String t; // holds an extracted token
	public static State state; // the current state of the FA

	private static State nextState[][] = new State[20][128];
 
          // This array implements the state transition function State x (ASCII char set) --> State.
          // The state argument is converted to its ordinal number used as
          // the first array index from 0 through 19. 

	private static HashMap<String, State> keywordMap = new HashMap<String, State>();

	private static void setKeywordMap()
	{
		keywordMap.put("def",    State.Keyword_def);
		keywordMap.put("if",     State.Keyword_if);
		keywordMap.put("then",   State.Keyword_then);
		keywordMap.put("else",   State.Keyword_else);
		keywordMap.put("and",    State.Keyword_and);
		keywordMap.put("or",     State.Keyword_or);
		keywordMap.put("not",    State.Keyword_not);
		keywordMap.put("pair",   State.Keyword_pair);
		keywordMap.put("first",  State.Keyword_first);
		keywordMap.put("second", State.Keyword_second);
		keywordMap.put("nil",    State.Keyword_nil);
	}

	private static int driver()

	// This is the driver of the FA. 
	// If a valid token is found, assigns it to "t" and returns 1.
	// If an invalid token is found, assigns it to "t" and returns 0.
	// If end-of-stream is reached without finding any non-whitespace character, returns -1.

	{
		State nextSt; // the next state of the FA

		t = "";
		state = State.Start;

		if ( Character.isWhitespace((char) a) )
			a = getChar(); // get the next non-whitespace character
		if ( a == -1 ) // end-of-stream is reached
			return -1;

		while ( a != -1 ) // do the body if "a" is not end-of-stream
		{
			c = (char) a;
			nextSt = nextState[state.ordinal()][a];
			if ( nextSt == State.UNDEF ) // The FA will halt.
			{
				if ( state.isFinal() )
					return 1; // valid token extracted
				else // "c" is an unexpected character
				{
					t = t+c;
					a = getNextChar();
					return 0; // invalid token found
				}
			}
			else // The FA will go on.
			{
				state = nextSt;
				t = t+c;
				a = getNextChar();
			}
		}

		// end-of-stream is reached while a token is being extracted

		if ( state.isFinal() )
			return 1; // valid token extracted
		else
			return 0; // invalid token found
	} // end driver

	private static void setNextState()
	{
		for (int s = 0; s <= 19; s++ )
			for (int c = 0; c <= 127; c++ )
				nextState[s][c] = State.UNDEF;

		for (char c = 'A'; c <= 'Z'; c++)
		{
			nextState[State.Start     .ordinal()][c] = State.Id;
			nextState[State.Id        .ordinal()][c] = State.Id;
			nextState[State.Underscore.ordinal()][c] = State.Id;
		}

		for (char c = 'a'; c <= 'z'; c++)
		{
			nextState[State.Start     .ordinal()][c] = State.Id;
			nextState[State.Id        .ordinal()][c] = State.Id;
			nextState[State.Underscore.ordinal()][c] = State.Id;
		}

		for (char d = '0'; d <= '9'; d++)
		{
			nextState[State.Start     .ordinal()][d] = State.Int;
			nextState[State.Id        .ordinal()][d] = State.Id;
			nextState[State.Int       .ordinal()][d] = State.Int;
			nextState[State.Add       .ordinal()][d] = State.Int;
			nextState[State.Sub       .ordinal()][d] = State.Int;
			nextState[State.Period    .ordinal()][d] = State.Float;
			nextState[State.Float     .ordinal()][d] = State.Float;
			nextState[State.E         .ordinal()][d] = State.FloatE;
			nextState[State.EPlusMinus.ordinal()][d] = State.FloatE;
			nextState[State.FloatE    .ordinal()][d] = State.FloatE;
			nextState[State.Underscore.ordinal()][d] = State.Id;
		}

		nextState[State.Start.ordinal()]['+'] = State.Add;
		nextState[State.Start.ordinal()]['-'] = State.Sub;
		nextState[State.Start.ordinal()]['*'] = State.Mul;
		nextState[State.Start.ordinal()]['/'] = State.Div;
		nextState[State.Start.ordinal()]['<'] = State.Lt;
		nextState[State.Start.ordinal()]['>'] = State.Gt;
		nextState[State.Start.ordinal()]['='] = State.Eq;
		nextState[State.Start.ordinal()]['('] = State.LParen;
		nextState[State.Start.ordinal()][')'] = State.RParen;
		nextState[State.Start.ordinal()]['.'] = State.Period;

		nextState[State.Lt.ordinal()]['='] = State.Le;
		nextState[State.Gt.ordinal()]['='] = State.Ge;
		
		nextState[State.Add.ordinal()]['.'] = State.Period;
		nextState[State.Sub.ordinal()]['.'] = State.Period;
		nextState[State.Int.ordinal()]['.'] = State.Float;
			
		nextState[State.Float.ordinal()]['e'] = State.E;
		nextState[State.Float.ordinal()]['E'] = State.E;
		nextState[State.Int  .ordinal()]['e'] = State.E;
		nextState[State.Int  .ordinal()]['E'] = State.E;
		
		nextState[State.E.ordinal()]['+'] = State.EPlusMinus;
		nextState[State.E.ordinal()]['-'] = State.EPlusMinus;
		
		nextState[State.Id.ordinal()]['_'] = State.Underscore;

	} // end setNextState

	private static void keywordCheck()
	{
		State keywordState = keywordMap.get(t);
		if ( keywordState != null )
			state = keywordState;
	}

	public static void getToken()

	// Extract the next token using the driver of the FA.
	// If an invalid token is found, issue an error message.

	{
		int i = driver();
		if ( state == State.Id )
			keywordCheck();
		else if ( i == 0 )
			displayln(t + " : Lexical Error, invalid token");
	}

	public static void setLex()

	// Sets the nextState array and keywordMap.

	{
		setNextState();
		setKeywordMap();
	}

	public static void main(String argv[])

	{
		// argv[0]: input file containing source code using tokens defined above
		// argv[1]: output file displaying a list of the tokens 

		setIO( argv[0], argv[1] );
		setLex();

		int i;

		while ( a != -1 ) // while "a" is not end-of-stream
		{
			i = driver(); // extract the next token
			if ( i == 1 )
			{
				if ( state == State.Id )
					keywordCheck();
				displayln( t+"   : "+state.toString() );
			}
			else if ( i == 0 )
				displayln( t+" : Lexical Error, invalid token");
		} 

		closeIO();
	}
} 

