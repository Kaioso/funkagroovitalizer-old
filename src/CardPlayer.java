/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ccelizic
 */
import java.util.*;

public class CardPlayer implements Comparable<CardPlayer> {
    private ArrayList<Card> hand;
    private String name;

    public CardPlayer()
    {
        name = "";
        hand = new ArrayList<Card>();
    }
    public CardPlayer(String pName)
    {
        name = pName;
        hand = new ArrayList<Card>();
    }
    public Card highCard()
    {
    	Card a;
    	Card highest = new Card();
    	for (int i = 0; i < hand.size(); i++)
    	{
    		a = hand.get(i);
    		if ( highest.compareTo(a) > 0 )
    			highest = a;
    	}
    	return highest;	
    }
    public void addCard(Card c)
    {
        hand.add(c);
    }
    public Card[] getHand()
    {
    	int a = hand.size();
    	Card[] out = new Card[a];
    	for (int i = 0; i < hand.size(); i++)
    	{
    		out[i] = hand.get(i);
    	}
    	return out;
    }
    public Card[] emptyHand()
    {
    	int a = hand.size();
    	Card[] out = new Card[a];
    	for (int i = 0; i < hand.size(); i++)
    	{
            out[i] = hand.get(i);
    	}
        hand.clear();
    	return out;
    }
    public String getName()
    {
        return name;
    }
    public int handSize()
    {
        return hand.size();
    }
    public boolean hasCard(Card c)
    {
    	int a = hand.size();
    	Card x = new Card();
    	for (int i = 0; i < hand.size(); i++)
    	{
            x = hand.get(i);
            if (x.equals(c))
            {
                return true;
            }
    	} 
        return false;
    }
    public void removeCard(Card c)
    {
    	int a = hand.size();
    	Card x = new Card();
    	for (int i = 0; i < hand.size(); i++)
    	{
            x = hand.get(i);
            if (x.equals(c))
            {
                hand.remove(i);
            }
    	} 
        return;
    }    
    public void setName(String pName)
    {
        name = pName;
    }
    
    @Override
    public int compareTo(CardPlayer p)
    {
        int comp = this.highCard().getStrength() - p.highCard().getStrength();
        if (comp == 0)
        {
            return this.highCard().getSuit() - p.highCard().getSuit();
        }
        return comp;
    }
}
