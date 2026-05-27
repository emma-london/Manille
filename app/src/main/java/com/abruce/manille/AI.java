package com.abruce.manille;

import java.util.ArrayList;
import java.util.Random;

public class AI
{
    public static Card lead()
    {
        ManilleActivity ma = ManilleApp.getActivity();

        Hand hand = ma.getOppHand();

        ArrayList<Card> cards = hand.getAvailCards();

        Random rnd = new Random();

        Card c = cards.get(rnd.nextInt(cards.size()));
        hand.playCard(c);

        return c;
    }

    public static Card follow(Card leadCard)
    {
        Hand hand = ManilleApp.getActivity().getOppHand();
        ArrayList<Card> list = GameRules.validFollowCards(leadCard, hand);

        Card c = list.get(new Random().nextInt(list.size()));

        hand.playCard(c);

        return c;
    }
}
