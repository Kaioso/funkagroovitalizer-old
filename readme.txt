Herein lies the latest Draft of my Funkagroovitalizer.  The Funkagroovitalizer is a dice bot I created
in my spare time to handle rolling for Dungeons and Dragons, Shadowrun, and Savage Worlds.  The Fbot
also has a magic8ball function.

====
Use
====

Funkagroovitalizer is dependant on the pircbot jar package.  The file is enclosed in the zip.  Running
Funky involves the following command line:

java -classpath pircbot.jar;. FBotMain <IRC Server> <port> <nick> <gm>

You must specify the nick of the initial GM when running funkagroovitalizer, or else a lot of
commands will be unavailable to anyone.

Once the funkagroovitalizer is up and running, invite it to your channel via the IRC command:

/invite <nick> <channel>

From there you are ready to go.

========
Commands
========

If you start a line in channel with a dice roll, funky will roll it d20+1 for example.

!addgm <nick> :: GM ONLY Adds the specified nick to the list of gms and saves the list.

!cardlist [deck] :: Lists all players to a deck in order of highest card held as per rules for
	initiate in Savage Worlds via a Poker deck.

!chargen [highpower/lowpower] :: Rolls a set of d20 attributes.  If no parameter is specified default
	rules are used.  If highpower is used then 5d6 is used, if lowerpower is used then it is 3d6

!collect <nick/all> [deck] :: Collects cards from the specified player.  all is specified then
	all players' cards are collected.

!currentgm :: spits out a list of current gms.

!dealup <target> [deck] :: GM ONLY deals a card up to the specified target

!deckadd <target> [deck] :: GM ONLY adds a player to a deck of cards

!deckreset [deck] :: GM ONLY  forces a deck to the default state.

!deckremove <target> [deck] :: GM ONLY removes a player from a deck of cards

!edit <name> <value> <list> :: GM ONLY manually edits a roll value in a list by name.

!fudge :: rolls 4 fudge dice as described to me by somebody who asked me if Funky could roll fudge
	I only had his description to go off of and have not seen a fudge book, so I am not certain
	if this is up to standards.

!gmlist::  Same function as !currentgm

!hand [deck] :: displays your current hand of cards

!inventory :: Reports your current inventory of Deadlands Fate Chips.

!list <name> :: NOTE:  Gms can only use this in channel.  Anyone else may request !list via a
	query window to prevent spam.  DIsplays the contents of the specified list in order from
	highest to lowest.

!magic8ball :: returns a magic8ball response

!makedeck :: Creates a new deck of cards.

!next :: GM ONLY cycles to the next person in the list.

!notify <yes/no/true/false/1/0/on/off> :: when set to on and cycling through a list such as an
	init list, the bot will send you a query when your turn is up.  THis setting is saved.

!npc <npcname> <roll> <table> :: GM ONLY used for adding a npc's roll to a table, such as init.

!potput <color> :: GM ONLY, adds a fatechip to the fate pot.

!potadd <name> :: GM ONLY, assigns the given name as a player to the fatepot for deadlands

!potdraw :: if the player is a valid player for the deadlands fate pot then it'll draw a chip
	for that player.  A number after the command draws multiple chips.

!potreset :: GM ONLY, resets the Deadlands fate pot

!potset <white> <red> <blue> <legend> :: GM ONLY, manually sets the contents of the pot.

!potstatus :: reports the inventory of the Deadlands Fate Pot

!remove <name> <list> :: removes a name from a list.

!roll x ydz+w :: Rolls dice.  You can try !roll d20 or get as elaborate as !roll 3 4d4+2

!rollfor <name> :: GM ONLY, clears the specified table of all rolls, and requests a roll for that
	table.  Players may roll for it by typing !roll xdy+z <tablename>.  See init sorting for
	more information.

!save :: Forces the bot to save settings.  Normally the bot saves when settings are changed
	so this command is not nessecary in most cases.

!scram :: GM ONLY, causes funkagroovitalizer to leave

!shuffle [deck] :: Shuffles specified deck

!sound :: Same general idea as !notify, except it sends a sound event to you using the sound
	specified via !soundfile.  This setting is saved.

!soundfile :: specifies the sound funkagroovitalizer will use when sending a sound event to you.
	this setting is saved.

!sr <dice> <edge> :: Rolls the specified number of dice as per Shadowrun 4e rules.  If you type in
	edge after the number of dice then the bot will reroll 6s.

!start <list> :: GM ONLY forces a list to the start position

!stats <dice> <rolls>  :: Added purely to test if the dice are fair.  Funkagroovitalizer will
	roll the specified number of dice and report the statistical analysis of the rolls.

!sw xdy+z :: rolls dice as per savage worlds rules, max value dice are rerolled and added up

!swarm xdy+z :: used for batch rolling rolls via the Savage Worlds system, handy for making a bunch
	of attacks from extras.

!use <chipcolor> :: Uses a fatechip, for use with the Deadlands fatepot system, used chips are
	returned to the pot unless they are legend chips which are lost.

!value :: when supplied with 6 attributes Funkagroovitalizer will return the DnD 3.5e value
of those attributes.  TODO:  4e rules.

!wild xdy+z :: Rolls the dice as if you were a wild card as per the rules of Savage Worlds.

============
Dice rolling
============

The most basic format of a dice roll is 
dx+y

x is your dice size, y is the modifier.  So if you want to roll a d20 and add 2 to it, you use
d20+2

Funkagroovitalizer will check any line starting with d to see if it is a valid dice roll.

<Celizic> !roll d20+2
* Funkagroovitalizer rolled a (1d20+2) for Celizic and got: 7
<Celizic> d20+2
* Funkagroovitalizer rolled a (1d20+2) for Celizic and got: 11

A number before the dice rolls multiple dice, but how that number is presented changes how funky
handles the roll.

If there is NO space between the first number and the d funky will roll the specified die that many
times and THEN add the modifier.

<Celizic> !roll 3d4+2
* Funkagroovitalizer rolled a (3d4+2) for Celizic and got: 5

If you put a space between the number and the d then funky will roll multiple times and add the mod
to each individual roll.  Funky will display both the individual rolls and the total.  This is used
for batch rolling a bunch of similar rolls.  Such as a DnD 3e magic missile.

<Celizic> !roll 5 d4+1
* Funkagroovitalizer rolled 5 (1d4+1) for Celizic and got ( 4 3 4 5 3 ) Total: 19

PCs playing a savage worlds game should use the !wild command to roll dice.  extras can use the
!sw command.  The !wild command detects bust automatically.

In shadowrun use the !sr command, don't bother with the d, the command assumes d6s it will even
detect glitches and critical glitches for you.

<Celizic> !sr 12
* Funkagroovitalizer rolled 12 dice for Celizic and got ( 4 1 6 4 5 6 4 1 4 4 1 2 )  Hits: 3


======================
Lists and init sorting
======================

A GM can create a list using the command !rollfor.  The reason for creating such a list mainly is to
sort out init and use the init sorter.

First step is rollfor to create the list, let's make an init list.
!rollfor init

<Celizic> !rollfor init
<Funkagroovitalizer> Celizic has requested a roll for init.

Every time you do this, you clear the list, but only GMs can do this.  (whoever is the first gm
can add additional ones using the !addgm command).

Players can then roll and have their roll added to the list using the !roll command.

<Celizic> !roll d20+2 init
* Funkagroovitalizer rolled a (1d20+2) for Celizic and got: 20 added to init

Now that we have a player on the list, the GM can NPCS to the list using the !npc command

<Celizic> !npc turtle d20-1 init
* Funkagroovitalizer rolled a (d20-1) for turtle and got: 4 added to init
<Celizic> !npc rabbit d20+5 init
* Funkagroovitalizer rolled a (d20+5) for rabbit and got: 14 added to init

Now by typing !list you can show the list's contents.

<Celizic> !list init
<Funkagroovitalizer> List for init:
<Funkagroovitalizer> 20  Celizic
<Funkagroovitalizer> 14  rabbit
<Funkagroovitalizer> 4   turtle

Now that the list is set up, you can cycle through it using the !next command.

<Celizic> !next init
<Funkagroovitalizer> Celizic's turn!  Next is rabbit.
<Celizic> !next init
<Funkagroovitalizer> rabbit's turn!  Next is turtle.
<Celizic> !next init
<Funkagroovitalizer> turtle's turn!  Next is Celizic.
<Celizic> !next init
<Funkagroovitalizer> Celizic's turn!  Next is rabbit.

Anyone who wants to be notified when their turn is up by the bot can type !notify on.  If you have
your irc client set up to recieve sound events, you can type !sound on, and then !soundfile <filename>
to specify a file name for the bot to use to beep you whe nits your turn.  See your irc client's
documentation for instructions on how to set up your client to recieve sound events.

=======
Fatepot
=======

The Fatepot is used by both Deadlands and Deadlands:  Reloaded.  By default the pot holds the starting
set of chips for a deadlands: reloaded fatepot.

Not just anyone can use !potdraw.  They need to be assigned to the pot first.  The Gm can do this by
typing !potadd <name>.  Once you have a chipt you can use it by typing !potuse this puts it back
in the pot unless it is a legend chip which is removed from play when used.  !potreset resets
everything to the defaults.

=====
Cards
=====

Poker card handling has been added to this bot for Savage Worlds and Deadlands: reloaded.  The bot
loads up with a deck known as Master.  If you do not specify a deck to use, the bot will go to Master
as default, naturally Master makes a good deck to use for initiative in Savage Worlds.  Type
!deckadd to add players to the deck you want to use.  And then use either !dealup or !dealdown to 
toss cards at the player.  Cards dealt up are broadcasted to the channel.  cards dealt down are
whispered to the target.  A player can check his hand using !hand.  !collect can be used to grab
all cards from the target !collect all removes all cards from all players.  !cardlist will show a 
sorted list of all players on the specified deck sorted by highest card.  Funky does NOT handle poker
hand heirarchy, the intent of this function was init for Savage Worlds.