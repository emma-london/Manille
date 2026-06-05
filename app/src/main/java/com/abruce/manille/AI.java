package com.abruce.manille;

import java.util.ArrayList;
import java.util.Random;

public abstract class AI
{
    // -------------------------------------------------------------------------
    // Template methods — handle mechanics; delegate card selection to subclasses
    // -------------------------------------------------------------------------

    /**
     * Choose the trump suit.  Stub implementation — picks randomly.
     * Subclasses can override to use smarter logic.
     */
    public int chooseTrumps()
    {
        return new Random().nextInt(4);
    }

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

    /** Sums point values per suit for the given cards. Returns int[4], indexed by suit. */
    protected int[] pointsBySuit(ArrayList<Card> cards)
    {
        int[] totals = new int[4];
        for (Card c : cards)
            totals[c.getSuit()] += c.getValue();
        return totals;
    }

    /**
     * Returns the suit index with the highest score.
     * Breaks ties by picking randomly among the tied suits.
     */
    protected int pickMaxSuit(int[] scores)
    {
        int max = Integer.MIN_VALUE;
        for (int s : scores) if (s > max) max = s;
        ArrayList<Integer> tied = new ArrayList<>();
        for (int i = 0; i < 4; i++) if (scores[i] == max) tied.add(i);
        return tied.get(new Random().nextInt(tied.size()));
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
