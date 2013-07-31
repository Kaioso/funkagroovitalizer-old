
import java.util.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ccelizic
 */
public class NewDeck {
    private String name;
    private ArrayList<PlayerCharacter> players;
    private ArrayList<Card> cards;
    private Stack discard;
    private Card[] deckIndex;
    private String[] suitNames;
    
    public NewDeck()
    {
        name = "";
        players = new ArrayList<PlayerCharacter>();
        cards = new ArrayList<Card>();
        discard = new Stack();
    }
    
}
