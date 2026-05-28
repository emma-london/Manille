A card game for two people (player + AI)

NB written in 2014 - it isn't up to current coding standards yet! 

This was a favourite of my Father's. I have no idea how he found it becuase it's native to
the NE France/W Belgium area and he grew up in Yorkshire, UK! 

https://en.wikipedia.org/wiki/Manille

Some imporant points
* Short (French) deck - 32 cards, 7-A only
* Card rank order: 10, A, K, Q, J, 9, 8, 7
* Card values: 10=5, A=4, K=3, Q=2, J=1. 9-7=0
* Whist based
  * There are trumps
  * You must follow suit if you can
  * (extra) you must win a trick if you can  - NB most whist based games don't have this
* Winning - based on points (see values) for the tricks you win.
    You add points for all cards in a trick. If you win a trick by playing A over their Q you get 6 (4 for your A and 2 for the their 2)
    There are 60 total points in the short deck - winning is >30


There are two areas controlled by the player (and the same two for the AI)

Hand cards (8 of them, botttom of the screen on the app) - 8 initially, not visible to the opps
Table cards (4 slots, above the hand cards on the app) - 4+4. Consisting of 4 visible cards, each covering an under card. As the game starts, 4 top cards are visible. When a visible card is played the card underneath is turned up and made visible on the table (to both players.) 

Play
Players can play any card from their hand or their (visible) table cards. They must follow the rules above re suit, winning etc....



App todo:
* Rewrite the Hard AI to make it more difficult to beat
* Trumps - currently the AI chooses randomly, need to implement some logic to choose better

Done
* Card designs - the spades & clubs are difficult to tell apart. Fixed now!
* Layout - much improved, and now the under cards can be seen on the table
* Trumps - the player can choose now. 
* Different levels of AI - easy/medium/hard (named PJB after my Dad)
* 
If you recognise this game please reach out! I would love to hear about people using it
