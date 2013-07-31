import java.util.Random;
import java.lang.Integer;
import java.util.ArrayList;
import java.util.*;

class PlayerProfile 
{
	private String name;
	private boolean notify;
	private boolean sound;
	private String soundFile;
	
	PlayerProfile(String n)
	{
		name = n;
		notify = false;
		sound = false;
		soundFile = "";
	}
	public String getName()
	{
		return name;
	}
	public boolean isNotify()
	{
		return notify;
	}
	public boolean isSound()
	{
		return sound;
	}
	public String getSoundFile()
	{
		return soundFile;
	}
	public void setName(String n)
	{
		name = n;
	}
	public void setNotify(boolean b)
	{
		notify = b;
	}
	public void setSound(boolean b)
	{
		sound = b;
	}
	public void setSoundFile(String s)
	{
		soundFile = s;
	}
	public String toString()
	{
		int n = 0;
		int s = 0;
		if ( notify )
			n = 1;
		if ( sound )
			s = 1;
		return name + " " + n + " " + s + " " + soundFile;
	}
}
