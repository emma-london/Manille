package com.abruce.manille;

import java.util.ArrayList;

public abstract class AI
{
    // -------------------------------------------------------------------------
    // Template methods — handle mechanics; delegate card selection to subclasses
    // -------------------------------------------------------------------------

    public final Card lead()
    {
        Hand hand = ManilleApp.getActivity().getOppHand();
        Card c = selectLead(hand.getAvailCards());
        hand.playCard(c);
        return c;
    }

    public final Card follow(Card leadCard)
    {
        Hand hand = ManilleApp.getActivity().getOppHand();
        ArrayList<Card> valid = GameRules.validFollowCards(leadCard, hand);
        Card c = selectFollow(valid, leadCard);
        hand.playCard(c);
        return c;
    }

    // -------------------------------------------------------------------------
    // Subclasses implement these
    // -------------------------------------------------------------------------

    protected abstract Card selectLead(ArrayList<Card> cards);

    protected abstract Card selectFollow(ArrayList<Card> validCards, Card leadCard);

    // -------------------------------------------------------------------------
    // Factory
    // -------------------------------------------------------------------------

    public static final String[] DIFFICULTY_LABELS = { "Easy", "Medium", "Hard (PJB)" };

    public static AI create(int index)
    {
        switch (index)
        {
            case 0:  return new EasyAI();
            case 1:  return new MediumAI();
            case 2:  return new HardAI();
            default: return new MediumAI();
        }
    }

    // -------------------------------------------------------------------------
    // Shared helpers — available to all subclasses
    // -------------------------------------------------------------------------

    /** Highest point value; breaks ties by rank. */
    protected Card highestValue(ArrayList<Card> cards)
    {
        Card best = null;
        int bestScore = -1;
        for (Card c : cards)
        {
            int score = c.getValue() * 10 + c.getRank();
            if (score > bestScore) { bestScore = score; best = c; }
        }
        return best;
    }

    /** Lowest point value; breaks ties by rank (lower rank preferred). */
    protected Card lowestValue(ArrayList<Card> cards)
    {
        Card worst = null;
        int worstScore = Integer.MAX_VALUE;
        for (Card c : cards)
        {
            int score = c.getValue() * 10 + c.getRank();
            if (score < worstScore) { worstScore = score; worst = c; }
        }
        return worst;
    }

    /** Lowest ranked card in the list. */
    protected Card lowestRank(ArrayList<Card> cards)
    {
        Card best = null;
        int bestRank = Integer.MAX_VALUE;
        for (Card c : cards)
        {
            if (c.getRank() < bestRank) { bestRank = c.getRank(); best = c; }
        }
        return best;
    }

    protected ArrayList<Card> cardsInSuit(ArrayList<Card> cards, int suit)
    {
        ArrayList<Card> result = new ArrayList<Card>();
        for (Card c : cards)
            if (c.getSuit() == suit) result.add(c);
        return result;
    }

    protected ArrayList<Card> cardsNotInSuit(ArrayList<Card> cards, int suit)
    {
        ArrayList<Card> result = new ArrayList<Card>();
        for (Card c : cards)
            if (c.getSuit() != suit) result.add(c);
        return result;
    }
}
