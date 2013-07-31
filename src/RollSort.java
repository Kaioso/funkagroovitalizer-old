import java.util.ArrayList;
import java.util.Random;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.*;

class RollSort
{
	private String name;
	private int position;
	private ArrayList<RollValue> rollValues;
	public RollSort( String name )
	{
		this.name = name;
		rollValues = new ArrayList<RollValue>();
		position = 0;
	}
	public void addValue( RollValue value )
	{
		RollValue current = getCurrent();
		for (int i = 0; i < rollValues.size(); i++)
		{
			String name = value.getName();
			RollValue x = rollValues.get(i);
			if ( name.equals(x.getName()) )
				rollValues.remove(i);
		}
		rollValues.add(value);
		Collections.sort(rollValues);
		positionTo(current.getName());
	}
	public RollValue getRollValue(String name)
	{

		for (int i = 0; i < rollValues.size(); i++)
		{
			RollValue x = rollValues.get(i);
			if ( name.equals(x.getName()) )
				return x;
		}
		return new RollValue("null", 0 );
	}
	public String getName()
	{
		return name;
	}
	public int getPosition()
	{
		return position;
	}
	public int nextPos()
	{
		position++;
		if ( position >= rollValues.size() )
			position = 0;
		return position;
	}
	public void topOrder()
	{
		position = 0;
	}
	public boolean removeValue(String value)
	{
		RollValue current = getCurrent();
		if ( value.equals(current.getName()) )
		{
			nextPos();
			current = getCurrent();
		}
		for (int i = 0; i < rollValues.size(); i++)
		{
			RollValue x = rollValues.get(i);
			if ( value.equals(x.getName()) )
			{
				rollValues.remove(i);
				positionTo(current.getName());
				return true;
			}
		}
		return false;
	}
	public RollValue getCurrent()
	{
		if( rollValues.size()> 0 )
			return rollValues.get(position);
		else
			return new RollValue ("null", 0 );
	}
	public void sortList()
	{
		Collections.sort(rollValues);
	}
	public String[] getList()
	{
		String[] list = new String[rollValues.size()];
		for (int i = rollValues.size()-1; i >= 0; i--)
		{
			RollValue x = rollValues.get(i);
			list[i] = x.toString();
		}
		return list;
	}
	public boolean editRoll(String name, int roll)
	{
		for (int i = 0; i < rollValues.size(); i++)
		{
			RollValue x = rollValues.get(i);
			if ( name.equals(x.getName()) )
			{
				RollValue y = new RollValue(name, roll, x.getA(), x.getB(), x.getC() );
				rollValues.remove(i);
				rollValues.add(y);
				return true;
			}
		}
		return false;
	}
	public void addRoll(String name, int roll)
	{
		RollValue value = new RollValue(name, roll);
		for (int i = 0; i < rollValues.size(); i++)
		{
			RollValue x = rollValues.get(i);
			if ( name.equals(x.getName()) )
				rollValues.remove(i);
		}
		rollValues.add(value);
		Collections.sort(rollValues);
	}
	public void addRoll(String name, int roll, int mod)
	{
		RollValue current = getCurrent();
		RollValue value = new RollValue(name, roll, mod);
		for (int i = 0; i < rollValues.size(); i++)
		{
			RollValue x = rollValues.get(i);
			if ( name.equals(x.getName()) )
				rollValues.remove(i);
		}
		positionTo(current.getName());
		rollValues.add(value);
		Collections.sort(rollValues);
	}
	public boolean hasName( String name )
	{
		for (int i = 0; i < rollValues.size(); i++)
		{
			RollValue x = rollValues.get(i);
			if ( name.equals(x.getName()) )
				return true;
		}
		return false;
	}
	public void positionTo( String name )
	{
		int p = position;
		for (int i = 0; i < rollValues.size(); i++)
		{
			RollValue x = rollValues.get(i);
			if ( name.equals(x.getName()) )
				p = i;
		}
		position = p;
	}
}
