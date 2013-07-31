/**
 * @(#)Dice.java
 *
 *
 * @author
 * @version 1.00 2007/8/15
 */

import java.util.HashMap;
import java.lang.Double;
import java.util.Random;

public class Dice
{
    public static int rollDice( int die )
    {
    	Random random = new Random( );
    	return random.nextInt(die) + 1;
    }
    public static int rollDice( int amount, int die )
    {
    	Random random = new Random( );
    	int result = 0;
    	while ( amount > 0)
    	{
    		amount--;
    		result += random.nextInt(die) + 1;
    	}
    	return result;
    }
    public static int rollDice( int amount, int die, int mod )
    {
    	Random random = new Random( );
    	int result = 0;
    	while ( amount > 0)
    	{
    		amount--;
    		result += random.nextInt(die) + 1;
    	}
    	return result + mod;
    }
    public static int rollAce( int die )
    {
    	Random random = new Random();
    	if (die == 1 )
    		return 1;
    	int result = 0;
    	result = random.nextInt(die) + 1;
    	if ( result == die )
    		return rollAce (die, result );
    	else
    		return result;
    }
    public static int rollAce(int die, int mod)
    {
    	Random random = new Random();
    	if (die == 1 )
    		return 1;
    	int result = 0;
    	result = random.nextInt(die) + 1;
    	if ( result == die )
    		return rollAce (die, result + mod );
    	else
    		return result + mod;
    }
    public static String statRun( int die, int rolls )
    {
    	Random random = new Random( );
    	int mode=0;
    	int median=0;
    	double mean=0;
    	int j=0;
    	int biggest = 0;
    	int roll;
    	int[] results = new int[die+1];
    	for ( int i = die; i >=0; i--  )
    	{
    		results[i]=0;
    	}
    	if ( rolls > 10000000 )
    	{
    		return "Let's keep the number of rolls under ten million.";
    	}
    	for ( int i = rolls; i > 0; i-- )
    	{
    		results[random.nextInt(die)+1]++;
    	}
    	for ( int i = die; i > 0; i--)
    	{
    		j += i*results[i];
    	}
    	double d1 = j;
    	double d2 = rolls;
    	mean = d1/d2;
		int k = 1;
		j = rolls/2;
		while ( j > 0 )
		{
			j -= results[k];
			k++;
		}
		median = k-1;
		for ( int i = die; i > 0; i--)
		{
			if ( results[i] > biggest )
			{
				biggest = results[i];
				mode = i;
			}
		}
		return "Number of rolls:  " + rolls + "  Mean:  " + mean + "  Median:  " + median + "  Mode:  " + mode;
    }
    public static String westDice( int dice, int mod, String roller )
    {
    	//dice roller for west end games type rolls
    	//rolls a specified number of d6s + one wild die
    	//the wild die can explode if it's 1 then bad stuff happens

    	//first step is to filter out negative dice
    	if ( dice < 1 )
    		return "shrugs, no way it can roll a number of dice less then 1.";
    	//now the preliminary stuff
    	int total = 0;
    	int roll = 0;
    	String output = "rolls " + dice + " dice ";
    	if ( mod != 0 )
    	{
    		if ( mod > 0 )
    		{
    			output += "with a modifier of +" + mod + " ";
    		}
    		else
    		{
    			output += "with a modifier of " + mod + " ";
    		}
    	}
    	output += "for " + roller + " and got: (";
    	//now to tick off the dice rolls and add them up
    	dice--;
    	while ( dice > 0 )
    	{
    		roll = rollDice(6);
    		output += " " + roll;
    		total += roll;
    		dice--;
    	}
    	//now for that wild die
		roll = rollAce(6);
		total += roll + mod;
		if ( roll == 1 )
			output += "4 ";
		else
			output += "9 ";
		output += roll + " ) for a total of: " + total;
		if ( roll == 1 )
			output += " 4BUST!";
		return output;
    }
    public static String heroAttack( int ocv, String roller )
    {
    	int result = ocv+11-rollDice(6)-rollDice(6)-rollDice(6);
    	return "rolls an attack for " + roller + " with an OCV of " + ocv + " for a result of: " + result;
    }
    public static String heroDamage( double dc, int mod, String roller )
    {
    	//rolls damage for the HERO system, input allows for half dice since
    	//that's the way HERO rolls
    	int dice=0;
    	boolean hDie=false;
    	int stun = 0;
    	int body = 0;
    	int roll = 0;
    	double dc10=dc*10;
    	int dieCheck = (int)dc10;
		if ( dieCheck%10 == 5 )
		{
			hDie=true;
		}
    	if ( dc < 1 && !hDie )
    		return "needs a number of dice greater then 0 to roll";
    	dice = (int)dc;
		String output = "rolled " + dice;
		if ( hDie )
			output += " and a half";
		if (mod != 0)
			output+= " with a mod of " + mod;
		output += " dice for " + roller + " and got(";
		if ( hDie )
		{
			roll = rollDice(6);
			stun+= (roll+1)/2;
			if ( roll < 3 )
				body++;
			output += "10 " + roll + "";
		}
		while ( dice > 0 )
		{
			roll = rollDice(6);
			stun+=roll;
			body+=getBody(roll);
			output+= " " + roll;
			dice--;
		}
		stun+=mod;
		int knockBack=(body-rollDice(6)-rollDice(6))*2;
		output+= " ) for " + stun + " STUN and " + body + " BODY";
		if (knockBack==0)
			output+=" and knockdown.";
		else if (knockBack>0)
			output+=" and " + knockBack + " meters of knockback.";
		return output;
    }
    public static int getBody( int roll )
    {
    	if ( roll == 1 )
    		return 0;
    	else if ( roll == 6 )
    		return 2;
    	else
    		return 1;
    }
    public static String exaltRoller( String message, String sender )
    //shadowrun dice roller
    {
        String diceChain = "";
	double fate;
	int hits = 0;
	int die;
	int rolls = 0;
	int ones = 0;
	boolean isDice = true;
	int pool;
	String results = "";
	String[] args = message.split(" ");
	if ( args.length == 1 )
	{
            return "Useage: !exalted X Where X is the number of dice in the pool.";
	}
	else
	{
            isDice = true;
            try
        {
            pool = Integer.parseInt ( args[1] );
   	}
   	catch(Exception e)
	{
            return "Useage: !exalted X Where X is the number of dice in the pool.";
	}
	results = results.concat( "rolled " + pool + " dice " );
   	results = results.concat( "for " + sender + " and got (" );
        while( pool > 0 )
   	{
                die = Dice.rollDice( 10 );
                if ( die == 1 )
   		{
                    diceChain = diceChain.concat( "4" );
                    ones++;
                }
                diceChain = diceChain.concat( " " + die + "");
                rolls++;
                if ( die == 10 )
                {
                    hits+=2;
                }
                else if (die >= 7)
                {
                    hits++;
                }
                pool--;
            }
            results = results.concat( diceChain + " )  Hits: " + hits );

            if (isDice)
            {
                return results;
            }
            else
            {
                return "Useage: !sr X [edge] Where x is the number of die in pool, and edge applies the rule of sixes.";
            }
	}
    }
}