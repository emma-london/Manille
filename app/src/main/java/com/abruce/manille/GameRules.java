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

        Hand myHand = ma.getMyHand();

        //Check 1 - either you have to follow suit unless you haven't got any to follow.
        if (c.getSuit() == oppPlayed.getSuit() )
        {
            //OK - we're following suit
            //Check 2- are we going to win the trick?
            if (c.getRank() > oppPlayed.getRank())
            {
                return true;
            }
            else
            {
                //Check if there is a winning card in hand
                if (myHand.getMaxRankBySuit(oppPlayed.getSuit()) > oppPlayed.getRank())
                {
                    //Player has a higher ranked card which could win, so this isn't a valid play
                    return false;
                }
                else
                {
                    //Player doesn't have anything that could win while following suit
                    return true;
                }
            }
        }
        else if (!myHand.checkForSuit(oppPlayed.getSuit()))
        {
            //OK - we don't have any of that suit to follow
            //Check 2 - are we going to win by playing a trump?
            if (c.getSuit() == ma.getTrumpSuit())
            {
                return true;
            }
            else if (myHand.checkForSuit(ma.getTrumpSuit()))
            {
                //We have a trump which is playable and could win the trick, so we should play that instead
                return false;
            }
            else
            {
                //We can't follow suit, and have no trumps so we can't win. It's a valid (but losing) play
                return true;
            }
        }
        else
        {
            //Quit - this card isn't valid.
            return false;
        }

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

    public static ArrayList<Card> validFollowCards(Card c)
    {
        int suit = c.getSuit();
        int rank = c.getRank();
        ManilleActivity ma = ManilleApp.getActivity();
        Hand hand = ma.getOppHand();

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
