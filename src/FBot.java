/**
 * @(#)FBot.java
 *
 * The funkagroovitalizer IRC dice bot.  Based off of Pircbot.
 *
 * Before you lays the tangled horrific piece of code that is my bot
 * feel free to use anything you see here, but please give credit
 * where it is due.
 *
 * @Author:  Carl Celizic
 * @version 2.1 12/5/2008
 */
import org.jibble.pircbot.*;
import java.util.*;
import java.io.*;

public class FBot extends PircBot
{
	private ArrayList<ExpTot> totals;
	private ArrayList<PlayerProfile> profiles;
	private ArrayList<Deck> decks;
	private ArrayList<RollSort> tables;
	private FatePot pot;
	private ArrayList<String> gms;
        private String joinEmote;
	//What is Fbot?  Some say it is a way of life, other's say it is
	//a state of mind.  I say it is at it's core a list of variables.
	//such is the life of a java object.
    public FBot()
    {
    	totals = new ArrayList<ExpTot>();
    	profiles = new ArrayList<PlayerProfile>();
    	Deck d = new Deck( "Master" );
        this.setName("Funkagroovitalizer");
        decks = new ArrayList<Deck>();
        decks.add(d);
        gms = new ArrayList<String>();
        tables = new ArrayList<RollSort>();
        pot = new FatePot();
        loadGMs();
        loadProfiles();
        loadTotals();
    }
    public FBot( String handle )
    {
    	totals = new ArrayList<ExpTot>();
    	profiles = new ArrayList<PlayerProfile>();
    	Deck d = new Deck( "Master" );
        this.setName(handle);
        this.setLogin(handle);
        this.setFinger(handle);
        this.setVersion("Funkagroovitalizer 1.0" );
        decks = new ArrayList<Deck>();
        decks.add(d);
        tables = new ArrayList<RollSort>();
        gms = new ArrayList<String>();
        pot = new FatePot();
        loadGMs();
        loadProfiles();
        loadTotals();
    }
 	//Command recognition and sorting.
    public void onMessage(String channel, String sender,
            String login, String hostname, String message)
    {
    	//Break that string down into bite sized kibble!
    	//IF it begins with the right characters...
    	if ( isInt(""+message.charAt(0)) || message.startsWith("d") || message.startsWith("D") || message.startsWith("!") )
    	{
    		boolean dStart=false;
    		String[] test = message.toLowerCase().split(" ");
    		if (isEquation(test[0]))
    		{
				message = "!roll " + message.trim();
				dStart=true;
    		}

    		String[] args = message.split(" ");
	    	if (args.length < 1 )
	    		return;
	    	//checking for valid dice string at the start
   	    	//Checking for commands
   	    	//TODO:  Some of these should become functions....
   	    	else if ( args[0].equalsIgnoreCase("!stats") )
   	    	//This is one of those whims  I wanted to see how fair the dice were.
   	    	{
   	    		int first=1;
    	   		int second=1;
       			boolean idiot = false;
       			if ( args.length < 3 )
       			{
       				sendMessage( channel, "Useage:  !stats <size of dice> <number of rolls>" );
       			}
       			else
       			{
					try
					{
						first = Integer.parseInt( args[1] );
						second = Integer.parseInt( args[2] );
					}
					catch (Exception e)
					{
						idiot = true;
						//bitter? us?
						sendMessage( channel, "Useage:  !stats <size of dice> <number of rolls>" );
						return;
					}
					if (idiot)
					{
						sendMessage( channel, "Useage:  !stats <size of dice> <number of rolls>" );
						return;
					}
					else
					{
						sendMessage( channel, Dice.statRun(first, second) );
						return;
					}
   	    		}
   	    	}
			else if ( args[0].equalsIgnoreCase("!join"))
			{
				if ( args.length < 2 )
				{
   	    			sendMessage( channel, "!join [Channel]" );
   	    			return;
				}
				joinChannel( args[1] );
				sendAction( args[1], getJoin(sender) );
			}
   	    	else if ( args[0].equalsIgnoreCase("!roll"))
   	    	{
   	    		String daMessage = "";
   	    		int total = 0;
   	    		if ( args.length < 2 )
   	    		{
   	    			if (!dStart)
   	    				sendMessage( channel, "!roll [rolls] [Dice Equation] <Table>  xdy rolls x dice size y.  e denotes an exploding dice (Max value rerolled). xey." );
   	    			return;
   	    		}
   	    		if ( args.length > 2 && isInt(args[1]) )
   	    		{
   	    			if ( !isEquation( args[2] ) )
   	    			{
   	    				if (!dStart)
   	    					sendMessage( channel, "!roll [rolls] [Dice Equation] <table>  xdy rolls x dice size y.  e denotes an exploding dice (Max value rerolled). xey." );
   	    				return;
   	    			}
   	    			String tally = "";
   	    			int i = 0;
   	    			int gwah = getInt( args[1] );
   	    			while ( gwah > 0 )
   	    			{
						i=parseOne(args[2]);
						total+=i;
						tally=tally+" "+i;
						gwah--;
   	    			}
   	    			daMessage = "rolled " + args[2] + " for " + sender + " and got (" + tally + " ) for a total of: " + total;
   	    			if ( args.length > 3 )
   	    			{
   	    				if ( !hasTable(args[3]) )
						{
							sendAction( channel, daMessage );
							return;
						}
						else
						{
							RollValue r = new RollValue( sender, total, 0 );
							RollSort x = getTable(args[3]);
							x.addValue(r);
							x.sortList();
							addTable(x);
							sendAction( channel, daMessage + " added to " + args[3] );
							return;
						}
   	    			}
   	    			sendAction( channel, daMessage );
   	    			return;
   	    		}
   	    		if ( isEquation(args[1]) )
   	    		{
   	    			total = parseOne(args[1]);
   	    		}
   	    		else
   	    		{
   	    			if (!dStart)
   	    				sendMessage( channel, "!roll [rolls] [Dice Equation] <Table>  xdy rolls x dice size y.  e denotes an exploding dice (Max value rerolled). xey." );
   	    			return;
   	    		}
				daMessage = "rolled " + args[1] + " for " + sender + " and got " + total;
    			if ( args.length > 2 )
    			{
    				if ( !hasTable(args[2]) )
					{
						sendAction( channel, daMessage );
						return;
					}
					else
					{
						RollValue r = new RollValue( sender, total, 0 );
						RollSort x = getTable(args[2]);
						x.addValue(r);
						x.sortList();
						addTable(x);
						sendAction( channel, daMessage + " added to " + args[2] );
						return;
					}
    			}
    			sendAction( channel, daMessage );
    			return;
   	    	}
   	    	else if ( args[0].equalsIgnoreCase("!hd") )
   	    	{
   	    		if (args.length <2 )
   	    		{
   	    			sendMessage( channel, "!hd [number of dice] [modifier] accepts half dice values such as 4.5.  Teal die is half die." );
   	    			return;
   	    		}
   	    		else if ( args.length < 3 )
   	    		{
   	    			sendAction( channel, Dice.heroDamage(getDouble(args[1]), 0, sender ) );
   	    			return;
   	    		}
   	    		else
   	    		{
   	    			sendAction( channel, Dice.heroDamage(getDouble(args[1]), getInt(args[2]), sender ) );
   	    			return;
   	    		}
   	    	}
   	    	else if ( args[0].equalsIgnoreCase("!ha") )
   	    	{
   	    		if ( args.length < 2 )
   	    		{
   	    			sendMessage( channel, "!ha [OCV] Rolls an attack using the HERO system.");
   	    			return;
   	    		}
   	    		else
   	    		{
   	    			sendAction( channel, Dice.heroAttack( getInt(args[1]), sender )  );
   	    			return;
   	    		}
   	    	}
   	    	else if ( args[0].equalsIgnoreCase("!wg") )
   	    	{
   	    		if ( args.length < 2 )
   	    		{
   	    			sendMessage( channel, "!wg [number of dice] [modifier] Makes a roll using Westend Games d6 system." );
   	    			return;
   	    		}
   	    		else if ( args.length < 3 )
   	    		{
   	    			int dice = getInt(args[1]);
   	    			String output = Dice.westDice(dice, 0, sender );
   	    			sendAction( channel, output );
   	    			return;
   	    		}
   	    		else
   	    		{
   	    			int dice = getInt(args[1]);
   	    			int mod = getInt(args[2]);
   	    			String output = Dice.westDice(dice, mod, sender );
   	    			sendAction( channel, output );
   	    			return;
   	    		}
   	    	}
   	    	else if ( args[0].equalsIgnoreCase("!aarnrace") )
   	    	{
   	    		sendMessage( channel, "Your randomly determined race is: " + aarnRace( false, false, "" ) );
   	    	}
   	    	else if ( args[0].equalsIgnoreCase("!wonder") )
   	    	{
   	    		if (args.length < 2 )
   	    		{
   	    			sendMessage( channel, wonder(false));
   	    			return;
    			}
    			else
    			{
    				if (args[1].equalsIgnoreCase("expanded"))
    				{
    					sendMessage( channel, wonder(true));
    					return;
    				}
    				else
    				{
    					sendMessage( channel, wonder(true));
    					return;
    				}
    			}
   	    	}
   	    	else if ( args[0].equalsIgnoreCase("!value") )
   	    	//pointbuy value calculator
   	    	//if only all my command checks were this tidy
   	    	{
   	    		sendMessage( channel, pointValue(args) );
   	    		return;
   	    	}
   	    	else if ( args[0].equalsIgnoreCase("!addgm") )
   	    	{
   	    		if ( !isGm(sender) )
   	    			return;
   	    		if ( args.length < 2 )
   	    		{
   	    			sendMessage( channel, "Useage:  !addgm <name>" );
   	    			return;
   	    		}
   	    		else
   	    		{
   	    			setGm(args[1]);
   	    			sendMessage( channel, args[1] + " was added to the list of gms.");
   	    			saveGMs();
   	    			return;
   	    		}
   	    	}
   	    	else if ( args[0].equalsIgnoreCase("!npc"))
   	    	{
   	    		if ( args.length < 4 || !isRoll(args[2]) || !hasTable(args[3]) )
   	    		{
   	    			sendMessage( channel, "Useage:  !npc <npc name> xdy+z <table name>" );
   	    			return;
   	    		}
   	    		RollSort x = getTable(args[3]);
   	    		int roll = rollerInt(args[2]);
   	    		x.addRoll(args[1], roll, getMod(args[2]));
   	    		addTable(x);
   	    		sendAction(channel, "rolled a (" + args[2] + ") for " + args[1] + " and got: " + roll + " added to " + args[3] );
   	    	}
   	    	else if ( args[0].equalsIgnoreCase("!save"))
   	    	{
   	    		boolean monkeh = saveGMs();
   	    		if (!monkeh)
   	    			sendMessage( channel, "Error saving to file!" );
   	    		else
   	    			sendAction( channel, "saves the current list of gms.");
   	    		monkeh = saveProfiles();
   	    		if (!monkeh)
   	    			sendMessage( channel, "Error saving to file!" );
   	    		else
   	    			sendAction( channel, "saves the current list of profiles.");
   	    	}
   	    	else if ( args[0].equalsIgnoreCase("!use") )
   	    	//fatepot chip using for deadlands
   	    	//discards the specified chip and spits
   	    	//back a report based on if there was a chip
   	    	//to give
   	    	{
   	    		String report = "";
   	    		if ( args.length < 2 )
   	    		{
   	    			sendMessage( channel, "Useage:  !use <color>" );
   	    			return;
   	    		}
   	    		else if ( args[1].equalsIgnoreCase("white") )
   	    		{
   	    			report = pot.useWhite(sender);
   	    		}
   	    		else if ( args[1].equalsIgnoreCase("red") )
   	    		{
   	    			report = pot.useRed(sender);
   	    		}
 	      		else if ( args[1].equalsIgnoreCase("blue") )
   	    		{
   	    			report = pot.useBlue(sender);
   	    		}
   	    		else if ( args[1].equalsIgnoreCase("legend") )
   	    		{
   	    			report = pot.useLegend(sender);
   	    		}
   	    		sendMessage( channel, report );
   	    	}
   	    	else if ( args[0].equalsIgnoreCase("!start") && isGm(sender) )
   	    	{
   	    		if ( args.length < 2 )
   	    		{
   	    			sendMessage(channel, "Useage:  !start <list>");
   	    			return;
   	    		}
   	    		if ( !hasTable(args[1]) )
   	    		{
   	    			sendMessage(channel, "That list does not exist.");
   	    			return;
   	    		}
   	    		RollSort x = getTable(args[1]);
   	    		x.topOrder();
   	    		addTable(x);
   	    		sendMessage(channel, "It's now top of the order, use the !next command to cycle through list.");
   	    		return;
   	    	}
   	    	else if ( args[0].equalsIgnoreCase("!next") && isGm(sender) )
   	    	{
   	    		if ( args.length < 2 )
   	    		{
   	    			sendMessage(channel, "Useage:  !next <list>");
   	    			return;
   	    		}
   	    		if ( !hasTable(args[1]) )
   	    		{
   	    			sendMessage(channel, "That list does not exist.");
   	    			return;
   	    		}
   	    		RollSort x = getTable(args[1]);
   	    		RollValue v = x.getCurrent();
				String current = v.getName();
				x.nextPos();
				v = x.getCurrent();
				String next = v.getName();
				addTable(x);
				sendMessage(channel, "" + current + "'s turn!  Next is " + next + "." );
				if (notifyMe(current))
				{
					sendMessage(current, "Your turn has come up.");
				}
				if (beepMe(current))
				{
					sendCTCPCommand(current, "SOUND " + getSFile(current) );
				}
				return;
   	    	}
   		   	else if ( args[0].equalsIgnoreCase("!potput") )
   		   	//gm command for putting chips into the pot.
   		   	{
   	   			if ( !isGm(sender) )
      				return;
      			if ( args.length < 2 )
      			{
      				sendMessage( channel, "Useage:  !potput <color>" );
      				return;
      			}
      			else if ( args[1].equalsIgnoreCase( "white" ) )
      			{
      				pot.addWhite();
      				sendMessage( channel, sender + " tossed a white chip into the pot." );
      			}
      			else if ( args[1].equalsIgnoreCase( "red" ) )
      			{
      				pot.addRed();
      				sendMessage( channel, sender + " tossed a red chip into the pot." );
      			}
    	  		else if ( args[1].equalsIgnoreCase( "blue" ) )
      			{
      				pot.addBlue();
      				sendMessage( channel, sender + " tossed a blue chip into the pot." );
      			}
      			else if ( args[1].equalsIgnoreCase( "legend" ) )
      			{
      				pot.addLegend();
      				sendMessage( channel, sender + " tossed a legend chip into the pot." );
      			}
      			else
      			{
      				sendMessage( channel, "Useage: !potput <color>" );
      			}
   	   		}
      		else if ( args[0].equalsIgnoreCase("!potadd") )
      		{
      			if ( !isGm(sender) )
      				return;
      			if ( args.length < 2 )
      			{
      				sendMessage( channel, "Give a name to add to the Fate Pot." );
      				return;
      			}
      			else
      			{
      				pot.addPlayer( args[1] );
      				sendAction( channel, "adds " + args[1] + " to the pot." );
 	     		}
   	 	  	}
     	 	else if ( args[0].equalsIgnoreCase("!potreset") )
     	 	{
     	 		if ( !isGm(sender) )
					return;
				pot.potReset();
				sendMessage( channel, "Done." );
      		}
      		else if ( args[0].equalsIgnoreCase("!potdraw") )
      		{
      			int draws = 1;
      			String report;
      			if ( args.length > 1)
 	     		{
					try
					{
						draws = Integer.parseInt( args[1] );
					}
					catch (Exception e)
					{
						draws = 1;
					}
      			}
      			for ( int i = 0; i < draws; i++ )
  	    		{
  		       		report = pot.drawChip( sender );
      				sendMessage( channel, report );
      			}
     	 	}
     	 	else if ( args[0].equalsIgnoreCase("!potstatus") )
     	 	{
    	  		sendMessage( channel, pot.chipCount() );
    	  	}
    	  	else if ( args[0].equalsIgnoreCase("!potset") )
   		   	{
    	  		int w = 0;
    	  		int r = 0;
    	  		int b = 0;
    	  		int l = 0;
      			if ( args.length < 5 )
      			{
      				sendMessage(channel, "useage !potset <white> <red> <blue> <legend>" );
      				return;
      			}
				try
				{
					w = Integer.parseInt( args[1] );
				}
				catch (Exception e)
				{
					sendMessage(channel, "useage !potset <white> <red> <blue> <legend>" );
					return;
				}
      			try
				{
					r = Integer.parseInt( args[2] );
				}
				catch (Exception e)
				{
					sendMessage(channel, "useage !potset <white> <red> <blue> <legend>" );
					return;
				}
				try
				{
					b = Integer.parseInt( args[3] );
				}
				catch (Exception e)
				{
					sendMessage(channel, "useage !potset <white> <red> <blue> <legend>" );
					return;
				}
				try
				{
					l = Integer.parseInt( args[4] );
				}
				catch (Exception e)
				{
					sendMessage(channel, "useage !potset <white> <red> <blue> <legend>" );
					return;
				}
				pot.setChips( w, r, b, l );
				sendMessage(channel, "Chips set." );
    	  	}
    	  	else if ( args[0].equalsIgnoreCase("!inventory") )
    	  	{
    	  		sendMessage(channel, pot.playerStatus(sender) );
    	  	}
    	   	else if ( args[0].equalsIgnoreCase("!currentgm") || args[0].equalsIgnoreCase("!gmlist") )
    	   	{
    	   		sendMessage(channel, listGm() );
    	   	}
			else if( args[0].equalsIgnoreCase("!sr") )
			{
				String result = shadowRoller( message, sender );
				if ( result.startsWith("Useage") )
					sendMessage( channel, result );
				else
					sendAction( channel, result );
                        }
			else if( args[0].equalsIgnoreCase("!exalted") )
			{
				String result = Dice.exaltRoller( message, sender );
				if ( result.startsWith("Useage") )
					sendMessage( channel, result );
				else
					sendAction( channel, result );
			}
			else if ( args[0].equalsIgnoreCase("!wild") )
			{
				String result = wildRoller( message, sender );
				if ( result.startsWith("Use") )
					sendMessage( channel, result );
				else
					sendAction( channel, result );
			}
			else if ( args[0].equalsIgnoreCase("!sw") )
			{
				String result = swRoller( message, sender );
				if ( result.startsWith("Use") )
					sendMessage( channel, result );
				else
					sendAction( channel, result );
			}
			else if ( args[0].equalsIgnoreCase("!swarm") )
			{
				String result = swSwarm( message, sender );
				if ( result.startsWith("Use") )
					sendMessage( channel, result );
				else
					sendAction( channel, result );
			}
			else if ( args[0].equalsIgnoreCase("!notify") )
			{
				if (args.length<2)
				{
					sendMessage(channel, "Useage:  !notify yes/no");
					return;
				}
				if ( args[1].equalsIgnoreCase("yes") || args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") )
				{
					if (!hasProfile(sender))
						addProfile(sender);
					setNotice(sender,true);
					sendMessage(channel, "Notification turned on for " + sender);
					saveProfiles();
					return;
				}
				if (args[1].equalsIgnoreCase("no") || args[1].equalsIgnoreCase("0") || args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("off") )
				{
					if (!hasProfile(sender))
						addProfile(sender);
					setNotice(sender,false);
					sendMessage(channel, "Notification turned off for " + sender);
					saveProfiles();
					return;
				}
			}
			else if ( args[0].equalsIgnoreCase("!magic8ball"))
			{
				sendMessage(channel,  magic8Ball() );
				return;
			}
			else if ( args[0].equalsIgnoreCase("!sound") )
			{
				if (args.length<2)
				{
					sendMessage(channel, "Useage:  !sound yes/no be sure to use !soundfile to set a soundfile to play");
					return;
				}
				if ( args[1].equalsIgnoreCase("yes") || args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") )
				{
					if (!hasProfile(sender))
						addProfile(sender);
					setSonic(sender,true);
					sendMessage(channel, "Sound notification turned on for " + sender+".  Be sure to use !soundfile to set a file to play.");
					saveProfiles();
					return;
				}
				if (args[1].equalsIgnoreCase("no") || args[1].equalsIgnoreCase("0") || args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("off") )
				{
					if (!hasProfile(sender))
						addProfile(sender);
					setSonic(sender,false);
					saveProfiles();
					sendMessage(channel, "Sound notification turned off for " + sender);
					return;
				}
			}
			else if ( args[0].equalsIgnoreCase("!soundfile") )
			{
				if (args.length<2)
				{
					sendMessage(channel, "Useage:  !soundfile filename.wav you must have the file in the appropraite folder for your IRC client to play.");
					return;
				}
				if (!hasProfile(sender))
					addProfile(sender);
				setSFile(sender,args[1]);
				sendMessage(channel, "Sound file for " + sender + " has been set.  If you are running mirc it should be located in your mirc/sound/ folder.");
				saveProfiles();
				return;
			}
			else if ( args[0].equalsIgnoreCase("!rollfor") )
			{
				if ( !isGm(sender) )
				{
					return;
				}
				if ( args.length < 2 )
				{
					sendMessage( channel, "Useage:  !rollfor <table>" );
					return;
				}
				else
				{
					String s = args[1];
					RollSort x = new RollSort(s);
					addTable(x);
					sendMessage( channel, sender + " has requested a roll for " + s + "." );
				}
			}
			else if ( args[0].equalsIgnoreCase("!rollmod") )
			//more of this could probably move into a function
			{
				message = message.trim();
				int roll = 0;
				int m = 0;
				String tableName = "";
				int tableLoc = 0;
				String result = roller( message, sender );
				if ( result.startsWith("Use") )
				{
					if ( !dStart )
					{
						sendMessage( channel, result );
						return;
					}
					else
						return;
				}
				else
				{
					try
					{
						Integer.parseInt( args[1] );
						tableLoc = 3;
					}
					catch (Exception e)
					{
						tableLoc = 2;
					}
					if ( args.length > tableLoc )
					{
						tableName = args[tableLoc];
						m = getMod(args[tableLoc-1]);
					}
					else
					{
						tableLoc = 0;
					}
					args = result.split(" ");
					try
					{
						roll = Integer.parseInt(args[args.length-1]);
					}
					catch(Exception e)
					{
						tableLoc = 0;
					}
					if ( tableLoc == 0 )
						sendAction( channel, result );
					else if ( !hasTable(tableName) )
					{
						sendAction( channel, result );
						return;
					}
					else
					{
						RollValue r = new RollValue( sender, roll, m );
						RollSort x = getTable(tableName);
						x.addValue(r);
						x.sortList();
						addTable(x);
						sendAction( channel, result + " added to " + tableName );
					}
				}
			}
//			else if ( args[0].equalsIgnoreCase("!hinit") )
			else if ( args[0].equalsIgnoreCase("!edit") && isGm(sender) )
			{
				int i = 0;
				if ( args.length < 4 )
				{
					sendMessage(channel, "Useage:  !edit <name> <new Roll> <list name>");
					return;
				}
				if ( !hasTable(args[3]) )
				{
					sendMessage(channel, "There is no list by that name.");
					return;
				}
				RollSort x = getTable(args[3]);
				if ( !x.hasName(args[1]) )
				{
					sendMessage(channel, "That name is not in the list.");
					return;
				}
				try
				{
					i = Integer.parseInt( args[2] );
				}
				catch (Exception e)
				{
					sendMessage(channel, "The new roll value must be an integer.");
					return;
				}
				RollValue value = x.getRollValue(args[1]);
				RollValue rep = new RollValue(args[1], i, value.getA(), value.getB(), value.getC() );
				x.addValue(rep);
				x.sortList();
				addTable(x);
				sendAction(channel, "applies " + sender + "'s edits to " + args[1] + "." );
				return;
			}
			else if ( args[0].equalsIgnoreCase("!scram") && isGm(sender) )
    		{
				sendAction( channel, "strikes a pose before vanishing in a puff of smoke." );
				partChannel( channel, "Feel free to /invite me back if you need me!" );
			}
			else if ( args[0].equalsIgnoreCase("!fudge") )
			{
				String mod = "";
				if ( args.length < 2 )
					mod = "Mediocre";
				else
					mod = args[1];
				sendAction( channel, Fudge.roll( sender, mod ) );
			}
			else if ( args[0].equalsIgnoreCase("!fate") )
			{
				String mod = "";
				if ( args.length < 2 )
					mod = "Mediocre";
				else
					mod = args[1];
				sendAction( channel, Fate.roll( sender, mod ) );
			}
			else if (  args[0].equalsIgnoreCase("!list") && isGm(sender) )
			{
				if ( args.length < 2 )
				{
					sendMessage( channel, "Useage:  !list <table>" );
				}
				else
				{
					String s = args[1];
					if (!hasTable(s))
					{
						sendMessage( channel, "Sorry, that table does not exist");
						return;
					}
					RollSort x = getTable(s);
					x.sortList();
					String[] output = x.getList();
					sendMessage( channel, "List for " + s + ":");
					for ( int i = 0; i < output.length; i++)
					{
						sendMessage( channel, output[i] );
					}
				}
			}
			else if ( args[0].equalsIgnoreCase("!hand") )
			{
				int[] hand = new int[0];
				boolean found = false;
				String report = "";
				if ( args.length < 2 )
				{
					for (int i = 0; i < decks.size(); i++)
					{
						Deck deck = decks.get(i);
						if ( deck.getName().equals( "Master" ) )
						{
							i = decks.size();
							found = true;
							hand = deck.getHand("sender");
						}
					}
				}
				else
				{
					for (int i = 0; i < decks.size(); i++)
					{
						Deck deck = decks.get(i);
						if ( deck.getName().equals( args[1] ) )
						{
							i = decks.size();
							found = true;
							hand = deck.getHand("sender");
						}
					}
				}
				if ( !found )
				{
					sendMessage(channel, "Deck not found.");
					return;
				}

				if ( hand.length < 1 )
				{
					sendMessage(channel, sender + " has no cards." );
					return;
				}
				for ( int i = 0; i < hand.length; i++)
				{
					report = report + " " + Deck.cardName(hand[i]);
				}
				sendMessage(channel, sender + " currently has in his hand" + report );
			}
			else if ( args[0].equalsIgnoreCase("!dealup") )
			{
				if ( !isGm(sender) )
					return;
				String deck = "Master";
				boolean found = false;
				Deck d = new Deck();
				int pos = 0;
				String report = "";
				if ( args.length < 2 )
				{
					sendMessage(channel, "Useage: !dealup name [deck]" );
					return;
				}
				String victim = args[1];
				if ( args.length > 2 )
					deck = args[2];
				for (int i = 0; i < decks.size(); i++)
				{
					d = decks.get(i);
					if ( d.getName().equals(deck) )
					{
						pos = i;
						i = decks.size();
						found = true;
					}
				}
				if ( !found )
				{
					sendMessage(channel, "Deck not found.");
					return;
				}
				else if ( args[1].equalsIgnoreCase("all") )
				{
					d.dealAll();
					String [] reports = d.listPlayers();
					sendMessage(channel, "Cardlist for deck " + deck);
					for ( int i = reports.length-1; i > -1; i--)
					{
						sendMessage(channel, reports[i] );
					}
					decks.set(pos, d);
					return;
				}
				else if ( !d.hasPlayer(victim) )
				{
					sendMessage(channel, "Adding " + victim + " to " + deck + " deck.");
					d.addPlayer(victim);
				}
				report = d.deal(victim);
				sendAction(channel, report);
				decks.set(pos, d);
			}
			else if ( args[0].equalsIgnoreCase("!deckadd") )
			{
				if ( !isGm(sender) )
					return;
				String deck = "Master";
				int pos = -1;
				Deck d = new Deck();
				if ( args.length < 2 )
				{
					sendMessage( channel, "Useage:  !deckadd <player> [deck]" );
					return;
				}
				else if ( args.length > 2 )
				{
					deck = args[2];
				}
				for (int i = 0; i < decks.size(); i++)
				{
					d = decks.get(i);
					if ( d.getName().equals(deck) )
					{
						pos = i;
						i = decks.size();
					}
				}
				if ( pos < 0 )
				{
					sendMessage(channel, "Deck not found.");
					return;
				}
				d.addPlayer(args[1]);
				decks.set(pos, d);
				sendMessage( channel, args[1] + " added to " + deck + ".");
			}
			else if ( args[0].equalsIgnoreCase("!deckremove") )
			{
				if ( !isGm(sender) )
					return;
				String deck = "Master";
				int pos = -1;
				Deck d = new Deck();
				if ( args.length < 2 )
				{
					sendMessage( channel, "Useage:  !deckremove <player> [deck]" );
					return;
				}
				else if ( args.length > 2 )
				{
					deck = args[2];
				}
				for (int i = 0; i < decks.size(); i++)
				{
					d = decks.get(i);
					if ( d.getName().equals(deck) )
					{
						pos = i;
						i = decks.size();
					}
				}
				if ( pos < 0 )
				{
					sendMessage(channel, "Deck not found.");
					return;
				}
				else if ( !d.hasPlayer( args[1] ) )
				{
					sendMessage(channel, args[1] + " is not assigned to this deck." );
					return;
				}
				d.removePlayer(args[1]);
				decks.set(pos, d);
				sendMessage( channel, args[1] + " removed from " + deck + ".");
			}
			else if ( args[0].equalsIgnoreCase("!deckreset") )
			{
				if ( !isGm(sender) )
					return;
				Deck d = new Deck();
				String deck = "Master";
				int pos = -1;
				if ( args.length > 1 )
					deck = args[1];
				for (int i = 0; i < decks.size(); i++)
				{
					d = decks.get(i);
					if ( d.getName().equals(deck) )
					{
						pos = i;
						i = decks.size();
					}
				}
				if ( pos < 0 )
				{
					sendMessage(channel, "Deck not found.");
					return;
				}
				Deck newDeck = new Deck( d.getName() );
				decks.set( pos, newDeck );
				sendMessage( channel, "Deck " + d.getName() + " reset." );
			}
			else if ( args[0].equalsIgnoreCase("!collect" ) )
			{
				String victim = "";
				String deckname = "Master";
				int pos = 0;
				boolean found = false;
				Deck d = new Deck();
				String report = "";
				if (args.length < 2)
				{
					sendMessage(channel, "Useage: !collect <target> [deck] typing all will collect from everyone.");
					return;
				}
				victim = args[1];
				if (args.length > 2 )
				{
					deckname = args[2];
				}
				for (int i = 0; i < decks.size(); i++)
				{
					d = decks.get(i);
					if (d.getName().equals(deckname))
					{
						pos = i;
						i = decks.size();
						found = true;
					}
				}
				if ( !found )
				{
					sendMessage( channel, "Deck not found.");
					return;
				}
				if (victim.equalsIgnoreCase("all"))
				{
					d.collect();
					decks.set(pos, d);
					sendAction( channel, "collects all cards for deck " + deckname);
				}
				else
				{
					d.collect(victim);
					decks.set(pos, d);
					sendAction( channel, "collects " + victim + "'s cards for deck " + deckname);
				}
			}
			else if ( args[0].equalsIgnoreCase("!cardlist" ) )
			{
				String deck = "Master";
				Deck d = new Deck();
				boolean found = false;
				if ( args.length > 1 )
					deck = args[1];
				for (int i = 0; i < decks.size(); i++)
				{
					d = decks.get(i);
					if ( d.getName().equals(deck) )
					{
						found = true;
						i = decks.size();
					}
				}
				if ( !found )
				{
					sendMessage(channel, "Deck not found." );
					return;
				}
				String [] report = d.listPlayers();
				sendMessage(channel, "Cardlist for deck " + deck);
				for ( int i = report.length-1; i > -1; i--)
				{
					sendMessage(channel, report[i] );
				}
			}
			else if ( args[0].equalsIgnoreCase("!decklist" ) )
			{
				String deck = "Master";
				Deck d = new Deck();
				boolean found = false;
				if ( args.length > 1 )
					deck = args[1];
				for (int i = 0; i < decks.size(); i++)
				{
					d = decks.get(i);
					if ( d.getName().equals(deck) )
					{
						found = true;
						i = decks.size();
					}
				}
				if ( !found )
				{
					sendMessage(channel, "Deck not found." );
					return;
				}
				String [] report = d.listPlayers();
				sendMessage(channel, "Cardlist for deck " + deck);
				for ( int i = report.length-1; i > -1; i--)
				{
					sendMessage(channel, report[i] );
				}
			}
			else if ( args[0].equalsIgnoreCase("!shuffle") )
			{
				Deck d = new Deck();
				int pos = 0;
				String deck = "Master";
				boolean found = false;
				if ( args.length > 1 )
					deck = args[1];
				for ( int i = 0; i < decks.size(); i++ )
				{
					d = decks.get(i);
					if ( d.getName().equals(deck) )
					{
						found = true;
						pos = i;
						i = decks.size();
					}
				}
				if ( !found )
				{
					sendMessage( channel, "Deck not found.");
					return;
				}
				d.shuffle();
				decks.set(pos, d);
				sendAction(channel, "shuffles deck " + deck);
			}
			else if ( args[0].equalsIgnoreCase("!chargen") )
			{
				String type;
				if ( args.length < 2 )
					type = "";
				else
					type = args[1];
				String[] results = genChar(type);
				for (int i = 0; i < results.length; i++)
				{
					sendMessage(channel, results[i]);
				}
			}
			else if ( args[0].equalsIgnoreCase("!makedeck") )
			{
				if (args.length < 2 )
				{
					sendMessage(channel, "Useage:  !makedeck <deckname>");
					return;
				}
				for ( int i = 0; i < decks.size(); i++ )
				{
					Deck d = decks.get(i);
					if ( d.getName().equalsIgnoreCase(args[1]) )
					{
						sendMessage(channel, "Deck " + args[1] + " already exists." );
						return;
					}
				}
				Deck newDeck = new Deck(args[1]);
				decks.add(newDeck);
				sendMessage( channel, "Deck " + args[1] + " created.");
			}
			else if ( args[0].equalsIgnoreCase("!remove") )
			{
				if ( args.length < 3 )
				{
					sendMessage(channel, "Useage:  !remove <name> <list>");
					return;
				}
				if ( !hasTable(args[2]) )
				{
					sendMessage(channel, "That list does not exist.");
					return;
				}
				RollSort x = getTable(args[2]);
				if ( !x.hasName(args[1]) )
				{
					sendMessage(channel, "That name is not on the list.");
					return;
				}
				x.removeValue(args[1]);
				addTable(x);
				sendAction(channel, "removes " + args[1] + " from " + args[2] + "." );
			}
			else if ( args[0].equalsIgnoreCase("!newchar") )
			{
				int game;
				if (args.length < 2 )
				{
					sendMessage(channel, "Useage: !newchar <game version> version can be 3E 4E SR SW (SW stands for savage worlds, not starwars)");
					return;
				}
				else
				{
					if ( args[1].equalsIgnoreCase("3e") )
						game = 0;
					else if ( args[1].equalsIgnoreCase("4e") )
						game = 1;
					else if ( args[1].equalsIgnoreCase("SR") )
						game = 2;
					else if ( args[1].equalsIgnoreCase("SW") )
						game = 3;
					else
					{
						sendMessage(channel, "That is not a valid game type, use either 3E 4E SR or SW");
						return;
					}
				}
				ExpTot character = new ExpTot(sender, game);
				addTotal(character);
				saveTotals();
				sendMessage(channel, "Character created for " + sender );
				return;
			}
			else if ( args[0].equalsIgnoreCase("!advance") )
			{
				if ( !hasTotal(sender) )
				{
					sendMessage(channel, "There is no character file for you, use !newchar first");
					return;
				}
				else
				{
					if ( args.length < 2 )
					{
						sendMessage(channel, "Useage: !advance <experience amount> character is determined by nick");
						return;
					}
					else
					{
						int exp = getInt(args[1]);
						if (exp == 0)
						{
							sendMessage(channel, "Argument must be a non 0 whole number.");
							return;
						}
						ExpTot player = getTotal(sender);
						int total = player.advance(exp);
						int level = player.getLevel();
						int game = player.getGame();
						if ( game != 2 )
						{
							sendMessage(channel, sender + " advanced " + exp + " experience points for a total of " + total + ".  Current level:  "+ level + " TNL:  " + player.tnl() );
						}
						else
							sendMessage(channel, sender + " gets " + exp + " karma for a total of " + total );
						addTotal(player);
						saveTotals();
					}
				}
			}
			else if ( args[0].equalsIgnoreCase("!deposit") )
			{
				if ( !hasTotal(sender) )
				{
					sendMessage(channel, "There is no character file for you, use !newchar first");
					return;
				}
				else
				{
					if ( args.length < 2 )
					{
						sendMessage(channel, "Useage: !deposit <amount> character is determined by nick");
						return;
					}
					else
					{
						double money = getDouble(args[1]);
						if (money == 0)
						{
							sendMessage(channel, "Argument must be a non 0 number.");
							return;
						}
						ExpTot player = getTotal(sender);
						double total = player.deposit(money);
						addTotal(player);
						saveTotals();
						String currency = "";
						if ( player.getGame() < 2 )
							currency = " gp";
						if ( player.getGame() == 2 )
							currency = " nuyen";
						sendMessage(channel, sender + " deposits " + money + currency + ".   Total Funds: " + total + currency );
						return;
					}
				}
			}
			else if ( args[0].equalsIgnoreCase("!spend") )
			{
				if ( !hasTotal(sender) )
				{
					sendMessage(channel, "There is no character file for you, use !newchar first");
					return;
				}
				else
				{
					if ( args.length < 2 )
					{
						sendMessage(channel, "Useage: !spend <amount> character is determined by nick");
						return;
					}
					else
					{
						double money = getDouble(args[1]);
						if (money == 0)
						{
							sendMessage(channel, "Argument must be a non 0 number.");
							return;
						}
						ExpTot player = getTotal(sender);
						double total = player.deduct(money);
						addTotal(player);
						saveTotals();
						String currency = "";
						if ( player.getGame() < 2 )
							currency = " gp";
						if ( player.getGame() == 2 )
							currency = " nuyen";
						sendMessage(channel, sender + " spends " + money + currency + ".  Total Funds: " + total + currency );
						return;
					}
				}
			}
			else if ( args[0].equalsIgnoreCase("!deduct") )
			{
				if ( !hasTotal(sender) )
				{
					sendMessage(channel, "There is no character file for you, use !newchar first");
					return;
				}
				else
				{
					if ( args.length < 2 )
					{
						sendMessage(channel, "Useage: !spend <amount> character is determined by nick");
						return;
					}
					else
					{
						double money = getDouble(args[2]);
						if (money == 0)
						{
							sendMessage(channel, "Argument must be a non 0 number.");
							return;
						}
						ExpTot player = getTotal(sender);
						double total = player.deduct(money);
						addTotal(player);
						saveTotals();
						String currency = "";
						if ( player.getGame() < 2 )
							currency = " gp";
						if ( player.getGame() == 2 )
							currency = " nuyen";
						sendMessage(channel, sender + " deducts " + money + currency + ".  Total Funds: " + total + currency );
						return;
					}
				}
			}
			else if ( args[0].equalsIgnoreCase("!ct") )
			{
				if (args.length < 3 )
				{
					sendMessage(channel, "Useage:  !ct <skill> <base>" );
					return;
				}
				if ( !isInt(args[1]))
				{
					sendMessage(channel, "Useage:  !ct <skill> <base>");
					return;
				}
				if ( !isInt(args[2]))
				{
					sendMessage(channel, "Useage:  !ct <skill> <base>");
					return;
				}
				sendAction(channel, cTRoller(getInt(args[1]), getInt(args[2]), sender));
			}
			else if ( args[0].equalsIgnoreCase("!gettotals") || args[0].equalsIgnoreCase("!totals") || args[0].equalsIgnoreCase("!status") )
			{
				if ( !hasTotal(sender) )
				{
					sendMessage(channel, "There is no character file for you, use !newchar first");
					return;
				}
				else
				{
					ExpTot player = getTotal(sender);
					sendMessage(channel, player.report());
				}
			}
		}
    }
	public void onPrivateMessage(String sender, String login, String hostname, String message)
	//same general deal as the last function
	//a few things that were gm only for the channel message
	//are legal through this since they don't spam
    {
    	String[] args = message.split(" ");
        if( message.startsWith("d") )
        {
			message = "!roll " + message.trim();
			int roll = 0;
			String tableName = "";
			int tableLoc = 0;
			String result = roller( message, sender );
			if ( result.startsWith("Use") )
				return;
			else
			{
				sendAction( sender, result );
			}
       	}
   	    else if ( args[0].equalsIgnoreCase("!aarnrace") )
   	   	{
   	    	sendMessage( sender, "Your randomly determined race is: " + aarnRace( false, false, "" ) );
   	    }
    	else if ( args[0].equalsIgnoreCase("!currentgm") )
    	{
    	   		sendMessage(sender, listGm() );
    	}
		else if ( args[0].equalsIgnoreCase("!join"))
		{
			if ( args.length < 2 )
			{
    			sendMessage( sender, "!join [Channel]" );
    			return;
			}
			joinChannel( args[1] );
			sendAction( args[1], getJoin(sender) );
		}
       	else if ( args[0].equalsIgnoreCase("!stats") )
       	{
       		int first=1;
       		int second=1;
       		boolean idiot = false;
       		if ( args.length < 3 )
       		{
       			sendMessage( sender, "Useage:  !stats <size of dice> <number of rolls>" );
       		}
       		else
       		{
				try
				{
					first = Integer.parseInt( args[1] );
					second = Integer.parseInt( args[2] );
				}
				catch (Exception e)
				{
					idiot = true;
					sendMessage( sender, "Useage:  !stats <size of dice> <number of rolls>" );
				}
				if (idiot)
				{
					sendMessage( sender, "Useage:  !stats <size of dice> <number of rolls>" );
				}
				else
				{
					sendMessage( sender, Dice.statRun(first, second) );
				}
       		}
       	}
       	else if ( args[0].equalsIgnoreCase("!value") )
       	{
       		sendMessage( sender, pointValue(args) );
       	}
      	else if ( args[0].equalsIgnoreCase("!inventory") )
      	{
      		sendMessage(sender, pot.playerStatus(sender) );
      	}
      	else if ( args[0].equalsIgnoreCase("!potstatus") )
      	{
      		sendMessage( sender, pot.chipCount() );
      	}
      	else if ( args[0].equalsIgnoreCase("!fudge") )
		{
			String mod = "";
			if ( args.length < 2 )
				mod = "Mediocre";
			else
				mod = args[1];
			sendAction( sender, Fudge.roll( sender, mod ) );
		}
      	else if ( args[0].equalsIgnoreCase("!fate") )
		{
			String mod = "";
			if ( args.length < 2 )
				mod = "Mediocre";
			else
				mod = args[1];
			sendAction( sender, Fate.roll( sender, mod ) );
		}
    	else if ( args[0].equalsIgnoreCase("!sr") )
    	{
  			String result = shadowRoller( message, sender );
  			if ( result.startsWith("Use") )
   				sendMessage(sender, result );
   			else
   				sendAction(sender, result);
   	}
    	else if ( args[0].equalsIgnoreCase("!exalted") )
    	{
  			String result = Dice.exaltRoller( message, sender );
  			if ( result.startsWith("Use") )
   				sendMessage(sender, result );
   			else
   				sendAction(sender, result);
   	}
		else if ( args[0].equalsIgnoreCase("!soundfile") )
		{
			if (args.length<2)
			{
				sendMessage(sender, "Useage:  !soundfile filename.wav you must have the file in the appropraite folder for your IRC client to play.");
				return;
			}
			if (!hasProfile(sender))
				addProfile(sender);
			setSFile(sender,args[1]);
			sendMessage(sender, "Sound file for " + sender + " has been set.  If you are running mirc it should be located in your mirc/sound/ folder.");
			saveProfiles();
			return;
		}
    	else if ( args[0].equalsIgnoreCase("!roll"))
    	{
    		String daMessage = "";
    		int total = 0;
    		if ( args.length < 2 )
    		{
    			sendMessage( sender, "!roll [rolls] [Dice Equation] <Table>  xdy rolls x dice size y.  e denotes an exploding dice (Max value rerolled). xey." );
    			return;
    		}
    		if ( args.length > 2 && isInt(args[1]) )
    		{
    			if ( !isEquation( args[2] ) )
    			{
    				sendMessage( sender, "!roll [rolls] [Dice Equation] <table>  xdy rolls x dice size y.  e denotes an exploding dice (Max value rerolled). xey." );
    				return;
    			}
    			String tally = "";
    			int i = 0;
    			int gwah = getInt( args[1] );
    			while ( gwah > 0 )
    			{
					i=parseOne(args[2]);
					total+=i;
					tally=tally+" "+i;
					gwah--;
    			}
    			daMessage = "rolled " + args[2] + " for " + sender + " and got (" + tally + " ) for a total of: " + total;
    			if ( args.length > 3 )
    			{
    				if ( !hasTable(args[3]) )
					{
						sendAction( sender, daMessage );
						return;
					}
					else
					{
						RollValue r = new RollValue( sender, total, 0 );
						RollSort x = getTable(args[3]);
						x.addValue(r);
						x.sortList();
						addTable(x);
						sendAction( sender, daMessage + " added to " + args[3] );
						return;
					}
    			}
    			sendAction( sender, daMessage );
    			return;
    		}
    		if ( isEquation(args[1]) )
    		{
    			total = parseOne(args[1]);
    		}
    		else
    		{
    			sendMessage( sender, "!roll [rolls] [Dice Equation] <Table>  xdy rolls x dice size y.  e denotes an exploding dice (Max value rerolled). xey." );
    		}
			daMessage = "rolled " + args[1] + " for " + sender + " and got " + total;
			if ( args.length > 2 )
			{
				if ( !hasTable(args[2]) )
				{
					sendAction( sender, daMessage );
					return;
				}
				else
				{
					RollValue r = new RollValue( sender, total, 0 );
					RollSort x = getTable(args[2]);
					x.addValue(r);
					x.sortList();
					addTable(x);
					sendAction( sender, daMessage + " added to " + args[2] );
					return;
				}
			}
			sendAction( sender, daMessage );
			return;
    	}
    	else if ( args[0].equalsIgnoreCase("!hd") )
    	{
    		if (args.length <2 )
    		{
    			sendMessage( sender, "!hd [number of dice] [modifier] accepts half dice values such as 4.5.  Teal die is half die." );
    			return;
    		}
    		else if ( args.length < 3 )
    		{
    			sendAction( sender, Dice.heroDamage(getDouble(args[1]), 0, sender ) );
    			return;
    		}
    		else
    		{
    			sendAction( sender, Dice.heroDamage(getDouble(args[1]), getInt(args[2]), sender ) );
    			return;
    		}
    	}
    	else if ( args[0].equalsIgnoreCase("!ha") )
    	{
    		if ( args.length < 2 )
    		{
    			sendMessage( sender, "!ha [OCV] Rolls an attack using the HERO system.");
    			return;
    		}
    		else
    		{
    			sendAction( sender, Dice.heroAttack( getInt(args[1]), sender )  );
    			return;
    		}
    	}
	   	else if ( args[0].equalsIgnoreCase("!wg") )
    	{
    		if ( args.length < 2 )
    		{
    			sendMessage( sender, "!wg [number of dice] [modifier] Makes a roll using Westend Games d6 system." );
    			return;
    		}
    		else if ( args.length < 3 )
    		{
    			int dice = getInt(args[1]);
    			String output = Dice.westDice(dice, 0, sender );
    			sendAction( sender, output );
    			return;
    		}
    		else
    		{
    			int dice = getInt(args[1]);
    			int mod = getInt(args[2]);
    			String output = Dice.westDice(dice, mod, sender );
    			sendAction( sender, output );
    			return;
    		}
    	}
		else if ( args[0].equalsIgnoreCase("!notify") )
		{
			if (args.length<2)
			{
				sendMessage(sender, "Useage:  !notify yes/no");
				return;
			}
			if ( args[1].equalsIgnoreCase("yes") || args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") )
			{
				if (!hasProfile(sender))
					addProfile(sender);
				setNotice(sender,true);
				sendMessage(sender, "Notification turned on for " + sender);
				saveProfiles();
				return;
			}
			if (args[1].equalsIgnoreCase("no") || args[1].equalsIgnoreCase("0") || args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("off") )
			{
				if (!hasProfile(sender))
					addProfile(sender);
				setNotice(sender,false);
				sendMessage(sender, "Notification turned off for " + sender);
				saveProfiles();
				return;
			}
		}
		else if ( args[0].equalsIgnoreCase("!sound") )
		{
			if (args.length<2)
			{
				sendMessage(sender, "Useage:  !sound yes/no be sure to use !soundfile to set a soundfile to play");
				return;
			}
			if ( args[1].equalsIgnoreCase("yes") || args[1].equalsIgnoreCase("1") || args[1].equalsIgnoreCase("true") || args[1].equalsIgnoreCase("on") )
			{
				if (!hasProfile(sender))
					addProfile(sender);
				setSonic(sender,true);
				sendMessage(sender, "Sound notification turned on for " + sender+".  Be sure to use !soundfile to set a file to play.");
				return;
			}
			if (args[1].equalsIgnoreCase("no") || args[1].equalsIgnoreCase("0") || args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("off") )
			{
				if (!hasProfile(sender))
					addProfile(sender);
				setSonic(sender,false);
				sendMessage(sender, "Sound notification turned off for " + sender);
				return;
			}
		}
   		else if ( args[0].equalsIgnoreCase("!npc"))
   	    {
   	    	if ( args.length < 4 || !isRoll(args[2]) || !hasTable(args[3]) )
   	    	{
   	    		sendMessage( sender, "Useage:  !npc <npc name> xdy+z <table name>" );
   	    		return;
   	    	}
   	    	RollSort x = getTable(args[3]);
   	    	int roll = rollerInt(args[2]);
   	    	x.addRoll(args[1], roll, getMod(args[2]));
   	    	x.sortList();
   	    	addTable(x);
   	    	sendAction(sender, "rolled a (" + args[2] + ") for " + args[1] + " and got: " + roll + " added to " + args[3] );
   	    }
		else if ( args[0].equalsIgnoreCase("!wild") )
		{
			String result = wildRoller( message, sender );
			if ( result.startsWith("Use") )
				sendMessage( sender, result );
			else
				sendAction( sender, result );
		}
		else if ( args[0].equalsIgnoreCase("!sw") )
		{
			String result = swRoller( message, sender );
			if ( result.startsWith("Use") )
				sendMessage( sender, result );
			else
				sendAction( sender, result );
		}
		else if ( args[0].equalsIgnoreCase("!swarm") )
		{
			String result = swSwarm( message, sender );
			if ( result.startsWith("Use") )
				sendMessage( sender, result );
			else
				sendAction( sender, result );
		}
		else if ( args[0].equalsIgnoreCase("!rollmod") )
		{
			message = message.trim();
			int roll = 0;
			String tableName = "";
			int tableLoc = 0;
			int m = 0;
			String result = roller( message, sender );
			if ( result.startsWith("Use") )
				sendMessage( sender, result );
			else
			{
				try
				{
					Integer.parseInt( args[1] );
					tableLoc = 3;
				}
				catch (Exception e)
				{
					tableLoc = 2;
				}
				if ( args.length > tableLoc )
				{
					tableName = args[tableLoc];
					m = getMod(args[tableLoc-1]);
				}
				else
				{
					tableLoc = 0;
				}
				args = result.split(" ");
				try
				{
					roll = Integer.parseInt(args[args.length-1]);
				}
				catch(Exception e)
				{
					tableLoc = 0;
				}
				if ( tableLoc == 0 )
					sendAction( sender, result );
				else if ( !hasTable(tableName) )
				{
					sendAction( sender, result );
					return;
				}
				else
				{
					RollValue r = new RollValue( sender, roll, m );
					RollSort x = getTable(tableName);
					x.addValue(r);
					x.sortList();
					addTable(x);
					sendAction( sender, result + " added to " + tableName );
				}
			}

		}
		else if (  args[0].equalsIgnoreCase("!list") )
		{
			if ( args.length < 2 )
			{
				sendMessage( sender, "Useage:  !list <table>" );
			}
			else
			{
				String s = args[1];
				if (!hasTable(s))
				{
					sendMessage( sender, "Sorry, that table does not exist");
					return;
				}
				RollSort x = getTable(s);
				x.sortList();
				String[] output = x.getList();
				sendMessage( sender, "List for " + s + ":");
				for ( int i = 0; i < output.length; i++)
				{
					sendMessage( sender, output[i] );
				}
				return;
			}
		}
		else if ( args[0].equalsIgnoreCase("!hand") )
		{
			int[] hand = new int[0];
			boolean found = false;
			String report = "";
			if ( args.length < 2 )
			{
				for (int i = 0; i < decks.size(); i++)
				{
					Deck deck = decks.get(i);
					if ( deck.getName().equals( "Master" ) )
					{
						i = decks.size();
						found = true;
						hand = deck.getHand("sender");
					}
				}
			}
			else
			{
				for (int i = 0; i < decks.size(); i++)
				{
					Deck deck = decks.get(i);
					if ( deck.getName().equals( args[1] ) )
					{
						i = decks.size();
						found = true;
						hand = deck.getHand("sender");
					}
				}
			}
			if ( !found )
			{
				sendMessage(sender, "Deck not found.");
				return;
			}

			if ( hand.length < 1 )
			{
				sendMessage(sender, "You have no cards." );
				return;
			}
			for ( int i = 0; i < hand.length; i++)
			{
				report = report + " " + Deck.cardName(hand[i]);
			}
			sendMessage(sender, "You currently have" + report );
		}
		else if ( args[0].equalsIgnoreCase("!magic8ball"))
		{
			sendMessage(sender,  magic8Ball() );
			return;
		}
		else if ( args[0].equalsIgnoreCase("!ct") )
		{
			if (args.length < 3 )
			{
				sendMessage(sender, "Useage:  !ct <skill> <base>" );
				return;
			}
			if ( !isInt(args[1]))
			{
				sendMessage(sender, "Useage:  !ct <skill> <base>");
				return;
			}
			if ( !isInt(args[2]))
			{
				sendMessage(sender, "Useage:  !ct <skill> <base>");
				return;
			}
			sendAction(sender, cTRoller(getInt(args[1]), getInt(args[2]), sender));
		}
		else if ( args[0].equalsIgnoreCase("!cardlist") )
		{
			String deck = "Master";
			Deck d = new Deck();
			boolean found = false;
			if ( args.length > 1 )
				deck = args[1];
			for (int i = 0; i < decks.size(); i++)
			{
				d = decks.get(i);
				if ( d.getName().equals(deck) )
				{
					found = true;
					i = decks.size();
				}
			}
			if ( !found )
			{
				sendMessage(sender, "Deck not found." );
				return;
			}
			String [] report = d.listPlayers();
			sendMessage(sender, "Cardlist for deck " + deck);
			for ( int i = report.length-1; i > -1; i--)
			{
				sendMessage(sender, report[i] );
			}
		}
		else if ( args[0].equalsIgnoreCase("!cardlist") )
		{
			String deck = "Master";
			Deck d = new Deck();
			boolean found = false;
			if ( args.length > 1 )
				deck = args[1];
			for (int i = 0; i < decks.size(); i++)
			{
				d = decks.get(i);
				if ( d.getName().equals(deck) )
				{
					found = true;
					i = decks.size();
				}
			}
			if ( !found )
			{
				sendMessage(sender, "Deck not found." );
				return;
			}
			String [] report = d.listPlayers();
			sendMessage(sender, "Cardlist for deck " + deck);
			for ( int i = report.length-1; i > -1; i--)
			{
				sendMessage(sender, report[i] );
			}
		}
		else if ( args[0].equalsIgnoreCase("!chargen") )
		{
			String type;
			if ( args.length < 2 )
				type = "";
			else
				type = args[1];
			String[] results = genChar(type);
			for (int i = 0; i < results.length; i++)
			{
				sendMessage(sender, results[i]);
			}
		}
   	}
   	public void onInvite( String targetNick, String sourceNick, String sourceLogin, String sourceHostname, String channel )
   	{
   		joinChannel( channel );
   		sendAction( channel, getJoin(sourceNick) );
   	}
   	//returns true if format is xdy+z
   	public static boolean isRoll( String message )
   	{
   		message = message.toLowerCase();
   		String[] dice = new String[2];
   		if ( !message.startsWith("d") )
   		{
   			String[] stuff = message.split("d");
   			if ( stuff.length < 2 )
   				return false;
   			else
   			{
   				try
   				{
   					int i = Integer.parseInt( stuff[0] );
   				}
   				catch (Exception e)
   				{
   					return false;
   				}
   				message = "d" + stuff[1];
   			}
   		}
   		message = message.replace( "+", "�" );
   		if ( message.indexOf( "-" ) < 0 && message.indexOf( "�" ) < 0 )
   			message = message.concat( "�0" );
   		if ( message.indexOf( "�" ) > -1 )
   			dice = message.split("�");
   		else if ( message.indexOf( "-" ) > -1 )
   		{
   			dice = message.split("-");
   		}
   		if ( dice[0].length() < 2 )
   			return false;
   		dice[0] = dice[0].substring(1);
   		try
   		{
   			int i = Integer.parseInt( dice[0] );
   			i = Integer.parseInt( dice[1] );
   		}
   		catch (Exception e)
   		{
   			return false;
   		}
   		return true;
   	}
   	//and now for the rolling functions
   	public static String roller( String message, String sender)
   	//generic dice roller
   	{
   		message = message.trim();
   		String diceChain = "";
   		int amount = 1;
   		int i;
   		int die;
   		int[] rolls;
   		boolean isDice = true;
   		int mod = 0;
   		String[] args = message.split(" ");
   		String results = "rolled";
   		String text = "1";
   		int modMult = 1;
   		int total = 0;
   		int size = 0;
   		int passes;
   		int dLoc = 1;

   		if ( args.length == 1 )
   		{
   			return "Useage:  !roll [w] [x]dy[+z]";
   		}
   		try
   		{
   			passes = Integer.parseInt( args[1] );
   			if ( args.length == 2 )
   				return "Useage:  !roll [w] [x]dy[+z]";
   			dLoc = 2;
   		}
   		catch (Exception e)
   		{
   			passes = 1;
   			dLoc = 1;
   		}
   		if ( args[dLoc].startsWith("d") )
   			args[dLoc] = text.concat( args[dLoc] );
   		else if ( args[dLoc].indexOf( "d" ) < 0 )
   			return "Useage:  !roll [w] [x]dy[+z]";
   		if ( passes > 1 )
   			results = results.concat( " " + passes + " (" + args[dLoc] + ") for " + sender + " and got ( " );
   		else
   			results = results.concat( " a (" + args[dLoc] + ") for " + sender + " and got:" );
   		args[dLoc] = args[dLoc].replace( "+", "�" );
   		if ( args[dLoc].indexOf( "-" ) < 0 && args[dLoc].indexOf( "�" ) < 0 )
   			args[dLoc] = args[dLoc].concat( "�0" );
   		args = args[dLoc].split("d");
   		try
   		{
   			amount = Integer.parseInt( args[0] );
   		}
   		catch (Exception e)
   		{
   			return "Useage:  !roll [w] [x]dy[+z]";
   		}
   		if ( args[1].indexOf( "�" ) > -1 )
   			args = args[1].split("�");
   		else if ( args[1].indexOf( "-" ) > -1 )
   		{
   			args = args[1].split("-");
   			modMult = -1;
   		}
   		try
   		{
   			die = Integer.parseInt( args[0] );
   		}
   		catch(Exception e)
   		{
  			return "Useage:  !roll [w] [x]dy[+z]";
   		}
   		try
   		{
   			mod = Integer.parseInt( args[1] )*modMult;
   		}
   		catch(Exception e)
   		{
  			return "Useage:  !roll [w] [x]dy[+z]";
   		}
   		if (passes == 1)
   		{
   			total = Dice.rollDice( amount, die, mod );
   			results = results.concat(" " + total );
   			return results;
   		}
   		else
   		{
   			rolls = new int[passes];
   			for ( i = 0; i < rolls.length; i++ )
   			{
   				rolls[i] = Dice.rollDice( amount, die, mod );
   				diceChain = diceChain.concat( rolls[i] + " " );
   				total += rolls[i];
   			}
   			results = results.concat( diceChain + ") Total: " + total );
   			return results;
   		}
   	}
   	public static int getMod(String message)
   	//this gets z from a xdy+z string.  If the string lacks +z it will return 0  A non
   	//roll string will return 0
   	{
   		int mod;
   		message = message.replace( "+", "�" );
   		if ( message.indexOf( "-" ) < 0 && message.indexOf( "�" ) < 0 )
   			return 0;
   		if ( message.indexOf("-") < 0 )
   		{
   			String[] stuff = message.split("�");
   			try
   			{
   				mod = Integer.parseInt(stuff[1]);
   				return mod;
   			}
   			catch (Exception e)
   			{
   				return 0;
   			}
   		}
   		else
   		{
   			String[] stuff = message.split("-");
   			try
   			{
   				mod = 0-Integer.parseInt(stuff[1]);
   				return mod;
   			}
   			catch (Exception e)
   			{
   				return 0;
   			}
   		}
   	}
   	public static int rollerInt(String message)
   	//this rolls dice and returns the putput as a number only.
   	//bad input results in a 0
   	//do NOT use without checking with isRoll()
   	//does NOT handle passes xdy+z format only
   	{
   		message = message.trim();
   		int amount = 1;
   		int die;
   		int mod = 0;
   		String text = "1";
   		int modMult = 1;
   		int total = 0;
   		int size = 0;
   		message = message.toLowerCase();

   		if ( !isRoll(message) )
			return 0;
   		if ( !message.startsWith("d") )
   		{
   			String[] stuff = message.split("d");
	    	try
	   		{
	   			amount = Integer.parseInt( stuff[0] );
	   			message = stuff[1];
	   		}
	   		catch(Exception e)
	   		{
	  			return 0;
	   		}
   		}
   		else
   		{
   			message = message.substring(1);
   		}
   		message = message.replace( "+", "�" );
   		if ( message.indexOf( "-" ) < 0 && message.indexOf( "�" ) < 0 )
   			message = message.concat( "�0" );
   		if ( message.indexOf("-") < 0 )
   		{
   			String[] stuff = message.split("�");
   			try
   			{
   				die = Integer.parseInt( stuff[0] );
   				mod = Integer.parseInt( stuff[1] );
   			}
   			catch(Exception e)
   			{
   				return 0;
   			}
   		}
   		else
   		{
   			modMult = -1;
   			String[] stuff = message.split("-");
   			try
   			{
   				die = Integer.parseInt( stuff[0] );
   				mod = Integer.parseInt( stuff[1] );
   			}
   			catch(Exception e)
   			{
   				return 0;
   			}
   		}
   		return Dice.rollDice( amount, die, mod*modMult );
   	}
   	public static String swSwarm( String message, String sender)
   	{
   		//sw batch roller, for making a lof of attacks as a gm
   		// in savage worlds
   		message = message.trim();
   		String diceChain = "";
   		int amount = 1;
   		int i;
   		int die;
   		int[] rolls;
   		boolean isDice = true;
   		int mod = 0;
   		String[] args = message.split(" ");
   		String results = "rolled a ";
   		String text = "1";
   		int modMult = 1;
   		int total = 0;
   		int size = 0;

   		if ( args.length == 1 )
   		{
   			return "Useage:  [x]dy+z [raise]  Max value rolls are rerolled.";
   		}
   		if ( args[1].startsWith("d") )
   		{
   			args[1] = text.concat( args[1] );
   		}
   		else if ( args[1].indexOf( "d" ) < 0 )
   		{
   			return "Useage:  [x]dy+z [raise]  Max value rolls are rerolled.";
   		}
   		if ( message.endsWith("raise") )
   			results = results.concat( "(" + args[1] + ") with a raise for " + sender + " and got (" );
   		else
   			results = results.concat( "(" + args[1] + ") for " + sender + " and got (" );
   		args[1] = args[1].replace( "+", "�" );
   		if ( args[1].indexOf( "-" ) < 0 && args[1].indexOf( "�" ) < 0 )
   			args[1] = args[1].concat( "�0" );
   		args = args[1].split("d");
   		try
   		{
   			amount = Integer.parseInt( args[0] );
   		}
   		catch(Exception e)
   		{
  			return "Useage:  [x]dy+z [raise]  Max value rolls are rerolled.";
   		}
   		if ( args[1].indexOf( "�" ) > -1 )
   			args = args[1].split("�");
   		else if ( args[1].indexOf( "-" ) > -1 )
   		{
   			args = args[1].split("-");
   			modMult = -1;
   		}
   		try
   		{
   			die = Integer.parseInt( args[0] );
   		}
   		catch(Exception e)
   		{
  			return "Useage:  [x]dy+z [raise]  Max value rolls are rerolled.";
   		}
   		try
   		{
   			mod = Integer.parseInt( args[1] )*modMult;
   		}
   		catch(Exception e)
   		{
  			return "Useage:  [x]dy+z [raise]  Max value rolls are rerolled.";
   		}
   		if ( message.endsWith("raise") )
   			rolls = new int[amount+1];
   		else
   			rolls = new int[amount];
   		for ( i = 0; i < amount; i++ )
   		{
   			rolls[i] = Dice.rollAce( die ) + mod;
   		}
   		if ( message.endsWith("raise") )
   		{
   			rolls[amount] = Dice.rollAce( 6 );
   		}
   		for ( i = 0; i < rolls.length; i++ )
   		{
   			diceChain = diceChain.concat( " " + rolls[i] + "" );
   		}
   		results = results.concat( diceChain + " )" );
   		return results;
   	}
   	public static String fudge( String sender )
   	//fudge dice roller
   	{
   		//fudge dice have 6 sides and only 3 results
   		// - blank and +.  In retrospect, I probaly
   		//should not have gone puritan with 6 sided
   		//virtual dice, and used d3's but what's done
   		//is done
   		int d1 = Dice.rollDice(6);
   		int d2 = Dice.rollDice(6);
   		int d3 = Dice.rollDice(6);
   		int d4 = Dice.rollDice(6);
   		//now my dice have values
   		int result = 0;
   		//at first you have nothing
   		String message = "rolled for " + sender + " and got( ";
   		//from this point on it just addresses the dice one
   		//step at a time, concacting on the result and adjusting
   		//the total.
   		if ( d1 == 1 || d1 == 2 )
   		{
   			message = message + "- ";
   			result--;
   		}
   		else if ( d1 == 3 || d1 == 4 )
   		{
   			message = message + "0 ";
   		}
   		else if ( d1 == 5 || d1 == 6 )
   		{
   			message = message + "+ ";
   			result++;
   		}
   		if ( d2 == 1 || d2 == 2 )
   		{
   			message = message + "- ";
   			result--;
   		}
   		else if ( d2 == 3 || d2 == 4 )
   		{
   			message = message + "0 ";
   		}
   		else if ( d2 == 5 || d2 == 6 )
   		{
   			message = message + "+ ";
   			result++;
   		}
   		if ( d3 == 1 || d3 == 2 )
   		{
   			message = message + "- ";
   			result--;
   		}
   		else if ( d3 == 3 || d3 == 4 )
   		{
   			message = message + "0 ";
   		}
   		else if ( d3 == 5 || d3 == 6 )
   		{
   			message = message + "+ ";
   			result++;
   		}
   		if ( d4 == 1 || d4 == 2 )
   		{
   			message = message + "- ";
   			result--;
   		}
   		else if ( d4 == 3 || d4 == 4 )
   		{
   			message = message + "0 ";
   		}
   		else if ( d4 == 5 || d4 == 6 )
   		{
   			message = message + "+ ";
   			result++;
   		}
   		message = message + ") For a total of: " + result;
   		return message;
   	}
   	public static String swRoller( String message, String sender)
   	//savage worlds roller, also does salads and removes unsightly spots.
   	{
   		message = message.trim();
   		String diceChain = "";
   		int amount = 1;
   		int i;
   		int die;
   		int[] rolls;
   		boolean isDice = true;
   		int mod = 0;
   		String[] args = message.split(" ");
   		String results = "rolled a ";
   		String text = "1";
   		int modMult = 1;
   		int total = 0;
   		int size = 0;

   		if ( args.length == 1 )
   		{
   			return "Useage:  [x]dy+z [raise]  Max value rolls are rerolled.";
   		}
   		if ( args[1].startsWith("d") )
   		{
   			args[1] = text.concat( args[1] );
   		}
   		else if ( args[1].indexOf( "d" ) < 0 )
   		{
   			return "Useage:  [x]dy+z [raise]  Max value rolls are rerolled.";
   		}
   		if ( message.endsWith("raise") )
   			results = results.concat( "(" + args[1] + ") with a raise for " + sender + " and got (" );
   		else
   			results = results.concat( "(" + args[1] + ") for " + sender + " and got (" );
   		args[1] = args[1].replace( "+", "�" );
   		if ( args[1].indexOf( "-" ) < 0 && args[1].indexOf( "�" ) < 0 )
   			args[1] = args[1].concat( "�0" );
   		args = args[1].split("d");
   		try
   		{
   			amount = Integer.parseInt( args[0] );
   		}
   		catch(Exception e)
   		{
  			return "Useage:  [x]dy+z [raise]  Max value rolls are rerolled.";
   		}
   		if ( args[1].indexOf( "�" ) > -1 )
   			args = args[1].split("�");
   		else if ( args[1].indexOf( "-" ) > -1 )
   		{
   			args = args[1].split("-");
   			modMult = -1;
   		}
   		try
   		{
   			die = Integer.parseInt( args[0] );
   		}
   		catch(Exception e)
   		{
  			return "Useage:  [x]dy+z [raise]  Max value rolls are rerolled.";
   		}
   		try
   		{
   			mod = Integer.parseInt( args[1] )*modMult;
   		}
   		catch(Exception e)
   		{
  			return "Useage:  [x]dy+z [raise]  Max value rolls are rerolled.";
   		}
   		if ( message.endsWith("raise") )
   			rolls = new int[amount+1];
   		else
   			rolls = new int[amount];
   		for ( i = 0; i < amount; i++ )
   		{
   			rolls[i] = Dice.rollAce( die );
   		}
   		if ( message.endsWith("raise") )
   		{
   			rolls[amount] = Dice.rollAce( 6 );
   		}
   		for ( i = 0; i < rolls.length; i++ )
   		{
   			diceChain = diceChain.concat( " " + rolls[i] + "" );
   		}
   		results = results.concat( diceChain + " ) Total:" );
   		for ( i = 0; i < rolls.length; i++ )
   		{
			total += rolls[i];
   		}
   		total += mod;
   		results = results.concat( " " + total );
   		return results;
   	}
   	public void setGm(String handle)
   	{
   		String x = "";
   		for (int i = 0; i < gms.size(); i++)
		{
			x = gms.get(i);
			if( x.equalsIgnoreCase(handle) )
				return;
		}
		gms.add(handle);
   	}
    public boolean isGm(String handle)
    {
  		for (int i = 0; i < gms.size(); i++)
		{
			String x = gms.get(i);
			if( x.equalsIgnoreCase(handle) )
				return true;
		}
		return false;
    }
    public void setJoin(String joinMessage){
        //Slap an extra space on the end in case the % appears t the end of the string.
        joinEmote=joinMessage+" ";
    }
    public String getJoin(String sender){
        String[] joinArray = joinEmote.split("%");
        if ( joinArray.length < 2 )
        {
            return joinArray[0];
        }
        else
        return joinArray[0]+sender+joinArray[1];
    }
    public String listGm()
    {
    	String out = "The current GMs are: ";
    	for (int i = 0; i < gms.size(); i++)
		{
			out = out + " " + gms.get(i);
		}
		return out;
    }
   	public static String wildRoller( String message, String sender)
   	//savage worlds wildcard roller, adds the d6 wildcard die on automatically
   	//the lowest die is dropped
   	{
   		String diceChain = "";
   		int amount = 1;
   		int i;
   		int die;
   		int[] rolls;
   		boolean isDice = true;
   		int mod = 0;
   		String[] args = message.split(" ");
   		String results = "rolled a ";
   		String text = "1";
   		int modMult = 1;
   		int lowest = 2147483640;
   		//someone's gotta be very lucky to beat that number with all their rolls
   		int lowestPos = 0;

   		if ( args.length == 1 )
   		{
   			return "Useage:  [x]dy+z  All die are rolled seperately with a wild die wild replaces lowest if lower.";
   		}
   		if ( args[1].startsWith("d") )
   		{
   			args[1] = text.concat( args[1] );
   		}
   		else if ( args[1].indexOf( "d" ) < 0 )
   		{
   			return "Useage:  [x]dy+z  All die are rolled seperately with a wild die wild replaces lowest if lower.";
   		}
   		results = results.concat( "(" + args[1] + ") with wild die for " + sender + " and got (" );
   		args[1] = args[1].replace( "+", "�" );
   		if ( args[1].indexOf( "-" ) < 0 && args[1].indexOf( "�" ) < 0 )
   			args[1] = args[1].concat( "�0" );
   		args = args[1].split("d");
   		try
   		{
   			amount = Integer.parseInt( args[0] );
   		}
   		catch(Exception e)
   		{
  			return "Useage:  [x]dy+z  All die are rolled seperately with a wild die wild replaces lowest if lower.";
   		}
   		if ( args[1].indexOf( "�" ) > -1 )
   			args = args[1].split("�");
   		else if ( args[1].indexOf( "-" ) > -1 )
   		{
   			args = args[1].split("-");
   			modMult = -1;
   		}
   		try
   		{
   			die = Integer.parseInt( args[0] );
   		}
   		catch(Exception e)
   		{
  			return "Useage:  [x]dy+z  All die are rolled seperately with a wild die wild replaces lowest if lower.";
   		}
   		try
   		{
   			mod = Integer.parseInt( args[1] )*modMult;
   		}
   		catch(Exception e)
   		{
  			return "Useage:  [x]dy+z  All die are rolled seperately with a wild die wild replaces lowest if lower.";
   		}
   		rolls = new int[amount+1];
   		for ( i = 0; i < amount; i++ )
   		{
   			rolls[i] = Dice.rollAce( die, mod );
   		}
   		rolls[amount] = Dice.rollAce( 6, mod );
   		for ( i = 0; i < rolls.length; i++ )
   		{
   			if ( i == amount )
   				diceChain = diceChain.concat( "9" );
   			else if ( rolls[i] == 1+mod )
   			{
   				diceChain = diceChain.concat( "4" );
   			}
   			diceChain = diceChain.concat( " " + rolls[i] + "" );
   			if ( rolls[i] < lowest )
   			{
   				lowest = rolls[i];
   				lowestPos = i;
   			}
   		}
   		results = results.concat( diceChain + " ) Results:" );
   		for ( i = 0; i < rolls.length; i++ )
   		{
   			if ( i != lowestPos )
   				results = results.concat( " " + rolls[i] );
   		}
   		if ( rolls[amount] == mod+1 && lowest == mod+1 && lowestPos != amount )
   		{
   			results = results.concat( " 4BUST!" );
   		}
   		return results;
   	}
	public static String shadowRoller( String message, String sender )
	//shadowrun dice roller
	{
		String diceChain = "";
		double fate;
		int hits = 0;
		int die;
		int rolls = 0;
		int ones = 0;
		boolean isDice = true;
		boolean edge = false;
		int pool;
		String results = "";
		String[] args = message.split(" ");
		if ( args.length == 1 )
		{
			return "Useage: !sr X [edge] Where x is the number of die in pool, and edge applies the rule of sixes.";
		}
		else
		{
			isDice = true;
			try
   			{
   				pool = Integer.parseInt ( args[1] );
   			}
   			catch(Exception e)
   			{
  				return "Useage: !sr X [edge] Where x is the number of die in pool, and edge applies the rule of sixes.";
   			}
   			results = results.concat( "rolled " + pool + " dice " );
   			if ( args.length >= 3 )
   			{
   				if ( args[2].equalsIgnoreCase( "edge" ) )
   				{
   					edge = true;
  					results = results.concat( "with edge ");
   				}
   			}
   			results = results.concat( "for " + sender + " and got (" );
   			while( pool > 0 )
   			{
   				die = Dice.rollDice( 6 );
   				if ( die == 6 && edge )
   				{
   					diceChain = diceChain.concat( "12" );
   					pool++;
   				} else if ( die == 1 )
   				{
  					diceChain = diceChain.concat( "4" );
   					ones++;
  				}
   			    diceChain = diceChain.concat( " " + die + "");
   			    rolls++;
   			    if ( die > 4 )
   			    {
   			    	hits++;
   			    }
   			    pool--;
   			}
   			results = results.concat( diceChain + " )  Hits: " + hits );
   			if ( ones == 0 )
   			{
   				fate = 3;
   			}
   			else
   			{
   				fate = rolls/ones;
   			}
   			if ( fate < 2 && hits == 0 )
   			{
   				results = results.concat( " Critical Glitch!" );
   			}
   			else if ( fate < 2 )
   			{
   				results = results.concat( " Glitch!" );
   			}
   			if (isDice)
   			{
   				return results;
   			}
   			else
  			{
   				return "Useage: !sr X [edge] Where x is the number of die in pool, and edge applies the rule of sixes.";
   			}
		}
	}
	public static String pointValue(String[] args )
	//dnd 3e pointbuy values
	{
		int tally=0;
		int current=0;
		int counter;
		if ( args.length < 2 )
			return "Useage: !value <a> <b> <c> <d> <e> <f>  ALl values must be numeric and between 8 and 18.";
		for ( int i = 1; i < args.length; i++)
		{
			try
   			{
   				current = Integer.parseInt ( args[i] );
   			}
   			catch(Exception e)
   			{
  				return "Useage: !value <a> <b> <c> <d> <e> <f>  ALl values must be numeric and between 8 and 18.";
   			}
   			if ( current < 8 || current > 18 )
   			{
   				return "Useage: !value <a> <b> <c> <d> <e> <f>  ALl values must be numeric and between 8 and 18.";
   			}
   			current = current - 8;
   			counter = 6;
   			while ( counter != 0 && current != 0 )
   			{
   				tally++;
   				current--;
   				counter--;
   			}
   			counter = 2;
   			while ( counter != 0 && current != 0 )
   			{

   				tally+=2;
   				current--;
   				counter--;
   			}
   			counter = 2;
   			while ( counter != 0 && current != 0 )
   			{

   				tally+=3;
   				current--;
   				counter--;
   			}
		}
		return "Point buy value of the indicated attributes is: " + tally;
	}
	public static String[] genChar(String type)
	//dnd character roller
	{
		int amount = 4;
		int[] rolls = new int[5];
		String[] result = new String[7];
		result[0] = "Rolling d20 stats for a ";

		if ( type.equalsIgnoreCase("highpower") )
		{
			amount = 5;
			result[0] = result[0] + " high-powered character.";
		}
		else if ( type.equalsIgnoreCase("lowpower") )
		{
			amount = 3;
			result[0] = result[0] + " low-powered character.";
			rolls[4] = 0;
			rolls[3] = 0;
		}
		else
		{
			result[0] = result[0] + " mid-powered character.";
			rolls[4] = 0;
		}
		for (int i = 1; i < 7; i++)
		{
			result[i] = "Rolls: ";
			for (int j = amount-1; j > -1; j--)
			{
				rolls[j] = Dice.rollDice(6);
				result[i] = result[i] + rolls[j] + " ";
			}
			result[i] = result[i] + "Result: " + statCrunch(rolls);
		}
		return result;
	}
	public static int statCrunch(int[] rolls)
	//called by dnd stat roller
	{
		int lowest = 7;
		int lowestPos = -1;
		int amount = 0;
		int total = 0;
		for ( int i = 0; i < rolls.length; i ++)
		{
			if ( rolls[i] > 0 )
			{
				amount++;

				if ( rolls[i] < lowest )
				{
					lowest = rolls[i];
					lowestPos = i;
				}
			}
		}
		if ( amount > 3 )
		{
			rolls[lowestPos] = 0;
			return statCrunch(rolls);
		}
		else
		{
			for ( int i = 0; i < rolls.length; i ++)
			{
				total += rolls[i];
			}
			return total;
		}
	}
	public RollSort getTable(String name)
	{
		for (int i = 0; i < tables.size(); i++)
		{
			RollSort x = tables.get(i);
			if ( name.equals(x.getName()) )
				return x;
		}
		return new RollSort("null");
	}
	public boolean hasTable(String name)
	{
		for (int i = 0; i < tables.size(); i++)
		{
			RollSort x = tables.get(i);
			if ( name.equals(x.getName()) )
				return true;
		}
		return false;
	}
	public void addTable(RollSort value)
	{
		for (int i = 0; i < tables.size(); i++)
		{
			String name = value.getName();
			RollSort x = tables.get(i);
			if ( name.equals(x.getName()) )
				tables.remove(i);
		}
		tables.add(value);
	}
	public void addProfile(String name)
	{
		for (int i = 0; i < profiles.size(); i++)
		{
			PlayerProfile x = profiles.get(i);
			if ( name.equals(x.getName()) )
				profiles.remove(i);
		}
		profiles.add(new PlayerProfile(name) );
	}
	public boolean hasProfile(String name)
	{
		for (int i = 0; i < profiles.size(); i++)
		{
			PlayerProfile x = profiles.get(i);
			if ( name.equals(x.getName()) )
				return true;
		}
		return false;
	}
	public void setNotice(String name, boolean note)
	{
		for (int i = 0; i < profiles.size(); i++)
		{
			PlayerProfile x = profiles.get(i);
			if ( name.equals(x.getName()) )
			{
				x.setNotify(note);
				profiles.remove(i);
				profiles.add(x);
				return;
			}
		}
		return;
	}
	public void setSonic(String name, boolean note)
	{
		for (int i = 0; i < profiles.size(); i++)
		{
			PlayerProfile x = profiles.get(i);
			if ( name.equals(x.getName()) )
			{
				x.setSound(note);
				profiles.remove(i);
				profiles.add(x);
				return;
			}
		}
		return;
	}
	public void setSFile(String name, String file)
	{
		for (int i = 0; i < profiles.size(); i++)
		{
			PlayerProfile x = profiles.get(i);
			if ( name.equals(x.getName()) )
			{
				x.setSoundFile(file);
				profiles.remove(i);
				profiles.add(x);
				return;
			}
		}
		return;
	}
	public static String magic8Ball()
	{
		int fortune = Dice.rollDice(20);
		if (fortune==1)
		{
			return "As I see it, yes";
		}
		else if (fortune==2)
		{
			return "Ask again later";
		}
		else if (fortune==3)
		{
			return "Better not tell you now";
		}
		else if (fortune==4)
		{
			return "Cannot predict now";
		}
		else if (fortune==5)
		{
			return "Concentrate and ask again";
		}
		else if (fortune==6)
		{
			return "Don't count on it";
		}
		else if (fortune==7)
		{
			return "You may rely on it";
		}
		else if (fortune==8)
		{
			return "It is certain";
		}
		else if (fortune==9)
		{
			return "It is decidedly so ";
		}
		else if (fortune==10)
		{
			return "Most likely";
		}
		else if (fortune==11)
		{
			return "My reply is no";
		}
		else if (fortune==12)
		{
			return "Yes - definitely";
		}
		else if (fortune==13)
		{
			return "My sources say no";
		}
		else if (fortune==14)
		{
			return "Outlook good";
		}
		else if (fortune==15)
		{
			return "Outlook not so good";
		}
		else if (fortune==16)
		{
			return "Reply hazy, try again";
		}
		else if (fortune==17)
		{
			return "Signs point to yes";
		}
		else if (fortune==18)
		{
			return "Very doubtful";
		}
		else if (fortune==19)
		{
			return "Without a doubt";
		}
		else
		{
			return "Yes";
		}
	}
	public boolean notifyMe(String name)
	{
		for (int i = 0; i < profiles.size(); i++)
		{
			PlayerProfile x = profiles.get(i);
			if ( name.equals(x.getName()) )
				return x.isNotify();
		}
		return false;
	}
	public boolean beepMe(String name)
	{
		for (int i = 0; i < profiles.size(); i++)
		{
			PlayerProfile x = profiles.get(i);
			if ( name.equals(x.getName()) )
				return x.isSound();
		}
		return false;
	}
	public String getSFile(String name)
	{
		for (int i = 0; i < profiles.size(); i++)
		{
			PlayerProfile x = profiles.get(i);
			if ( name.equals(x.getName()) )
				return x.getSoundFile();
		}
		return "";
	}
	public boolean saveGMs()
	// saves current gm list to file
	{
		PrintWriter output = null;
		try
		{
			output = new PrintWriter(new FileOutputStream("gmlist.ini"));
		}
		catch(FileNotFoundException e)
		{
			return false;
		}
		for ( int i = 0; i < gms.size(); i++ )
		{
			output.println( gms.get(i) );
		}
		output.close();
		return true;
	}
	public boolean loadGMs()
	{
		try
		{
			BufferedReader input = new BufferedReader( new FileReader( "gmlist.ini"));
			String line = input.readLine();
			while ( line != null)
			{
				setGm(line);
				line = input.readLine();
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	public boolean saveProfiles()
	// saves current profile list to file
	{
		PrintWriter output = null;
		try
		{
			output = new PrintWriter(new FileOutputStream("profiles.ini"));
		}
		catch(FileNotFoundException e)
		{
			return false;
		}
		for ( int i = 0; i < profiles.size(); i++ )
		{
			output.println( profiles.get(i).toString() );
		}
		output.close();
		return true;
	}
	public static PlayerProfile stringToProfile(String input)
	{
		String soundFile = "";
		String[] args = input.split(" ");
		PlayerProfile x = new PlayerProfile(args[0]);
		if (args.length > 1 )
		{
			if (args[1].equalsIgnoreCase("1"))
				x.setNotify(true);
		}
		if (args.length > 2 )
		{
			if (args[1].equalsIgnoreCase("1"))
				x.setSound(true);
		}
		if (args.length > 3 )
			x.setSoundFile(args[3]);
		return x;
	}
	public boolean loadProfiles()
	{
		try
		{
			BufferedReader input = new BufferedReader( new FileReader( "profiles.ini"));
			String line = input.readLine();
			while ( line != null)
			{
				PlayerProfile x = stringToProfile(line);
				profiles.add(x);
				line = input.readLine();
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	public boolean saveTotals()
	{
		PrintWriter output = null;
		try
		{
			output = new PrintWriter(new FileOutputStream("totals.ini"));
		}
		catch(FileNotFoundException e)
		{
			return false;
		}
		for ( int i = 0; i < totals.size(); i++ )
		{
			output.println( totals.get(i).toRaw() );
		}
		output.close();
		return true;
	}
	public boolean loadTotals()
	{
		try
		{
			BufferedReader input = new BufferedReader( new FileReader( "totals.ini"));
			String line = input.readLine();
			while ( line != null)
			{
				ExpTot x = ExpTot.parseRaw(line);
				totals.add(x);
				line = input.readLine();
			}
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	public boolean hasTotal(String name)
	{
		for (int i = 0; i < totals.size(); i++)
		{
			ExpTot x = totals.get(i);
			if ( name.equals(x.getName()) )
				return true;
		}
		return false;
	}
	public ExpTot getTotal(String name)
	{
		for (int i = 0; i < totals.size(); i++)
		{
			ExpTot x = totals.get(i);
			if ( name.equals(x.getName()) )
				return x;
		}
		return new ExpTot("null", 0);
	}
	public void addTotal(ExpTot t)
	{
		String name = t.getName();
		for (int i = 0; i < totals.size(); i++)
		{
			ExpTot x = totals.get(i);
			if ( name.equals(x.getName()) )
				totals.remove(i);
		}
		totals.add( t );
	}
	public static int getInt(String input)
	//returns a 0 if an invalid input saves me a lot of try/catch acrobatics
	{
		try
		{
			return Integer.parseInt(input);
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	public double getDouble(String input)
	//returns a 0 if an invalid input saves me a lot of try/catch acrobatics
	{
		try
		{
			return Double.parseDouble(input);
		}
		catch(Exception e)
		{
			return 0;
		}
	}
	public static int[] cTRaw(int skill)
	{
		int[] output = new int[skill];
		for (int i = skill-1; i >= 0; i--)
		{
			output[i]=Dice.rollDice(10);
		}
		return output;
	}
	public static int countInt(int[] stuff, int huh)
	{
		int count = 0;
		for ( int i = 0; i < stuff.length; i++ )
		{
			if ( stuff[i] == huh )
				count++;
		}
		return count;
	}
	public static int getSet(int[] stuff)
	{
		int output = 0;
		int something = 0;
		for (int i = 1; i < 11; i++)
		{
			something = i*countInt(stuff, i);
			if (something > output)
			{
				output = something;
			}
		}
		return output;
	}
	public static int getStraight(int[] input)
	{
		int output = 0;
		int something = 0;
		int last = 0;
		int count = 1;
		for (int i = 1; i < 12; i++)
		{
			if ( i == last+1 && containsInt(input, i) )
			{
				something+=i;
				count++;
			}
			else
			{
				if ( something > output && count > 2 )
				{
					output = something;
				}
				count = 1;
				if ( containsInt(input, i) )
				{
					something = i;
				}
			}
			if ( containsInt(input, i) )
				last = i;
		}
		return output;
	}
	public static int getHigh(int[] input)
	{
		int output = 0;
		for ( int i = 0; i < input.length; i++ )
		{
			if ( input[i] > output )
				output = input[i];
		}
		return output;
	}
	public static String cTRoller(int skill, int base, String name)
	{
		int ones = 1;
		int straight = 0;
		int set = 0;
		int high = 0;
		int fin = 0;
		String output = "";
		int[] raw = cTRaw(skill);
		high = getHigh(raw);
		if (raw.length > 2)
		{
			straight = getStraight(raw);
		}
		set = getSet(raw);
		fin = high;
		if ( fin < set )
			fin = set;
		if ( fin < straight )
			fin = straight;
		int result = fin + base;
		output = "rolled a skill " + skill + " with base " + base + " for " + name + " and got ( ";
		for ( int i = 0; i < raw.length; i++)
		{
			output = output + raw[i] + " ";
		}
		output = output + ") Result: " + result;
		return output;
	}
	public static boolean isInt(String input)
	{
		try
		{
			Integer.parseInt(input);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	public static boolean containsInt(int[] stuff, int key)
	{
		for ( int i = 0; i < stuff.length; i++ )
		{
			if ( stuff[i] == key )
				return true;
		}
		return false;
	}
	public static String aarnRace( boolean noUndead, boolean noThera, String suffix )
	{
		int roll = Dice.rollDice(100);
		if ( roll >= 1 && roll <= 20 )
			return "Jerolan Human " + suffix;
		else if ( roll >= 21 && roll <= 35 )
			return "Igan Human " + suffix;
		else if ( roll >= 36 && roll <= 45 )
			return "Habrukan Human " + suffix;
		else if ( roll >= 46 && roll <= 50 )
			return "Anthro " + suffix;
		else if ( roll >= 51 && roll <= 54 )
			return "Camarian Human " + suffix;
		else if ( roll >= 55 && roll <= 56 )
			return "Zhan Human " + suffix;
		else if ( roll == 57 )
			return "Kaipu Human " + suffix;
		else if ( roll == 58 )
			return "Rensvaja Human " + suffix;
		else if ( roll >= 59 && roll <= 61 )
			return "Pan Satyr " + suffix;
		else if ( roll >= 62 && roll <= 66 )
			return "Buck Satyr " + suffix;
		else if ( roll == 67 )
			return "Faun Satyr " + suffix;
		else if ( roll >= 68 && roll <= 75 )
			return "Orchaeya " + suffix;
		else if ( roll >= 76 && roll <= 83 )
			return "Harpy " + suffix;
		else if ( roll >= 84 && roll <= 91 )
		{
			int rollx = Dice.rollDice(100);
			if ( rollx >= 1 && rollx <= 35 )
				return "City Ardlin " + suffix;
			else if ( rollx >= 36 && rollx <= 60 )
				return "Forest Ardlin " + suffix;
			else if ( rollx >= 61 && rollx <= 75 )
				return "Mountain Ardlin " + suffix;
			else if ( rollx >= 75 && rollx <= 90 )
				return "Swamp Ardlin " + suffix;
			else if ( rollx >= 91 && rollx <= 93 )
				return "Plains Ardlin " + suffix;
			else if ( rollx == 94 )
				return "Cave Ardlin " + suffix;
			else if ( rollx == 95 )
				return "Snow Ardlin " + suffix;
			else if ( rollx == 96 )
				return "River Ardlin " + suffix;
			else if ( rollx == 97 )
				return "Sea Ardlin " + suffix;
			else if ( rollx == 98 )
				return "Sand Ardlin " + suffix;
			else if ( rollx == 99 )
				return "Bush Ardlin " + suffix;
			else
				return "Tree Ardlin " + suffix;
		}
		else if ( roll >= 92 && roll <= 93 )
			return "Naga " + suffix;
		else if ( roll >= 94 && roll <= 95 )
			return "Lenneshin " + suffix;
		else if ( roll >= 96 && roll <= 97 )
			return "Ghorma " + suffix;
		else if ( roll >= 98 && roll <= 99 )
			return "Centuar " + suffix;
		else
		{
			int rollx = Dice.rollDice(10);
			if ( rollx >= 1 && rollx <= 3 )
				return "Ghrok " + suffix;
			else if ( rollx == 4 )
				return "D'zont " + suffix;
			else if ( rollx == 5 )
				return "Skull Imp " + suffix;
			else if ( rollx == 6 )
				return "Dreamling " + suffix;
			else if ( rollx == 7 )
				return "Salamander " + suffix;
			else if ( rollx == 8 )
				return "Spirit Animal " + suffix;
			else if ( rollx == 9 )
			{
				if ( noThera )
					return aarnRace( noUndead, true, suffix );
				else
					return aarnRace( noUndead, true, "Theromorph " + suffix );
			}
			else
			{
				if ( noUndead )
					return aarnRace( true, noThera, suffix );
				else
				{
					int rolly = Dice.rollDice(100);
					if ( rolly >= 1 && rolly <= 50 )
						return aarnRace( true, noThera, "Dhampire " + suffix );
					else if ( rolly >= 51 && rolly <= 65 )
						return "Anima possessing a(n) " + aarnRace( true, noThera, suffix );
					else if ( rolly >= 66 && rolly <= 80 )
						return aarnRace( true, noThera, "Phantom " + suffix );
					else if ( rolly >= 81 && rolly <= 90 )
						return aarnRace( true, noThera, "Araenid " + suffix );
					else if ( rolly >= 91 && rolly <= 98 )
						return aarnRace( true, noThera, "Deadwalker " + suffix );
					else if ( rolly == 99 )
						return aarnRace( true, noThera, "Vampire " + suffix );
					else
						return aarnRace( true, noThera, "Lich " + suffix );
				}
			}
		}
	}
	public static String wonder(boolean expanded)
	{
		int dice = Dice.rollDice(100);
		int table = 1;
		if (expanded)
		{
			if ( dice >= 1 && dice <= 65 )
				table = 1;
			else if ( dice >= 66 && dice <= 95 )
				table = 2;
			else
				table = 3;
		}
		dice = Dice.rollDice(100);
		if ( table == 1 )
		{
			if ( dice >= 1 && dice <= 5 )
				return "Slow creature pointed at for 10 rounds (Will DC 15 negates).";
			else if ( dice >= 6 && dice <= 9 )
				return "Faerie fire surrounds the target.";
			else if ( dice >= 10 && dice <= 14 )
				return "Deludes wielder for 1 round into believing (no save): " + wonder(expanded);
			else if ( dice >= 15 && dice <= 18 )
				return "Gust of wind, but at a windstorm force; see winds DMG p. 95 (Fortitude DC 14 negates).";
			else if ( dice >= 19 && dice <= 23 )
				return "Wielder learns target's surface thoughts (as with detect thoughts) for " + Dice.rollDice(4) + " rounds (no save).";
			else if ( dice >= 24 && dice <= 27 )
				return "Stinking cloud at 30-ft. range (Fortitude DC 15 negates).";
			else if ( dice >= 28 && dice <= 32)
				return "Stinking cloud at 30-ft. range (Fortitude DC 15 negates).";
			else if ( dice >= 33 && dice <= 36 )
			{
				dice = Dice.rollDice(100);
				if ( dice >= 1 && dice <= 25 )
					return "Summons a rhino.";
				else if ( dice >= 26 && dice <= 50 )
					return "Summons an elephant.";
				else
					return "Summons a mouse.";
			}
			else if ( dice >= 37 && dice <= 41 )
				return "Lightning bolt (70 ft. long, 5 ft. wide), " + Dice.rollDice(6, 6) + " damage (Reflex DC 15 half).";
			else if ( dice >= 42 && dice <= 45 )
				return "Stream of 600 large butterflies pours forth and flutters around for 2 rounds, blinding everyone (including wielder) within 25 ft. (Reflex DC 14 negates).";
			else if ( dice >= 46 && dice <= 50 )
				return "Enlarge person if within 60 ft. of rod (Fortitude DC 13 negates).";
			else if ( dice >= 51 && dice <= 54 )
				return "Darkness, 30-ft.-diameter hemisphere, centered 30 ft. away from rod.";
			else if ( dice >= 55 && dice <= 59 )
				return "Grass grows in 160-sq.-ft. area before the rod, or grass existing there grows to ten times normal height.";
			else if ( dice >= 60 && dice <= 63 )
				return "Turn ethereal any nonliving object of up to 1,000 lb. mass and up to 30 cu. ft. in size.";
			else if ( dice >= 64 && dice <= 68 )
				return "Reduce wielder to 1/12 height (no save).";
			else if ( dice >= 69 && dice <= 72 )
				return "Fireball at target or 100 ft. straight ahead, " + Dice.rollDice( 6, 6 ) + " damage (Reflex DC 15 half).";
			else if ( dice >= 73 && dice <= 77 )
				return "Invisibility covers rod wielder.";
			else if ( dice >= 78 && dice <= 81 )
				return "Leaves grow from target if within 60 ft. of rod. These last 24 hours.";
			else if ( dice >= 82 && dice <= 86 )
			{
				dice = Dice.rollDice(31)+9;
				int hits = Dice.rollDice( 5, 4 );
				return dice + " gems, value 1 gp each, shoot forth in a 30-ft.-long stream. Each gem deals 1 point of damage to any creature in its path: " + hits +  " hits and divide them among the available targets.";
			}
			else if ( dice >= 87 && dice <= 90 )
				return "Shimmering colors dance and play over a 40-ft.-by-30-ft. area in front of rod. Creatures therein are blinded for " + Dice.rollDice(6) + " rounds (Fortitude DC 15 negates).";
			else if ( dice >= 91 && dice <= 95 )
			{
				int coin = Dice.rollDice(2);
				dice = Dice.rollDice(3);
				String color = "";
				if ( dice == 1 )
					color = "blue";
				else if ( dice == 2 )
					color = "green";
				else
					color = "purple";
				if ( coin == 1 )
					return "You turn permanently " + color;
				else
					return "The target turns permanently " + color;
			}
			else
				return "Flesh to stone (or stone to flesh if target is stone already) if target is within 60 ft. (Fortitude DC 18 negates).";
		}
		else if ( table == 2 )
		{
			if ( dice >= 1 && dice <= 4 )
				return "Wielder is teleported to the location where she was at exactly one day ago.";
			else if ( dice >= 5 && dice <= 8 )
				return "A 200-ft. wall of stone appears wherever the rod wielder wishes it.";
			else if ( dice >= 9 && dice <= 12)
				return "Mass invisibility affects every creature within 900 ft. of the wielder.";
			else if ( dice >= 13 && dice <= 16 )
				return "Antimagic field centered on the rod wielder.";
			else if ( dice >= 17 && dice <= 20 )
				return "Brilliant light from above illuminates random creature (DMs choice) granting a +3 circumstance bonus on all ranged attacks against that creature.";
			else if ( dice >= 21 && dice <= 24 )
				return "Baleful polymorph (DC 25) affects a creature of the rod wielder's choosing.";
			else if ( dice >= 25 && dice <= 28 )
				return "Summon monster VIII.";
			else if ( dice >= 29 && dice <= 32 )
				return "Delayed blast fireball, at target or 100 ft. straight ahead, causes " + Dice.rollDice( 15, 6 ) + " points of fire damage (DC 25).";
			else if ( dice >= 33 && dice <= 36 )
				return "Forcecage affects an area selected by the rod wielder.";
			else if ( dice >= 37 && dice <= 40 )
				return "Nearest pool of standing water becomes stagnant and undrinkable.";
			else if ( dice >= 41 && dice <= 44 )
				return "A random creature within 50 ft. of the rod wielder is affected by eyebite (DC 25).";
			else if ( dice >= 45 && dice <= 48 )
				return "The nearest plant is affected by blight.";
			else if ( dice >= 49 && dice <= 52 )
				return "Wielder becomes a member of the opposite sex.";
			else if ( dice >= 53 && dice <= 56 )
				return "Legend lore is cast upon the item nearest to the rod wielder that is not carried by the wielder or another party member.";
			else if ( dice >= 57 && dice <= 60 )
				return "Hold monster (DC 25) is cast against the monster nearest the rod wielder.";
			else if ( dice >= 61 && dice <= 63 )
				return "Greater teleport to the nearest city (or place of interest if there are no cities on the plane where the PCs are currently at).";
			else if ( dice >= 64 && dice <= 67 )
				return "The creature nearest the rod wielder is afflicted with insanity (DC 25).";
			else if ( dice >= 68 && dice <= 71 )
				return "Reverse gravity affects 150 ft., centered on the rod wielder.";
			else if ( dice >= 72 && dice <= 75 )
				return "Move earth affects the surrounding area as instructed by the rod wielder.";
			else if ( dice >= 76 && dice <= 79 )
				return "Nearest structure house-sized or smaller constructed primarily of wood crumbles.";
			else if ( dice >= 80 && dice <= 82 )
				return "Statue is cast upon the wielder.";
			else if ( dice >= 83 && dice <= 86 )
				return "The rod wielder or the creature of the wielder's choice is affects by moment of prescience (caster level 20).";
			else if ( dice >= 87 && dice <= 90 )
				return "Project image of the rod wielder.";
			else if ( dice >= 91 && dice <= 93 )
				return "Transmute rock to mud affects thirty 10-ft. cubes in the locations specified by the rod wielder.";
			else if ( dice >= 94 && dice <= 97 )
				return "Power word blind against a creature of the rod wielder's choice.";
			else
				return "Rod fires a disruptor beam (see blaster rifle).";
		}
		else
		{
			if ( dice >= 1 && dice <= 4 )
				return "Gate (planar travel) to Kord's Realm in Ysgard.";
			else if ( dice >= 5 && dice <= 8 )
				return "Gate (planar travel) to Shra'kt'lor in Limbo.";
			else if ( dice >= 9 && dice <= 12 )
				return "Gate (planar travel) to the City of Slaughter in Pandemonium.";
			else if ( dice >= 13 && dice <= 16 )
				return "Gate (planar travel) to Azzgrat on the 45th layer of the Abyss.";
			else if ( dice >= 17 && dice <= 20 )
				return "Gate (planar travel) to the Sand Tombs of Payratheon in Carceri.";
			else if ( dice >= 21 && dice <= 24 )
				return "Gate (planar travel) to Khin-Oin the Wasting Tower in Hades.";
			else if ( dice >= 25 && dice <= 28 )
				return "Gate (planar travel) to The Crawling City in Gehenna.";
			else if ( dice >= 29 && dice <= 32 )
				return "Gate (planar travel) to The City of Minauros in the Nine Hells.";
			else if ( dice >= 33 && dice <= 36 )
				return "Gate (planar travel) to Clangor in Acheron.";
			else if ( dice >= 37 && dice <= 40 )
				return "Gate (planar travel) to The Center in Mechanus.";
			else if ( dice >= 41 && dice <= 44 )
				return "Gate (planar travel) to The Glass Tarn in Celestia.";
			else if ( dice >= 45 && dice <= 48 )
				return "Gate (planar travel) to The Golden Hills in Bytopia.";
			else if ( dice >= 49 && dice <= 52 )
				return "Gate (planar travel) to The Fortress of the Sun in Elysium.";
			else if ( dice >= 53 && dice <= 56 )
				return "Gate (planar travel) to The Grove of Unicorns in the beastlands.";
			else if ( dice >= 57 && dice <= 60 )
				return "Gate (planar travel) to The Seldarine in Arborea.";
			else if ( dice >= 61 && dice <= 64 )
				return "Gate (planar travel) to Sigil in The Outlands.";
			else if ( dice >= 65 && dice <= 69 )
				return "Wielder is affected by the imprisonment spell.";
			else if ( dice >= 70 && dice <= 73 )
				return "Day becomes night or night becomes day; the day/night pattern of the planet or plane is permanently offset by half a day.";
			else if ( dice >= 74 && dice <= 78 )
				return "Power word kill against a creature of the wielder's choice.";
			else if ( dice >= 79 && dice <= 83 )
				return "The most recent adversary the wielder killed that has an equal or greater number of Hit Dice is returned to life at exact location where it was slain, and it immediately becomes obsessed with evening the score.";
			else if ( dice >= 84 && dice <= 88 )
				return "Summon monster IX.";
			else if ( dice >= 89 && dice <= 92 )
				return "Wielder shapechanges into a creature of the character's choice.";
			else if ( dice >= 93 && dice <= 96 )
				return "Iron body affects the wielder for 20 minutes.";
			else
				return "Wish (no XP or gold cost).";
		}
	}
	public static boolean isSavage( String message )
   	{
   		message = message.toLowerCase();
   		String[] dice = new String[2];
   		if ( !message.startsWith("e") )
   		{
   			String[] stuff = message.split("e");
   			if ( stuff.length < 2 )
   				return false;
   			else
   			{
   				try
   				{
   					int i = Integer.parseInt( stuff[0] );
   				}
   				catch (Exception e)
   				{
   					return false;
   				}
   				message = "d" + stuff[1];
   			}
   		}
   		message = message.replace( "+", "�" );
   		if ( message.indexOf( "-" ) < 0 && message.indexOf( "�" ) < 0 )
   			message = message.concat( "�0" );
   		if ( message.indexOf( "�" ) > -1 )
   			dice = message.split("�");
   		else if ( message.indexOf( "-" ) > -1 )
   		{
   			dice = message.split("-");
   		}
   		if ( dice[0].length() < 2 )
   			return false;
   		dice[0] = dice[0].substring(1);
   		try
   		{
   			int i = Integer.parseInt( dice[0] );
   			i = Integer.parseInt( dice[1] );
   		}
   		catch (Exception e)
   		{
   			return false;
   		}
   		return true;
   	}
   	public static int aceInt(String message)
   	//this rolls dice and returns the putput as a number only.
   	//bad input results in a 0
   	//do NOT use without checking with isRoll()
   	//does NOT handle passes xdy+z format only
   	{
   		message = message.trim();
   		int amount = 1;
   		int die;
   		int mod = 0;
   		String text = "1";
   		int modMult = 1;
   		int total = 0;
   		int size = 0;
   		message = message.toLowerCase();

   		if ( !isSavage(message) )
			return 0;
   		if ( !message.startsWith("e") )
   		{
   			String[] stuff = message.split("e");
	    	try
	   		{
	   			amount = Integer.parseInt( stuff[0] );
	   			message = stuff[1];
	   		}
	   		catch(Exception e)
	   		{
	  			return 0;
	   		}
   		}
   		else
   		{
   			message = message.substring(1);
   		}
   		message = message.replace( "+", "�" );
   		if ( message.indexOf( "-" ) < 0 && message.indexOf( "�" ) < 0 )
   			message = message.concat( "�0" );
   		if ( message.indexOf("-") < 0 )
   		{
   			String[] stuff = message.split("�");
   			try
   			{
   				die = Integer.parseInt( stuff[0] );
   				mod = Integer.parseInt( stuff[1] );
   			}
   			catch(Exception e)
   			{
   				return 0;
   			}
   		}
   		else
   		{
   			modMult = -1;
   			String[] stuff = message.split("-");
   			try
   			{
   				die = Integer.parseInt( stuff[0] );
   				mod = Integer.parseInt( stuff[1] );
   			}
   			catch(Exception e)
   			{
   				return 0;
   			}
   		}
   		int bunk = 0;
   		while ( amount > 0 )
   		{
   			bunk+=Dice.rollAce( die, mod*modMult );
   			amount--;
   		}
   		return bunk;
   	}
    public static int parseOne(String dice)
    {
    	//we find parenthesis and replace them with NUMBERS!
		for ( int i = 0; i < dice.length(); i++ )
		{
			char whee = dice.charAt(i);
			if ( whee == '(' )
			{
				int pos = i;
				for ( int j = dice.length()-1; j > pos; j-- )
				{
					char whoa = dice.charAt(j);
					if ( whoa == ')')
					{
						String subbie = dice.substring(i+1, j);
						System.out.println( dice.substring( 0, i ) + parseOne(subbie) + dice.substring( j+1 ) );
						dice = dice.substring( 0, i ) + parseOne(subbie) + dice.substring( j+1 );
						j = pos;
					}
				}
			}
		}
    	//we turn any instances of - used as an operator into +- which is the same thing
    	//mathematically but it lets us split by +
    	for ( int i = 0; i < dice.length(); i++ )
		{
			char moo = dice.charAt(i);
			if ( moo == '-' && i != 0 )
			{
				String thing = dice.charAt(i-1) + "";
				if ( isInt(thing) )
				{
					dice = dice.substring(0,i) + "+" + dice.substring(i);
				}
			}
		}
		String[] stuff = dice.split("\\+");
		int total = 0;
		for ( int i = 0; i < stuff.length; i++ )
		{
			total+=parseTwo(stuff[i]);
		}
		return total;
    }
   	public static int parseTwo(String input)
   	{
   		String[] rubbish = input.split("/");
   		int total = parseThree(rubbish[0]);
   		if ( rubbish.length == 1 )
   			return total;
   		else
   		{
	   		for ( int i = 1; i < rubbish.length; i++ )
	   		{
	   			total = total/parseThree(rubbish[i]);
	   		}
	   		return total;
   		}
   	}
   	public static int parseThree(String input)
   	{
   		String[] moogle = input.split("\\*");
   		int total = parseFour(moogle[0]);
   		if ( moogle.length == 1 )
   			return total;
   		else
   		{
	   		for ( int i = 1; i < moogle.length; i++ )
	   		{
	   			total = total*parseFour(moogle[i]);
	   		}
	   		return total;
   		}
   	}
   	public static int parseFour(String input)
   	{
   		int i=0;
   		if ( input.startsWith("-") && isRoll(input.substring(1)) )
   			i = -1*rollerInt( input.substring(1) );
   		else if ( isRoll(input) )
   			i = rollerInt(input);
   		else if ( isSavage(input) )
   			i = aceInt(input);
   		else
   			i = getInt(input);
   		return i;
   	}
	public static boolean isEquation(String input)
   	{
   		//returns true if the string is a valid dice equation for the new roller
   		//returns false otherwise
   		char last='0';
   		String t = "";
   		char cur='0';
   		boolean dubNeg=true;
   		boolean wasOp=false;
   		boolean isOp=false;
   		for ( int i = 0; i < input.length(); i++ )
   		{
   			t = "" + input.charAt(i);
   			cur=input.charAt(i);
   			if ( isInt(t) || cur == ')' || cur == 'd' || cur == 'e' )
   			{
   				isOp=false;
   				dubNeg=false;
   			}
   			else if ( input.charAt(i) != ')' )
   				isOp=true;
   			if ( !isInt(t) && cur != '+' && cur != '-' && cur != '*' && cur != '/' && cur != '(' && cur != ')' && cur != 'd' && cur != 'e' )
   				return false;
   			if ( isOp && wasOp )
   			{
   				if ( cur != '-' || dubNeg )
   					return false;
   				else
   					dubNeg=true;
   			}
   			last = input.charAt(i);
   			wasOp=isOp;
   		}
		return !wasOp;
   	}
}