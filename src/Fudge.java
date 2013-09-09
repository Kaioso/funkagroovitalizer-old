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

public class Fudge 
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
			return "Fair";
		else if ( pie == 2 )
			return "Good";
		else if ( pie == 3 )
			return "Great";
		else if ( pie == 4 )
			return "Superb";
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
		else if ( pie.equalsIgnoreCase("Fair") )
			return 1;
		else if ( pie.equalsIgnoreCase("Good") )
			return 2;
		else if ( pie.equalsIgnoreCase("Great") )
			return 3;
		else if ( pie.equalsIgnoreCase("Superb") )
			return 4;
		else if ( pie.equalsIgnoreCase("Legendary") )
			return 5;
		else
			return 0;
	}
	public static String roll( String roller, String mod )
	//fudge dice roller
   	{
        int trueMod;
        String textMod;
        try
        {
            trueMod = Integer.parseInt(mod);
            textMod = intToString(trueMod);
        }
        catch (Exception e)
        {
            trueMod = stringToInt(mod);
            textMod = intToString(trueMod);
        }

        int[] dice = {Dice.rollDice(3) - 1, Dice.rollDice(3) - 1, Dice.rollDice(3) - 1, Dice.rollDice(3) - 1};
        char[] symbols = {'-', '0', '+'};
        int[] values = {-1, 0, 1};

        int result = trueMod;
        for (int roll : dice) result += values[roll];
        return String.format("rolled a %s (%s) for %s and got ( %s %s %s %s ) for a total of: %s (%s)",
                textMod, trueMod, roller,
                symbols[dice[0]], symbols[dice[1]], symbols[dice[2]], symbols[dice[3]],
                intToString(result), result);
   	}
}
