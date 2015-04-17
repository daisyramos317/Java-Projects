import java.util.*;

/* 
Represents a time in hour:minute. For example, 8:52 is 8:52 AM and 15:4 is 3:04 PM.
*/

@javax.jdo.annotations.PersistenceCapable

public class Time
{

	int hour; // 0 -- 23
	int minute; // 0 -- 59


	public int differenceFrom(Time earlierTime)

	/* Returns the time difference in minutes between "earlierTime"
           and the target time object, with "earlierTime" always regarded as 
	   the earlier time. Note that the time interval may include midnight.
           For example, suppose the target object represents 1:30. Then:

           if earlierTime = 23:30, the function will return 120 (minutes);
           if earlierTime = 0:30, the function will return 60 (minutes);
	   if earlierTime = 9:30, the function will return 960 (minutes), etc. */

	{
		int value1 = earlierTime.hour * 60 + earlierTime.minute; // the minute of the day for the earlier time
		int value2 = hour * 60 + minute; // the minute of the day for the current time
		int difference;
		
		if (value1 <= value2)
			difference = value2 - value1; // the times are in the same day
		else
			difference = (24 * 60 - value1) + value2; // different days
				
		return difference;
		
		//return 10;
	}

	public boolean isInInterval(int h1, int m1, int h2, int m2)

	/* Checks to see if the target time object is in the time interval
	   from h1:m1 to h2:m2, inclusive, with h1:m1 always regarded as 
	   earlier than h2:m2. Note that the interval may include midnight. */
	{

		int value1 = (h1 * 60 + m1);
		int value2 = (h2 * 60 + m2);
		int value = this.hour * 60 + this.minute;
		boolean result;

		if (value1 <= value2)
			result = ((value1 <= value) && (value <= value2)); // same day
		else
			result = ((value1 <= value) || (value <= value2)); // different days
				
		return result;

		
	}	
		
	}
