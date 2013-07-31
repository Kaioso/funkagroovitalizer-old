/**
 * @(#)ExpTot.java
 *
 *
 * @author 
 * @version 1.00 2008/12/18
 */
import java.lang.Integer;
import java.lang.Double;

public class ExpTot 
{
	private final static int THREEE = 0;
	private final static int FOURE = 1;
	private final static int SR = 2;
	private final static int SW = 3;
	private int experience;
	private int game;
	private double money;
	private String name;
	
    public ExpTot() 
    {
    	experience = 0;
    	game = 0;
    	money = 0;
    	name = null;
    }
    public ExpTot(String n, int g )
    {
    	game = g;
    	name = n;
    	experience = 0;
    	money = 0;
    }
    public ExpTot(String n, int g, double m, int i)
    {
    	game = g;
    	name = n;
    	money = m;
    	experience = i;
    }
    public String getName()
    {
    	return name;
    }
    public double deposit(double m)
    {
    	money +=m;
    	return money;
    }
    public double deduct(double m)
    {
    	money -=m;
    	return money;
    }
    public double getBalance()
    {
    	return money;
    }
    public int getExp()
    {
    	return experience;
    }
    public int advance(int e)
    {
    	experience +=e;
    	return experience;
    }
    public int getLevel()
    {
    	int e = experience;
    	if ( game == THREEE )
    	{
    		int i = 1;
    		boolean monkey = true;
    		while ( monkey )
    		{
    			if ( e < i*1000 )
    			{
    				return i;
    			}
    			else 
    			{
    				e -= i*1000;
    				i++;
    			}
    		}
    	}
    	else if ( game == FOURE )
    	{
    		if ( experience >= 1000000 )
    			return 30;
    		int i = 1;
    		int t = 1000;
    		while (true)
    		{
    			if ( e < t )
    				return i;
    			else
    			{
					if ( i < 5 )
					{
						t += 1000+250*i;
					}
					else if ( i < 9 )
					{
						t += 2000+500*(i-4);
					}
					else if ( i == 9 )
					{
						t += 5500;
					}
					else if (i < 13 )
					{
						t += 6000+1000*(i-10);
					}
					else if ( i < 17 )
					{
						t += 10000+2000*(i-13);
					}
					else if ( i < 19 )
					{
						t += 20000+4000*(i-17);
					}
					else if ( i == 19 )
					{
						t += 32000;
					}
					else if ( i < 25 )
					{
						t += 35000+10000*(i-20);	
					}
					else if ( i < 30 )
					{
						t += 100000+25000*(i-25);
					}
					if (i >= 30)
						return 30;
					i++;
    			}
    		}
	 	}
	 	else if ( game == SR )
	 	{
	 		if ( experience >=  250 )
	 			return 6;
	 		else if ( experience >= 160 )
	 			return 5;
	 		else if (experience >= 100 )
	 			return 4;
	 		else if (experience >= 60 )
	 			return 3;
	 		else if (experience >= 20 )
	 			return 2;
	 		else
	 			return 1;
	 	}
	 	else if ( game == SW )
	 	{
	 		if ( experience >= 80 )
	 			return 17 + (experience-80)/10;
	 		else
	 			return (experience/5)+1;
	 	}
	 	return 0;
    }
    public String toRaw()
    {
    	return name + " " + game + " " + experience + " " + money;
    }
    public static ExpTot parseRaw(String input)
    {
    	String[] args = input.split(" ");
    	String n = args[0];
    	int x = 0;
    	double m = 0;
    	int g = THREEE;
    	if ( args.length > 1 )
    	{
    		try
    		{
    			g = Integer.parseInt( args[1] );
    		}
    		catch (Exception e)
    		{
    			g = 0;
    		}
    	}
    	if ( args.length > 2 )
    	{
    		try
    		{
    			x = Integer.parseInt( args[2] );
    		}
    		catch (Exception e)
    		{
    			x = 0;
    		}
    	}
    	if ( args.length > 3 )
    	{
    		try
    		{
    			m = Double.parseDouble( args[3] );
    		}
    		catch (Exception e)
    		{
    			m = 0;
    		}
    	}
    	return new ExpTot(n, g, m, x);
    }
    public String report()
    {
    	String output = name + " has " + experience + " ";
    	if ( game != SR )
    	{
    		output = output + "experience and is level " + getLevel() + ".  Experience until next level:  " + tnl() + " ";
    	}
    	else
    	{
    		output = output + "karma and has a table rating of ";
    		if ( getLevel() == 1 )
    			output = output + "green.  ";
    		else if (getLevel() == 2)
    			output = output + "streetwise.  ";
    		else if (getLevel() == 3 )
    			output = output + "professional.  ";
    		else if (getLevel() == 4 )
    			output = output + "veteran.  ";
    		else if (getLevel() == 5 )
    			output = output + "elite.  ";
    		else if (getLevel() == 6 )
    			output = output + "prime.  ";
    	}
    	output = output + "Total funds available:  " + money;
    	if (game == THREEE || game == FOURE )
    		output = output + " gold pieces.";
    	else if (game == SR)
    		output = output + " nuyen.";
    	return output;
    }
    public int getGame()
    {
    	return game;
    }
    public int nextLevel()
    {
    	int level = getLevel();
    	if( game == FOURE )
    	{
	    	int t=1000;
			if (level >= 30 )
				return 1000000;
			for ( int i = 1; i < level; i++)
			{
				if ( i < 5 )
				{
					t += 1000+250*i;
				}
				else if ( i < 9 )
				{
					t += 2000+500*(i-4);
				}
				else if ( i == 9 )
				{
					t += 5500;
				}
				else if (i < 13 )
				{
					t += 6000+1000*(i-10);
				}
				else if ( i < 17 )
				{
					t += 10000+2000*(i-13);
				}
				else if ( i < 19 )
				{
					t += 20000+4000*(i-17);
				}
				else if ( i == 19 )
				{
					t += 32000;
				}
				else if ( i < 25 )
				{
					t += 35000+10000*(i-20);	
				}
				else if ( i < 30 )
				{
					t += 100000+25000*(i-25);
				}
				if (i >= 30)
					return 1000000;
			}
			return t;
    	}
    	else if ( game == THREEE )
    	{
    		int t = 0;
    		for (int i = 1; i <= level; i++)
    		{
				t+=i*1000;
    		}
    		return t;
    	}
    	else if ( game == SW )
    	{
			if (experience < 80)
			{
				return getLevel()*5;
			}
			else
			{
				return (getLevel()-17)*10+80;
			}
    	}
    	else
    	{
    		return experience;
    	}
    }
    public int tnl()
    {
    	int level = getLevel();
    	int next = nextLevel();
    	return next-experience;
    }
}