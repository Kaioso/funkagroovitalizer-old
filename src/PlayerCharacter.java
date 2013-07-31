/**
 * @(#)PlayerCharacter.java
 *
 *
 * @author 
 * @version 1.00 2007/8/15
 */
import java.util.*;
import java.lang.Integer;

public class PlayerCharacter implements Comparable<PlayerCharacter>
{
	private String name;
	private ArrayList<Integer> hand;

    public PlayerCharacter() 
    {
    	int i;
    	name = " ";
    	hand = new ArrayList<Integer>();
    }    
    public PlayerCharacter( String nick )
    {
    	int i;
    	name = nick;
    	hand = new ArrayList<Integer>();
    }        
    public String getName()
    {
    	return name;
    }
    public int[] getHand()
    {
    	int a = hand.size();
    	int[] report = new int[a];
    	for (int i = 0; i < hand.size(); i++)
    	{
    		report[i] = hand.get(i);
    	}
    	return report;
    }    
    public boolean hasCard( int card )
    {
    	int a;
    	for (int i = 0; i < hand.size(); i++)
    	{
    		a = hand.get(i);
    		if ( a == card )
    			return true;
    	}
    	return false;
    }
   	public void emptyHand()
   	{
		hand.clear();
   	}
   	public int handSize()
   	{
   		return hand.size();
   	}
   	public void removeCard( int card )
   	{
   		int a;
		for (int i = 0; i < hand.size(); i++)
    	{
    		a = hand.get(i);
    		if ( a == card )
    			hand.remove(i);
    	}
   	}
   	public void addCard( int card )
   	{
		hand.add(card);
   	}
   	public static String cardName(int card )
   	{
   		if ( card == 0 )
   			return "2C";
   		if ( card == 1 )
   			return "42D";
   		if ( card == 2 )
   			return "42H";
   		if ( card == 3 )
   			return "2S";
   		if ( card == 4 )
   			return "3C";
   		if ( card == 5 )
   			return "43D";
   		if ( card == 6 )
   			return "43H";
   		if ( card == 7 )
   			return "3S";
   		if ( card == 8 )
   			return "4C";
   		if ( card == 9 )
   			return "44D";
   		if ( card == 10 )
   			return "44H";
   		if ( card == 11 )
   			return "4S";
   		if ( card == 12 )
   			return "5C";
   		if ( card == 13 )
   			return "45D";
   		if ( card == 14 )
   			return "45H";
   		if ( card == 15 )
   			return "5S";
   		if ( card == 16 )
   			return "6C";
   		if ( card == 17 )
   			return "46D";
   		if ( card == 18 )
   			return "46H";
   		if ( card == 19 )
   			return "6S";
   		if ( card == 20 )
   			return "7C";
   		if ( card == 21 )
   			return "47D";
   		if ( card == 22 )
   			return "47H";
   		if ( card == 23 )
   			return "7S";
   		if ( card == 24 )
   			return "8C";
   		if ( card == 25 )
   			return "48D";
   		if ( card == 26 )
   			return "48H";
   		if ( card == 27 )
   			return "8S";
   		if ( card == 28 )
   			return "9C";
   		if ( card == 29 )
   			return "49D";
   		if ( card == 30 )
   			return "49H";
   		if ( card == 31 )
   			return "9S";
   		if ( card == 32 )
   			return "10C";
   		if ( card == 33 )
   			return "410D";
   		if ( card == 34 )
   			return "410H";
   		if ( card == 35 )
   			return "10S";
   		if ( card == 36 )
   			return "JC";
   		if ( card == 37 )
   			return "4JD";
   		if ( card == 38 )
   			return "4JH";
   		if ( card == 39 )
   			return "JS";
   		if ( card == 40 )
   			return "QC";
   		if ( card == 41 )
   			return "4QD";
   		if ( card == 42 )
   			return "4QH";
   		if ( card == 43 )
   			return "QS";
   		if ( card == 44 )
   			return "KC";
   		if ( card == 45 )
   			return "4KD";
   		if ( card == 46 )
   			return "4KH";
   		if ( card == 47 )
   			return "KS";
   		if ( card == 48 )
   			return "AC";
   		if ( card == 49 )
   			return "4AD";
   		if ( card == 50 )
   			return "4AH";
   		if ( card == 51 )
   			return "AS";
   		if ( card == 52 )
   			return "BJ";
   		if ( card == 53 )
   			return "4RJ";
   		return "BLARGH!";
   	}   
    public void setName( String nick )
    {
    	name = nick;
    }
    public int highCard()
    {
    	int a;
    	int highest = 0;
    	for (int i = 0; i < hand.size(); i++)
    	{
    		a = hand.get(i);
    		if ( a > highest )
    			highest = a;
    	}
    	return highest;	
    }
    public boolean equals( Object o )
    {
    	if (!(o instanceof PlayerCharacter))
        	return false;
    	PlayerCharacter p = (PlayerCharacter)o;
    	return String.valueOf(p.highCard()).equals(String.valueOf(this.highCard())) &&
    		p.name.equals(name);
    }
    public int compareTo(PlayerCharacter p)
    {
    	int cardCmp = this.highCard() - p.highCard();
    	if ( cardCmp == 0 )
    	{
    		return name.compareTo(p.getName());
    	}
    	return cardCmp;
    }
}