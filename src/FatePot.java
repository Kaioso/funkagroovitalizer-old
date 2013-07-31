
import java.util.*;

public class FatePot 
{
	private String name;
	private ArrayList<FateChip> players;
	private int white;
	private int red;
	private int blue;
	private int legend;
	
	public FatePot()
	{
		name = "master";
		players = new ArrayList<FateChip>();
		white = 20;
		red = 10;
		blue = 5;
		legend = 0;
	}
	public FatePot( String handle )
	{
		name = handle;
		players = new ArrayList<FateChip>();
		white = 20;
		red = 10;
		blue = 5;
		legend = 0;
	}
	public String playerStatus( String handle )
	{
		for (int i = 0; i < players.size(); i++)
    	{
    		FateChip p = players.get(i);
    		if ( p.getName().equalsIgnoreCase(handle) )
    			return p.getStatus();
    	}
    	return handle + " is not a player.";
	}
	public boolean hasPlayer( String handle )
	{
    	for (int i = 0; i < players.size(); i++)
    	{
    		FateChip p = players.get(i);
    		if ( p.getName().equalsIgnoreCase(handle) )
    			return true;
    	}
    	return false;
	}
	public FateChip getPlayer( String handle )
	{
    	for (int i = 0; i < players.size(); i++)
    	{
    		FateChip p = players.get(i);
    		if (p.getName().equalsIgnoreCase(handle) )
    			return p;
    	}
    	return new FateChip();		
	}
	public String useWhite( String handle )
	{
		int pos = -1;
		boolean found = false;
		FateChip p = new FateChip();
    	for (int i = 0; i < players.size(); i++)
    	{
    		p = players.get(i);
    		if (p.getName().equalsIgnoreCase(handle) )
    		{
    			pos = i;
    			i = players.size() + 1;
    		}
    	}
    	if ( pos == -1 )
			return handle + " is not part of this game.";
		boolean has = p.playWhite();
		if ( !has )
			return handle + " has no white chips.";
		else
		{
			white++;
			players.set( pos , p);
			return handle + " plays a white chip.";
		}
	}	
	public String useRed( String handle )
	{
		int pos = -1;
		boolean found = false;
		FateChip p = new FateChip();
    	for (int i = 0; i < players.size(); i++)
    	{
    		p = players.get(i);
    		if (p.getName().equalsIgnoreCase(handle) )
    		{
    			pos = i;
    			i = players.size() + 1;
    		}
    	}
    	if ( pos == -1 )
			return handle + " is not part of this game.";
		boolean has = p.playRed();
		if ( !has )
			return handle + " has no red chips.";
		else
		{
			red++;
			players.set( pos , p);
			return handle + " plays a red chip.";
		}
	}
	public String useBlue( String handle )
	{
		int pos = -1;
		boolean found = false;
		FateChip p = new FateChip();
    	for (int i = 0; i < players.size(); i++)
    	{
    		p = players.get(i);
    		if (p.getName().equalsIgnoreCase(handle) )
    		{
    			pos = i;
    			i = players.size() + 1;
    		}
    	}
    	if ( pos == -1 )
			return handle + " is not part of this game.";
		boolean has = p.playBlue();
		if ( !has )
			return handle + " has no blue chips.";
		else
		{
			blue++;
			players.set( pos , p);
			return handle + " plays a blue chip.";
		}
	}
	public String useLegend( String handle )
	{
		int pos = -1;
		boolean found = false;
		FateChip p = new FateChip();
    	for (int i = 0; i < players.size(); i++)
    	{
    		p = players.get(i);
    		if (p.getName().equalsIgnoreCase(handle) )
    		{
    			pos = i;
    			i = players.size() + 1;
    		}
    	}
    	if ( pos == -1 )
			return handle + " is not part of this game.";
		boolean has = p.playLegend();
		if ( !has )
			return handle + " has no legend chips.";
		else
		{
			players.set( pos , p);
			return handle + " plays a legend chip.";
		}
	}
	public String chipCount()
	{
		return "White: " + white + " Red: " + red + " Blue: " + blue + " Legend: " + legend;
	}
	public void setChips( int w, int r, int b, int l )
	{
		white = w;
		red = r;
		blue = b;
		legend = l;
	}
	public void addWhite()
	{
		white++;
	}
	public void addBlue()
	{
		blue++;
	}
	public void addRed()
	{
		red++;
	}
	public void addLegend()
	{
		legend++;
	}
    public void addPlayer(String aname)
    {
    	FateChip player = new FateChip( aname );
    	for (int i = 0; i < players.size(); i++)
    	{
    		FateChip p = players.get(i);
    		if (p.getName().equalsIgnoreCase(aname))
    			return;
    	}
    	players.add( player );
    }
    public String awardWhite( String handle )
    {
    	int pos = -1;
		boolean found = false;
		FateChip p = new FateChip();
    	for (int i = 0; i < players.size(); i++)
    	{
    		p = players.get(i);
    		if (p.getName().equalsIgnoreCase(handle) )
    		{
    			pos = i;
    			i = players.size() + 1;
    		}
    	}
    	if ( pos == -1 )
    		return handle + " is not part of this game.";
    	else if ( white < 1 )
			return "There are no white chips to give.";
    	else
    	{
    		p.giveWhite();
    		players.set( pos , p);
    		return handle + " has been given a white chip from the pot.";
    	}
    }
    public String awardRed( String handle )
    {
    	int pos = -1;
		boolean found = false;
		FateChip p = new FateChip();
    	for (int i = 0; i < players.size(); i++)
    	{
    		p = players.get(i);
    		if (p.getName().equalsIgnoreCase(handle) )
    		{
    			pos = i;
    			i = players.size() + 1;
    		}
    	}
    	if ( pos == -1 )
    		return handle + " is not part of this game.";
    	else if ( red < 1 )
			return "There are no red chips to give.";
    	else
    	{
    		p.giveRed();
    		players.set( pos , p);
    		return handle + " has been given a red chip from the pot.";
    	}
    }
    public String awardBlue( String handle )
    {
    	int pos = -1;
		boolean found = false;
		FateChip p = new FateChip();
    	for (int i = 0; i < players.size(); i++)
    	{
    		p = players.get(i);
    		if (p.getName().equalsIgnoreCase(handle) )
    		{
    			pos = i;
    			i = players.size() + 1;
    		}
    	}
    	if ( pos == -1 )
    		return handle + " is not part of this game.";
    	else if ( blue < 1 )
			return "There are no blue chips to give.";
    	else
    	{
    		p.giveBlue();
    		players.set( pos , p);
    		return handle + " has been given a blue chip from the pot.";
    	}
    }
    public String awardLegend( String handle )
    {
    	int pos = -1;
		boolean found = false;
		FateChip p = new FateChip();
    	for (int i = 0; i < players.size(); i++)
    	{
    		p = players.get(i);
    		if (p.getName().equalsIgnoreCase(handle) )
    		{
    			pos = i;
    			i = players.size() + 1;
    		}
    	}
    	if ( pos == -1 )
    		return handle + " is not part of this game.";
    	else if ( legend < 1 )
			return "There are no legend chips to give.";
    	else
    	{
    		p.giveBlue();
    		players.set( pos , p);
    		return handle + " has been given a legend chip from the pot.";
    	}
    }
    public String drawChip( String handle )
    {
    	int total = white + red + blue + legend;
    	int pos = -1;
		boolean found = false;
		FateChip p = new FateChip();
    	for (int i = 0; i < players.size(); i++)
    	{
    		p = players.get(i);
    		if (p.getName().equalsIgnoreCase(handle) )
    		{
    			pos = i;
    			i = players.size() + 1;
    		}
    	}
    	if ( pos == -1 )
    		return handle + " is not part of this game.";
    	else if ( total < 1 )
    		return "No more chips to give.";
		int roll = Dice.rollDice(total);
		if ( roll <= white )
		{
			white--;
			p.giveWhite();
			players.set( pos, p );
			return handle + " drew a white chip from the pot.";
		}
		else if ( roll <= (white+red) )
		{
			red--;
			p.giveRed();
			players.set( pos, p );
			return handle + " drew a red chip from the pot.";
		}
		else if ( roll <= (white+red+blue) )
		{
			blue--;
			p.giveBlue();
			players.set( pos, p );
			return handle + " drew a blue chip from the pot.";
		}
		else
		{
			legend--;
			p.giveLegend();
			players.set( pos, p );
			return handle + " drew a legend chip from the pot.";
		}
    }
    public void potReset()
    {
    	players = new ArrayList<FateChip>();
    	white = 20;
		red = 10;
		blue = 5;
		legend = 0;
    }
}
