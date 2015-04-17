import java.util.*;  

import javax.jdo.*;

import com.objectdb.Utilities;

@javax.jdo.annotations.PersistenceCapable

public class Flight
{
	String airlineCompanyName;
	String flightNum; // { airlineCompanyName, flightNum } is a key
	Airport origin; 
	Airport destination;
	Time departTime;
	Time arriveTime;


	public String toString()
	{
		return airlineCompanyName+" "+flightNum+" "+
		       origin.name+" "+departTime.hour+":"+departTime.minute+" "+
		       destination.name+" "+arriveTime.hour+":"+arriveTime.minute ;
	}

	public static Flight find(String airlineCompanyName, String flightNum,
                                  PersistenceManager pm)

	/* Returns the flight that has the two parameter values;
	   returns null if no such flight exists.
           { airlineCompanyName, flightNum } is assumed to be a key for Flight class.
	   The function is applied to the database held by the persistence manager "pm". */

	{

		Query q = pm.newQuery();
		q.setClass(Flight.class);
		q.declareParameters("String airlineCompanyName, String flightNum");
		q.setFilter("this.flightNum == flightNum && this.airlineCompanyName == airlineCompanyName");
		q.setUnique(true);
		Object result = q.execute(airlineCompanyName,flightNum);
		q.closeAll();				
		return (Flight)result;

	}

	public static Collection<Flight> getFlights(String a1, String a2, Query q)
	
	/* Given airport names a1 and a2, the function returns the collection of
	   all flights departing from a1 and arriving to a2. */

	{
			q.setClass(Flight.class);
			q.declareParameters("String a1, String a2");
			q.setFilter("origin.name== a1 && destination.name== a2");
			@SuppressWarnings("unchecked")
			Collection<Flight> result = (Collection<Flight>) q.execute(a1,a2);
			return result;

	}

	public static Collection<Flight> getFlightsForCities(String c1, String c2, Query q)

	/* Given city names c1 and c2, the function returns the collection of
	   all flights departing from an airport close to c1 and arriving to 
	   an airport close to c2. */

	{
		q.setClass(City.class);
		q.declareParameters("String cname");		
		q.setFilter("name==cname");
		q.setUnique(true);		
		
		City city1 = (City)q.execute(c1);
		City city2 = (City)q.execute(c2);
		
		if (city1 == null || city2 == null)
			return null;
		
		q.setClass(Flight.class);
		q.setUnique(false);
		q.declareParameters("City c1, City c2");		
								
		q.setFilter("origin.closeTo.contains(c1) && destination.closeTo.contains(c2)");
		@SuppressWarnings("unchecked")
		Collection <Flight> result= (Collection<Flight>) q.execute(city1, city2);
				
		return result;	
		

	}

	public static Collection<Flight> getFlightsDepartTime(
		  String a1, String a2, int h1, int m1, int h2, int m2, Query q)

	/* Given airport names a1 and a2 and times h1:m1 and h2:m2, the function returns
	   the collection of all flights departing from a1 and arriving to a2,
	   with the requirement that the departure time is h1:m1 at earliest and
	   h2:m2 at latest. Note that the time range from h1:m1 to h2:m2 may include midnight. */

	{
		q.setClass(Flight.class);				
		q.declareParameters("String a1, String a2, int h1, int m1, int h2, int m2");
		q.setFilter("origin.name==a1 && departTime.isInInterval(h1,m1,h2,m2)");
		
		 
				
		Object[] elements = new Object[] {a1, a2, h1, m1, h2, m2};
		@SuppressWarnings("unchecked")
		Collection<Flight> result = (Collection<Flight>) q.executeWithArray(elements);
		HashSet<Flight> filtered_result = new HashSet<Flight> ();
		
		for (Flight x : result)
		{
			if (x.destination.name.equals (a2))
				filtered_result.add (x);
		}
		
		return filtered_result;

	}


	public static Collection<Flight> getFlightsConnection(
		  String a1, String a2, int h1, int m1, int h2, int m2,
		  int connectionAtLeast, int connectionAtMost, Query q)

	/* Given airport names a1 and a2, times h1:m1 and h2:m2, and connection time
	   lower and upper bounds in minutes, connectionAtLeast and connectionAtMost,
	   the function returns the collection of all flights f satisfying
	   the following conditions:

	   1. f departs from a1 and arrives to a connecting airport "ca" different from a2; and
	   2. The departure time of f is h1:m1 at earliest and h2:m2 at latest; and
	   3. There is a second flight f1 from "ca" to a2; and
	   4. The connecting time, i.e. the time interval in minutes between 
	      the arrival time of f and the departure time of f1, is at least connectionAtLeast
	      and at most connectionAtMost. 

	   Note again that the relevant time intervals may include midnight. */

	{
		q.setClass(Flight.class);
		
		q.declareParameters("String a1, String a2, int h1, int m1, int h2, int m2");
		Object[] elements = new Object[] {a1, a2, h1, m1, h2, m2};
		q.setFilter("origin.name==a1 && departTime.isInInterval(h1,m1,h2,m2)");
		@SuppressWarnings("unchecked")
		Collection<Flight> result = (Collection<Flight>)q.executeWithArray(elements);
		HashSet<Flight> filtered_result = new HashSet<Flight> ();
						
		for (Flight f : result)
		{
			String ca = f.destination.name;
			int h = f.arriveTime.hour;
			int m = f.arriveTime.minute;
			int cm1 = ((m + connectionAtLeast) % 60);
			int ch1 = (h + ((m + connectionAtLeast) / 60)) % 24;
			int cm2 = ((m + connectionAtMost) % 60);
			int ch2 = (h + ((m + connectionAtMost) / 60)) % 24;
			
			if (!ca.equals (a2))
			{
				Collection<Flight> result_conn = getFlightsDepartTime (ca, a2, ch1, cm1, ch2, cm2, q);
				if (!result_conn.isEmpty ())
				{
					filtered_result.add (f);
				}
			}
		}

		return result;
	}

	public static void main(String argv[])
	{
		PersistenceManager pm = Utilities.getPersistenceManager( "flight.odb" );

		Flight f585 = Flight.find( "Delta", "585", pm );
		Flight f655 = Flight.find( "Delta", "655", pm );
		Flight f300A = Flight.find( "United", "300A", pm );
		Flight f351 = Flight.find( "American", "351", pm );
	
		System.out.println( f585 );
		System.out.println( f655 );
		System.out.println( f300A );
		System.out.println( f351 );
		System.out.println();

		System.out.println( f655.departTime.differenceFrom(f585.arriveTime) );
		System.out.println( f351.departTime.differenceFrom(f300A.arriveTime) );
		System.out.println( f585.arriveTime.differenceFrom(f655.departTime) );
		System.out.println( f300A.arriveTime.differenceFrom(f351.departTime) );
		System.out.println();

		System.out.println( f585.departTime.isInInterval(20,0,0,30) );
		System.out.println( f585.departTime.isInInterval(19,30,2,30) );
		System.out.println( f585.departTime.isInInterval(20,30,0,30) );
		System.out.println( f585.departTime.isInInterval(8,30,14,40) );
		System.out.println();

		Query q = pm.newQuery();
		//Collection<Flight> ff = Flight.getFlights( "New York LaGuardia", "Chicago O'Hare", q );
		Collection <Flight> ff = Flight.getFlights( "Los Angeles LAX", "New York JFK", q ); 
		Utility.printCollection( ff );
		q.closeAll();
		System.out.println();

		q = pm.newQuery();
		ff = Flight.getFlights( "St. Louis Lambert", "Los Angeles LAX", q );
		Utility.printCollection( ff );
		q.closeAll();
		System.out.println();

		q = pm.newQuery();
		ff = Flight.getFlightsForCities( "Riverside", "Flushing", q );
		Utility.printCollection( ff );
		q.closeAll();
		System.out.println();

		q = pm.newQuery();
		ff = Flight.getFlightsDepartTime( "Chicago O'Hare", "Washington Dulles", 9, 30, 11, 30, q );
		Utility.printCollection( ff );
		q.closeAll();
		System.out.println();

		q = pm.newQuery();
		ff = Flight.getFlightsDepartTime( "New York JFK", "Washington Dulles", 19, 30, 1, 30, q );
		Utility.printCollection( ff );
		q.closeAll();
		System.out.println();

		q = pm.newQuery();
		ff = Flight.getFlightsConnection( "New York JFK", "Los Angeles LAX", 19, 30, 21, 30, 83, 115, q );
		Utility.printCollection( ff );
		q.closeAll();
		System.out.println();

		q = pm.newQuery();
		ff = Flight.getFlightsConnection( "Chicago O'Hare", "Los Angeles LAX", 9, 45, 11, 0, 60, 90, q );
		Utility.printCollection( ff );
		q.closeAll();
		System.out.println();
		

		q = pm.newQuery();
		Collection<Airport> aa = Airport.airportsForCompany( "American", q );
		Utility.printCollection( aa );
		q.closeAll();
		System.out.println();

		q = pm.newQuery();
		aa = Airport.airportsForCompany( "Delta", q );
		Utility.printCollection( aa );
		q.closeAll();
		System.out.println();

		q = pm.newQuery();
		aa = Airport.airportsForCompany( "United", q );
		Utility.printCollection( aa );
		q.closeAll();

		if ( !pm.isClosed() )
        		pm.close();
	}
}
