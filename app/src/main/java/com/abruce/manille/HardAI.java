package com.abruce.manille;

import java.util.ArrayList;

public class HardAI extends AI
{
    @Override
    protected Card selectLead(ArrayList<Card> cards)
    {
        int trump = ManilleApp.getActivity().getTrumpSuit();

        ArrayList<Card> trumpCards = cardsInSuit(cards, trump);
        if (trumpCards.size() >= 2)
            return highestValue(trumpCards);

        ArrayList<Card> nonTrumps = cardsNotInSuit(cards, trump);
        return highestValue(nonTrumps.isEmpty() ? cards : nonTrumps);
    }

    @Override
    protected Card selectFollow(ArrayList<Card> validCards, Card leadCard)
    {
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
