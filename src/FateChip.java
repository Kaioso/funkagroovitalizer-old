
public class FateChip 
{
	
	/**
	 * Method FateChip
	 *
	 *
	 */
	private int white;
	private int red;
	private int blue;
	private int legend;
	private String name;
	
	public FateChip() 
	{
		name = "";
		white = 0;
		red = 0;
		blue = 0;
		legend = 0;
	}
	
	public FateChip( String handle )
	{
		name = handle;
		white = 0;
		red = 0;
		blue = 0;
		legend = 0;
	}	
	public FateChip( String handle, int w, int r, int b, int l )
	{
		name = handle;
		white = w;
		red = r;
		blue = b;
		legend = l;
	}
	public String getName()
	{
		return name;
	}
	public String getStatus()
	{
		return name + " has " + white + " white " + red + " red " + blue + " blue and " + legend + " legend chips."; 
	}
	public boolean nameEquals( String handle )
	{
		if ( handle.equalsIgnoreCase(name) )
			return true;
		return false;
	}
	public boolean playWhite()
	{
		if ( white > 0 )
		{
			white--;
			return true;
		}
		return false;
	}
	public boolean playRed()
	{
		if ( red > 0 )
		{
			red--;
			return true;
		}
		return false;
	}
	public boolean playBlue()
	{
		if ( blue > 0 )
		{
			blue--;
			return true;
		}
		return false;
	}
	public boolean playLegend()
	{
		if ( legend > 0 )
		{
			legend--;
			return true;
		}
		return false;
	}
	public void giveWhite()
	{
		white++;
	}
	public void giveRed()
	{
		red++;
	}
	public void giveBlue()
	{
		blue++;
	}
	public void giveLegend()
	{
		legend++;
	}
	public void setWhite( int amount )
	{
		white = amount;
	}
	public void setRed( int amount )
	{
		red = amount;
	}
	public void setBlue( int amount )
	{
		blue = amount;
	}
	public void setLegend( int amount )
	{
		legend = amount;
	}
}
