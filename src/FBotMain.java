/**
 * @(#)FBotMain.java
 *
 *
 * @author 
 * @version 1.00 2007/8/11
 */
import java.lang.Integer;

public class FBotMain 
{
    public static void main(String[] args) throws Exception 
    {
        FBot bot;
        if ( args.length < 3 )
        {
        	bot = new FBot();
        }
        else
        {
        	bot = new FBot( args[2] );
        }	
        // Enable debugging output.
        bot.setVerbose(true);
        
        //speed things up!
		bot.setMessageDelay( 1 );
		
		if (args.length > 3 )
			bot.setGm( args[3] );
        
        if ( args.length < 2 )
        	bot.connect("208.185.81.252", 6668);
        else
        {
        	int port;
        	try
        	{
        		port = Integer.parseInt( args[1] );
        	}
        	catch (Exception e )
        	{
        		port = 6668;
        	}
        	bot.connect( args[0], port );
        }
        
    }  
    
}