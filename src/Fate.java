/**
 * @(#)Fudge.java
 *
 *
 * @by Carl Celizic
 * @version 1.00 2010/11/17
 */

import java.lang.Double;
import java.util.Random;
import java.lang.Integer;

public class Fate
{
	public static String intToString(int pie)
	{
		if ( pie <= -2 )
			return "Terrible";
		else if ( pie == -1 )
			return "Poor";
		else if ( pie == 0 )
			return "Mediocre";
		else if ( pie == 1 )
			return "Average";
		else if ( pie == 2 )
			return "Fair";
		else if ( pie == 3 )
			return "Good";
		else if ( pie == 4 )
			return "Great";
		else if ( pie == 5 )
			return "Superb";
		else if ( pie == 6 )
			return "Fantastic";
		else if ( pie == 7 )
			return "Epic";
		else
			return "Legendary";

	}
	public static int stringToInt(String pie)
	{
		if ( pie.equalsIgnoreCase("Terrible") )
			return -2;
		else if ( pie.equalsIgnoreCase("Poor") )
			return -1;
		else if ( pie.equalsIgnoreCase("Mediocre") )
			return 0;
		else if ( pie.equalsIgnoreCase("Average") )
			return 1;
		else if ( pie.equalsIgnoreCase("Fair") )
			return 2;
		else if ( pie.equalsIgnoreCase("Good") )
			return 3;
		else if ( pie.equalsIgnoreCase("Great") )
			return 4;
		else if ( pie.equalsIgnoreCase("Superb") )
			return 5;
		else if ( pie.equalsIgnoreCase("Fantastic") )
			return 6;
		else if ( pie.equalsIgnoreCase("Epic") )
			return 7;
		else
			return 0;
	}
	public static String roll( String roller, String mod )
	//fudge dice roller
   	{
   		//fudge dice have 6 sides and only 3 results
   		// - blank and +.  In retrospect, I probaly
   		//should not have gone puritan with 6 sided
   		//virtual dice, and used d3's but what's done
   		//is done
   		int d1 = Dice.rollDice(6);
   		int d2 = Dice.rollDice(6);
   		int d3 = Dice.rollDice(6);
   		int d4 = Dice.rollDice(6);
   		//now my dice have values
   		int result = 0;
   		//at first you have nothing
   		//now to convert the modifier into a fudge mod if it isn't already.
   		//mediocre is default
   		String modifier = "Mediocre";
   		int intMod = 0;
   		try
   		{
   			intMod = Integer.parseInt( mod );
   			modifier = intToString(intMod);
   		}
   		catch(Exception e)
   		{
  			modifier = intToString(stringToInt(mod));
  			intMod = stringToInt(mod);
   		}
   		String message = "rolled a " + modifier + " (" + intMod + ") fudge roll for " + roller + " and got ( ";
   		//from this point on it just addresses the dice one
   		//step at a time, concacting on the result and adjusting
   		//the total.
   		if ( d1 == 1 || d1 == 2 )
   		{
   			message = message + "- ";
   			result--;
   		}
   		else if ( d1 == 3 || d1 == 4 )
   		{
   			message = message + "0 ";
   		}
   		else if ( d1 == 5 || d1 == 6 )
   		{
   			message = message + "+ ";
   			result++;
   		}
   		if ( d2 == 1 || d2 == 2 )
   		{
   			message = message + "- ";
   			result--;
   		}
   		else if ( d2 == 3 || d2 == 4 )
   		{
   			message = message + "0 ";
   		}
   		else if ( d2 == 5 || d2 == 6 )
   		{
   			message = message + "+ ";
   			result++;
   		}
   		if ( d3 == 1 || d3 == 2 )
   		{
   			message = message + "- ";
   			result--;
   		}
   		else if ( d3 == 3 || d3 == 4 )
   		{
   			message = message + "0 ";
   		}
   		else if ( d3 == 5 || d3 == 6 )
   		{
   			message = message + "+ ";
   			result++;
   		}
   		if ( d4 == 1 || d4 == 2 )
   		{
   			message = message + "- ";
   			result--;
   		}
   		else if ( d4 == 3 || d4 == 4 )
   		{
   			message = message + "0 ";
   		}
   		else if ( d4 == 5 || d4 == 6 )
   		{
   			message = message + "+ ";
   			result++;
   		}
   		result+=intMod;
   		message = message + ") For a total of: " + intToString(result) + " (" + result + ")";
   		return message;
   	}
}
