package com.abruce.manille;

import java.util.*;

import android.content.res.Resources;
import android.graphics.drawable.*;

public class Card implements Comparable<Card>
{
	//A Manille deck is a "short" deck consisting of 32 cards (7,8,9,J,Q,K,A,10). NB the 10 is the highest ranked card in each suit.  
	//I've created a sequential ID from 0-31 to represent these cards. The least significant 3 bits represent the rank. The most 
	//significant 2 bits represent the suit.
	//0=Hearts, 1=Diamond, 2=Club, 3=Spade    ;    0=7, 1=8, 2=9, 3=J, 4=Q, 5=K, 6=A, 7=10


	static ArrayList<Integer> unusedCardIds = new ArrayList(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31));
	public static Drawable cardBack;
	public static Drawable cardBlank;

	private int suit;
	private int rank;
	private int id;

	private String resourceName;
	private Drawable cardDrawable;

	public Card()
	{
		//TODO - throw exception if unusedCardIds is empty

		//This constructor takes a random card from the unusedCardIds ArrayList and returns it.
		Random rand = new Random();
		int randIndex = rand.nextInt(unusedCardIds.size());
		int cardId = unusedCardIds.remove(randIndex);
		setupFromId(cardId);

		Resources r = ManilleApp.getContext().getResources();
		int id=r.getIdentifier(resourceName, "drawable", ManilleApp.getContext().getPackageName());
		cardDrawable = r.getDrawable(id);
//		int id=res.getIdentifier(c.getResourceName(), "drawable", getPackageName());
//		iv.setImageDrawable(res.getDrawable(id));
	}

	/*
        public Card(int cardId)
        {
            setupFromId(cardId);
        }
    */
	public static void setCardBack(Drawable d)	{ cardBack = d;	}
	public static Drawable getCardBack()	{ return cardBack; }
	public static void setCardBlank(Drawable d)	{ cardBlank = d;	}
	public static Drawable getCardBlank()	{ return cardBlank; }

	public static void resetUnusedCardIds()
	{
		unusedCardIds = new ArrayList(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31));
	}

	private void setupFromId(int cardId)
	{
		id=cardId;
		rank = id % 8;
		suit = (int)id/8;

		resourceName="card"+String.format("%02d", id);

		//Resources res = getApplicationContext().

	}


	public String getResourceName()
	{ return resourceName; }

	public Drawable getDrawable()
	{ return cardDrawable; }

	@Override public int compareTo(Card c)
	{
		if (this.getId() < c.getId())  { return -1; }
		if (this.getId() > c.getId())  { return 1; }
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Card)) return false;
		return this.id == ((Card) obj).id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	public int getId() { return id; }
	public int getSuit() { return suit; }
	public int getRank() { return rank; }
	public int getValue()
	{
		if (rank < 3) { return 0; }
		return rank-2;
	}
}
