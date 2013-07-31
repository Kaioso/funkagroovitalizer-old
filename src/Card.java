/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ccelizic
 */
public class Card implements Comparable<Card>
{
    private String fullName;
    private int card;
    private int suit;
    private String shortDesc;
    private String longDesc;
    private String suitName;
    private String numberName;
    
    public Card()
    {
        fullName = "";
        card = 0;
        suit = 0;
        shortDesc = "";
        longDesc = "";
        suitName = "";
        numberName = "";
    }
    public Card(String name)
    {
        fullName = name;
        card = 0;
        suit = 0;
        shortDesc = "";
        longDesc = "";
        suitName = "";
        numberName = "";
    }
    public void setName(String name)
    {
        fullName = name;
    }
    public void setSuit(int suitS)
    {
        suit = suitS;
    }
    public void setStrength(int strength)
    {
        card = strength;
    }
    public void setShort(String shortD)
    {
        shortDesc = shortD;
    }
    public void setLong(String longD)
    {
        longDesc = longD;
    }
    public void setSuitName(String suitN)
    {
        suitName = suitN;
    }
    public void setNumberName(String nName)
    {
        numberName = nName;
    }
    public String getName()
    {
        return fullName;
    }
    public int getSuit()
    {
        return suit;
    }
    public int getStrength()
    {
        return card;
    }
    public String getShort()
    {
        return shortDesc;
    }
    public String getLong()
    {
        return longDesc;
    }
    public String getSuitName()
    {
        return suitName;
    }
    public String getNumberName()
    {
        return numberName;
    }
    @Override
    public int compareTo(Card c)
    {
        int comp = card - c.getStrength();
        if ( comp == 0 )
        {
            comp = card - c.getSuit();
        }
        return comp;
    }
    public boolean equals(Card c)
    {
        return this.getStrength()==c.getStrength()&&this.getSuit()==c.getSuit();
    }
}
