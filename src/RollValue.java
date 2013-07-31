
/**
 * @(#)RollValue.java
 * Used to handle a table of rolls primarily used for an init sorter
 *
 * @author 
 */

public class RollValue implements Comparable<RollValue>
{
	private final String name;
	private final int roll;
	// the things are variables for future expansions
	// I plan on using thing1 for init passes in shadowrun
	private final int thing1;
	private final int thing2;
	private final int thing3;
    public RollValue( String name, int roll ) 
    {
    	if ( name == null )
    		throw new NullPointerException();
    	this.name = name;
    	this.roll = roll;
    	thing1 = 0;
    	thing2 = 0;
    	thing3 = 0;
    }
    public RollValue( String name, int roll, int thing1 ) 
    {
    	if ( name == null )
    		throw new NullPointerException();
    	this.name = name;
    	this.roll = roll;
    	this.thing1 = thing1;
    	thing2 = 0;
    	thing3 = 0;
    }
    public RollValue( String name, int roll, int thing1, int thing2 ) 
    {
    	if ( name == null )
    		throw new NullPointerException();
    	this.name = name;
    	this.roll = roll;
    	this.thing1 = thing1;
    	this.thing2 = thing2;
    	thing3 = 0;
    }
    public RollValue( String name, int roll, int thing1, int thing2, int thing3 ) 
    {
    	if ( name == null )
    		throw new NullPointerException();
    	this.name = name;
    	this.roll = roll;
    	this.thing1 = thing1;
    	this.thing2 = thing2;
    	this.thing3 = thing3;
    }
    public int getRoll()
    {
    	return roll;
    }
    public String getName()
    {
    	return name;
    }
    public int getA()
    {
    	return thing1;
    }
    public int getB()
    {
    	return thing2;
    }
    public int getC()
    {
    	return thing3;
    }
    public boolean equals( Object o )
    {
    	if (!(o instanceof RollValue))
        	return false;
    	RollValue r = (RollValue)o;
    	return String.valueOf(r.roll).equals(String.valueOf(roll)) &&
    		r.name.equals(name);
    }
    public int hashCode()
    {
    	return 31*name.hashCode() + String.valueOf(roll).hashCode();
    }
    public int compareTo(RollValue r)
    {
    	int rollCmp =  r.roll - roll;
    	if ( rollCmp == 0 )
    	{
    		int modCmp = r.thing1 - thing1;	
    		if ( modCmp == 0 )
    		{
    			int nameCmp = name.compareTo(r.name);
    			return nameCmp;
      		}
      		return modCmp;
    	}
    	return rollCmp;

    }
    public String toString()
    {
    	int yar = roll;
    	if ( yar == 0 )
    	{
    		//1 has as many digits as 0 and won't screw up my math
    		yar = 1;
    	}
    	else if ( yar < 0 )
    	{
    		//make it positive and add an extra digit to account for -
    		yar = yar*(-10);
    	}
    	String spacer = " ";
    	int digits = 0;
    	while ( yar >= 1 )
    	{
    		//chop off a digit
    		yar = yar/10;
    		//count it off
    		digits++;
    	}
    	int spaces = 3 - digits;
		while ( spaces > 0 )
		{
			spacer = spacer + " ";
			spaces--;
		}
    	return roll + spacer + name;
    }
}