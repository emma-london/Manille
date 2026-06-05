package com.abruce.manille;

import java.util.ArrayList;

public class MediumAI extends AI
{
    @Override
    public int chooseTrumps()
    {
        // Pick the suit in which the AI has the most point value.
        // Ties broken randomly.
        ArrayList<Card> aiCards = ManilleApp.getActivity().getOppHand().getAvailCards();
        return pickMaxSuit(pointsBySuit(aiCards));
    }

    @Override
    protected Card selectLead(ArrayList<Card> cards)
    {
        // Currently: return the highest card, preferring the non-trumps

        int trump = ManilleApp.getActivity().getTrumpSuit();
        ArrayList<Card> nonTrumps = cardsNotInSuit(cards, trump);
        return highestValue(nonTrumps.isEmpty() ? cards : nonTrumps);
    }

    @Override
    protected Card selectFollow(ArrayList<Card> validCards, Card leadCard)
    {
        //Either throw away the lowest value card, or win with the lowest rank
        //NB Claude wrote this - I think for points I might rewrite the win case
        ArrayList<Card> winners = winners(validCards, leadCard);
        return winners.isEmpty() ? lowestValue(validCards) : lowestRank(winners);
    }

    private ArrayList<Card> winners(ArrayList<Card> cards, Card leadCard)
    {
        ArrayList<Card> result = new ArrayList<Card>();
        for (Card c : cards)
            if (!GameRules.whoWins(leadCard, c)) result.add(c);
        return result;
    }
}
