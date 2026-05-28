package com.abruce.manille;

import java.util.ArrayList;
import java.util.Random;

public class EasyAI extends AI
{
    @Override
    protected Card selectLead(ArrayList<Card> cards)
    {
        //Simple - leads a random (valid) card
        return cards.get(new Random().nextInt(cards.size()));
    }

    @Override
    protected Card selectFollow(ArrayList<Card> validCards, Card leadCard)
    {
        //Simple - follows with a random (valid) card
        return validCards.get(new Random().nextInt(validCards.size()));
    }
}
