import java.util.*;  
import javax.jdo.*;

@javax.jdo.annotations.PersistenceCapable

public class City
{
	String name;

	HashSet<Airport> closeTo = new HashSet<Airport>(); 

	 // The set of airports close enough to this city
	 // so its residents can use them; 
	 // Represents the inverse of Airport.closeTo
}