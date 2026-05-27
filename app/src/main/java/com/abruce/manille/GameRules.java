package com.abruce.manille;

import java.util.ArrayList;

public class GameRules
{

    //This class acts as a repository for the rules of the Manille game.
    //
    // Manille is a whist type game, so following suit etc is important, but there are some other
    // rules also included. The priority list of rules is:
    //
    // When following
    // 1. If you can play the same suit you must do that.
    //    If you can't follow suit you can play any suit as long as it follows the other rules
    // 2. You must win a trick if possible (as long as rule 1 is followed)
    //
    // When leading you can play what you want

    public static boolean checkUserPlay(Card c)
    {
        //Checks if a proposed user played card is valid under the current circumstances.
        ManilleActivity ma = ManilleApp.getActivity();

        Card oppPlayed = ma.getoppPlayedCard();
        if (oppPlayed == null) {return true;}

        return validFollowCards(oppPlayed, ma.getMyHand()).contains(c);


    }

    public static boolean whoWins(Card lead, Card follow)
    {
        //Returns true if the lead card wins
        ManilleActivity ma = ManilleApp.getActivity();

        if(lead.getSuit() == follow.getSuit())
        {
            if(lead.getRank() > follow.getRank()) {return true;}
            else {return false;}
        }
        else if(follow.getSuit() == ma.getTrumpSuit()) {return false;}
        else {return true;}
    }

    public static ArrayList<Card> validFollowCards(Card c, Hand hand)
    {
        int suit = c.getSuit();
        int rank = c.getRank();
        ManilleActivity ma = ManilleApp.getActivity();

        ArrayList<Card> list = new ArrayList<Card>();
        if (hand.checkForSuit(suit))
        {
            //We have cards in suit, so the played card must be one of those.
            list.addAll(hand.getCardsInSuit(suit));

            if(hand.getMaxRankBySuit(suit) > rank)
            {
                //We can win, so by rule 2 we have to remove the cards with lower rank
                ArrayList<Card> list2 = new ArrayList<Card>();
                for (Card card: list)
                {
                    if (card.getRank() > rank) { list2.add(card); }
                }
                list = list2;
            }
        }
        else if (hand.checkForSuit(ma.getTrumpSuit()))
        {
            //We can't follow suit, but we do have 1 or more trumps, so we can play and win with one of those
            list.addAll(hand.getCardsInSuit(ma.getTrumpSuit()));
        }
        else
        {
            //We can't play in suit and we can't trump, so we have to discard from one of the other two
            //suits
            list.addAll(hand.getAvailCards());
        }

        return list;
    }
}
