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

        ArrayList<Card> list = GameRules.validFollowCards(leadCard);

        Random rnd = new Random();
        Card c = list.get(rnd.nextInt(list.size()));

        ManilleApp.getActivity().getOppHand().playCard(c);

        return c;
    }
}
