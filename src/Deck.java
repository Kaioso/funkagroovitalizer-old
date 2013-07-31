/**
 * @(#)Deck.java
 *
 *
 * @author 
 * @version 1.00 2007/8/17
 */
import java.util.*;

public class Deck 
{
	private boolean[] deck;
	private String name;
	private ArrayList<PlayerCharacter> players;

    public Deck() 
    {
    	int i;
    	deck = new boolean[54];
    	for (i = 0; i < deck.length; i++ )
    	{
    		deck[i] = true;
    	}
		players = new ArrayList<PlayerCharacter>();
		name = "";
    }
    public Deck( String aname )
    {
    	int i;
    	deck = new boolean[54];
    	for (i = 0; i < deck.length; i++ )
    	{
    		deck[i] = true;
    	}
		players = new ArrayList<PlayerCharacter>();
		name = aname;    	
    }
    public boolean hasPlayer( String handle )
    {
    	{
    		for (int i = 0; i < players.size(); i++)
    		{
    			PlayerCharacter p = players.get(i);
    			if (p.getName().equalsIgnoreCase(handle) )
    				return true;
    		}
    		return false;
    	}
    }
    public String collectCard(String handle, int card)
    {
    	PlayerCharacter p = new PlayerCharacter();
    	int pos = -1;
    	for (int i = 0; i < players.size(); i++)
    	{
    		p = players.get(i);
    		if ( p.getName().equalsIgnoreCase(handle) )
    		{
    			pos = i;
    			i = players.size();
    		}
    	}
    	if ( pos == -1 )
    		return "Player not found.";
    	else if ( !p.hasCard(card) )
    		return p.getName() + " does not have that card.";
    	else
    	{
    		p.removeCard(card);
    		players.set( pos, p);
 			return p.getName() + " turned in a " + cardName(card);
    	}
    }
    public String getName()
    {
    	return name;
    }
    public String deal(String handle)
    {
    	String report = "";
    	if ( this.deckSize() < 1 )
    	{
    		this.shuffle();
    		report = "shuffled and ";
    		if ( this.deckSize() < 1 )
    			return "has no more cards to deal, collect some cards from the players first.";
    	}
    	for ( int i = 0; i < players.size(); i++ )
    	{
    		PlayerCharacter p = players.get(i);
    		if ( p.getName().equalsIgnoreCase(handle) )
    		{
    			int draw;
    			boolean dealt = false;
    			while ( !dealt )
    			{
    				draw = Dice.rollDice(54)-1;
    				if ( deck[draw] )
    				{
    					deck[draw] = false;
    					p.addCard(draw);
    					dealt = true;
    					return report + "dealt " + p.getName() + " " + cardName(draw);
    				}
    			}
    		}
    	}
    	return "Player not found.";
    }
    public int deckSize()
    {
    	int inventory = 0;
    	for (int i = 0; i < deck.length; i++ )
    	{
    		if ( deck[i] )
    			inventory++;	
    	}
    	return inventory;
    }
    public int[] getHand( String handle )
    {
    	for (int i = 0; i < players.size(); i++ )
    	{
    		PlayerCharacter p = players.get(i);
    		if ( p.getName().equalsIgnoreCase(handle) )
    			return p.getHand();
    	}
    	int[] blah = { -1 };
    	return blah;
    }	
    public void shuffle()
    {
    	int i;
    	int j;
    	deck = new boolean[54];
    	int[] hand;
    	for (i = 0; i < deck.length; i++ )
    	{
    		deck[i] = true;
    	}
    	for (i = 0; i < players.size(); i++)
    	{
    		PlayerCharacter p = players.get(i);
    		hand = p.getHand();
    		for ( j = 0; j < hand.length; j++ )
    		{
    			deck[hand[j]] = false;
    		}
    	}
    }
    public void collect()
    {
		for (int i = 0; i < players.size(); i++)
    	{
    		PlayerCharacter p = players.get(i);
    		p.emptyHand();
    		players.set(i, p);
    	}
    }
    public void collect(String aname)
    {
    	for (int i = 0; i < players.size(); i++)
    	{
    		PlayerCharacter p = players.get(i);
    		if ( p.getName().equalsIgnoreCase(aname) )
    		{
    			p.emptyHand();
    			players.set(i, p);
    		}
    	}
    }
    public void addPlayer(String aname)
    {
    	PlayerCharacter player = new PlayerCharacter( aname );
    	for (int i = 0; i < players.size(); i++)
    	{
    		PlayerCharacter p = players.get(i);
    		if (p.getName().equalsIgnoreCase(aname))
    			return;
    	}
    	players.add( player );
    }
    public void removePlayer(String aname)
    {
    	for (int i = 0; i < players.size(); i++)
    	{
    		PlayerCharacter p = players.get(i);
    		if ( p.getName().equalsIgnoreCase(aname) )
    			players.remove(i);
    	}
    }
    public String[] dealAll()
    {
    	String[] report = new String[players.size()];
    	for (int i = 0; i < players.size(); i++)
    	{
    		PlayerCharacter p = players.get(i);
    		report[i] = this.deal(p.getName());
    	}
    	return report;
    }
    public String[] listPlayers()
    {
    	Collections.sort(players);
    	String[] report = new String[players.size()];
    	for (int i = players.size()-1; i > -1; i-- )
    	{
    		PlayerCharacter p = players.get(i);
    		int[] hand = p.getHand();
    		report[i] = p.getName() + " ";
    		for (int j = 0; j < hand.length; j++ )
    		{
    			report[i] = report[i] + cardName(hand[j]) + " ";
    		}
    	}
    	return report;
    }
    public int playerCount()
    {
    	return players.size();
    }
    public static String cardName(int card )
   	{
   		if ( card == 0 )
   			return "2C";
   		if ( card == 1 )
   			return "2D";
   		if ( card == 2 )
   			return "2H";
   		if ( card == 3 )
   			return "2S";
   		if ( card == 4 )
   			return "3C";
   		if ( card == 5 )
   			return "3D";
   		if ( card == 6 )
   			return "3H";
   		if ( card == 7 )
   			return "3S";
   		if ( card == 8 )
   			return "4C";
   		if ( card == 9 )
   			return "4D";
   		if ( card == 10 )
   			return "4H";
   		if ( card == 11 )
   			return "4S";
   		if ( card == 12 )
   			return "5C";
   		if ( card == 13 )
   			return "5D";
   		if ( card == 14 )
   			return "5H";
   		if ( card == 15 )
   			return "5S";
   		if ( card == 16 )
   			return "6C";
   		if ( card == 17 )
   			return "6D";
   		if ( card == 18 )
   			return "6H";
   		if ( card == 19 )
   			return "6S";
   		if ( card == 20 )
   			return "7C";
   		if ( card == 21 )
   			return "7D";
   		if ( card == 22 )
   			return "7H";
   		if ( card == 23 )
   			return "7S";
   		if ( card == 24 )
   			return "8C";
   		if ( card == 25 )
   			return "8D";
   		if ( card == 26 )
   			return "8H";
   		if ( card == 27 )
   			return "8S";
   		if ( card == 28 )
   			return "9C";
   		if ( card == 29 )
   			return "9D";
   		if ( card == 30 )
   			return "9H";
   		if ( card == 31 )
   			return "9S";
   		if ( card == 32 )
   			return "10C";
   		if ( card == 33 )
   			return "10D";
   		if ( card == 34 )
   			return "10H";
   		if ( card == 35 )
   			return "10S";
   		if ( card == 36 )
   			return "JC";
   		if ( card == 37 )
   			return "JD";
   		if ( card == 38 )
   			return "JH";
   		if ( card == 39 )
   			return "JS";
   		if ( card == 40 )
   			return "QC";
   		if ( card == 41 )
   			return "QD";
   		if ( card == 42 )
   			return "QH";
   		if ( card == 43 )
   			return "QS";
   		if ( card == 44 )
   			return "KC";
   		if ( card == 45 )
   			return "KD";
   		if ( card == 46 )
   			return "KH";
   		if ( card == 47 )
   			return "KS";
   		if ( card == 48 )
   			return "AC";
   		if ( card == 49 )
   			return "AD";
   		if ( card == 50 )
   			return "AH";
   		if ( card == 51 )
   			return "AS";
   		if ( card == 52 )
   			return "BJ";
   		if ( card == 53 )
   			return "RJ";
   		return "BLARGH!";
   	}
   	public static int cardNumber(String card )
   	{
   		if ( card.equalsIgnoreCase("2C") )
   			return 0;
   		if ( card.equalsIgnoreCase("2D") )
   			return 1;
   		if ( card.equalsIgnoreCase("2H") )
   			return 2;
   		if ( card.equalsIgnoreCase("2S") )
   			return 3;
   		if ( card.equalsIgnoreCase("3C") )
   			return 4;
   		if ( card.equalsIgnoreCase("3D") )
   			return 5;
   		if ( card.equalsIgnoreCase("3H") )
   			return 6;
   		if ( card.equalsIgnoreCase("3S") )
   			return 7;
   		if ( card.equalsIgnoreCase("4C") )
   			return 8;
   		if ( card.equalsIgnoreCase("4D") )
   			return 9;
   		if ( card.equalsIgnoreCase("4H") )
   			return 10;
   		if ( card.equalsIgnoreCase("4S") )
   			return 11;
   		if ( card.equalsIgnoreCase("5C") )
   			return 12;
   		if ( card.equalsIgnoreCase("5D") )
   			return 13;
   		if ( card.equalsIgnoreCase("5H") )
   			return 14;
   		if ( card.equalsIgnoreCase("5S") )
   			return 15;
   		if ( card.equalsIgnoreCase("6C") )
   			return 16;
   		if ( card.equalsIgnoreCase("6D") )
   			return 17;
   		if ( card.equalsIgnoreCase("6H") )
   			return 18;
   		if ( card.equalsIgnoreCase("6S") )
   			return 19;
   		if ( card.equalsIgnoreCase("7C") )
   			return 20;
   		if ( card.equalsIgnoreCase("7D") )
   			return 21;
   		if ( card.equalsIgnoreCase("7H") )
   			return 22;
   		if ( card.equalsIgnoreCase("7S") )
   			return 23;
   		if ( card.equalsIgnoreCase("8C") )
   			return 24;
   		if ( card.equalsIgnoreCase("8D") )
   			return 25;
   		if ( card.equalsIgnoreCase("8H") )
   			return 26;
   		if ( card.equalsIgnoreCase("8S") )
   			return 27;
   		if ( card.equalsIgnoreCase("9C") )
   			return 28;
   		if ( card.equalsIgnoreCase("9D") )
   			return 29;
   		if ( card.equalsIgnoreCase("9H") )
   			return 30;
   		if ( card.equalsIgnoreCase("9S") )
   			return 31;
   		if ( card.equalsIgnoreCase("10C") )
   			return 32;
   		if ( card.equalsIgnoreCase("10D") )
   			return 33;
   		if ( card.equalsIgnoreCase("10H") )
   			return 34;
   		if ( card.equalsIgnoreCase("10S") )
   			return 35;
   		if ( card.equalsIgnoreCase("JC") )
   			return 36;
   		if ( card.equalsIgnoreCase("JD") )
   			return 37;
   		if ( card.equalsIgnoreCase("JH") )
   			return 38;
   		if ( card.equalsIgnoreCase("JS") )
   			return 39;
   		if ( card.equalsIgnoreCase("QC") )
   			return 40;
   		if ( card.equalsIgnoreCase("QD") )
   			return 41;
   		if ( card.equalsIgnoreCase("QH") )
   			return 42;
   		if ( card.equalsIgnoreCase("QS") )
   			return 43;
   		if ( card.equalsIgnoreCase("KC") )
   			return 44;
   		if ( card.equalsIgnoreCase("KD") )
   			return 45;
   		if ( card.equalsIgnoreCase("KH") )
   			return 46;
   		if ( card.equalsIgnoreCase("KS") )
   			return 47;
   		if ( card.equalsIgnoreCase("AC") )
   			return 48;
   		if ( card.equalsIgnoreCase("AD") )
   			return 49;
   		if ( card.equalsIgnoreCase("AH") )
   			return 50;
   		if ( card.equalsIgnoreCase("AS") )
   			return 51;
   		if ( card.equalsIgnoreCase("BJ") )
   			return 52;
   		if ( card.equalsIgnoreCase("RJ") )
   			return 53;
   		return -1;
   	}
}