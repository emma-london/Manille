package com.abruce.manille;

import java.util.ArrayList;
import java.util.Collections;

import android.content.res.Resources;
import android.widget.ImageView;

public class Hand
{
	private ArrayList<Card> hand;
	private Card[] faceDown;
	private Card[] faceUp;

	private ImageView[] handIV;
	private ImageView[] tableTopIV;    // face-up card (top of stack)
	private ImageView[] tableBottomIV; // face-down card (bottom of stack, peeks behind top)

	private boolean visible;

	public Hand(ImageView[] h, ImageView[] tTop, ImageView[] tBottom, boolean v)
	{
		hand = new ArrayList<Card>();
		faceDown = new Card[4];
		faceUp = new Card[4];
		visible = v;

		handIV = h;
		tableTopIV = tTop;
		tableBottomIV = tBottom;

		for(int i=0; i<8; i++)
		{
			Card c = new Card();
			hand.add(c);
		}

		Collections.sort(hand);
		for (int i=0; i<8; i++)
		{
			if(visible) { handIV[i].setImageDrawable(hand.get(i).getDrawable()); }
			else { handIV[i].setImageDrawable(Card.getCardBack()); }
		}

		for(int i=0; i<4; i++)
		{
			faceDown[i] = new Card();
			faceUp[i] = new Card();

			// State 1: pair — bottom shows card back, top shows face-up card
			tableBottomIV[i].setImageDrawable(Card.getCardBack());
			tableBottomIV[i].setVisibility(android.view.View.VISIBLE);
			tableTopIV[i].setImageDrawable(faceUp[i].getDrawable());
		}

	}

	public boolean userCardPlayed(boolean table, int card, boolean myLead)
	{
		if (card < 0) { return false; }
		if (card > 7) { return false; }
		if (table && card > 3) { return false; }

		Card c;
		//Check if the selected card is populated or blank.
		if(table)
		{
			if(faceUp[card] == null) { return false; }
			c = faceUp[card];
		}
		else
		{
			if(card > hand.size()-1) { return false; }
			c = hand.get(card);
		}

		//If it's my lead then I can play any card - no need to check game rules.
		if (myLead) { return true; }

		//If I am following then we need to check game rules to see if it is a valid play.
		boolean grResult = GameRules.checkUserPlay(c);

		//The play was allowed
		return grResult;
	}

	public Card playCard(boolean table, int card)
	{
		//Called after userCardPlayed - this is to actually move the card around
		Card c;

		if (table)
		{
			c = faceUp[card];
			if (faceDown[card] == null)
			{
				// State 3: both cards gone — show blank, hide bottom
				tableTopIV[card].setImageDrawable(Card.getCardBlank());
				tableBottomIV[card].setVisibility(android.view.View.GONE);
				faceUp[card] = null;
			}
			else
			{
				// State 2: under-card revealed — flip faceDown to faceUp, hide bottom
				faceUp[card] = faceDown[card];
				faceDown[card] = null;
				tableTopIV[card].setImageDrawable(faceUp[card].getDrawable());
				tableBottomIV[card].setVisibility(android.view.View.GONE);
			}
		}
		else
		{
			c = hand.remove(card);
			//If the cards are user cards (visible) we need to shuffle the imageviews along.
			if (visible)
			{
				for (int i = card; i < hand.size(); i++)
				{
					//We need to shuffle along the cards from this point to the end.
					handIV[i].setImageDrawable(hand.get(i).getDrawable());
				}
			}
			//The next IV needs to be blanked out too.
			handIV[hand.size()].setImageDrawable(Card.getCardBlank());
		}

		return c;
	}

	public Card playCard(Card c)
	{
		//Another entry point for the playCard method above, this time just using a Card object
		for (int i=0; i<4; i++)
		{
			if (faceUp[i] == c) { return playCard(true, i); }
		}

		for (int i=0; i<hand.size(); i++)
		{
			if (hand.get(i)==c) { return playCard(false, i); }
		}

		return null;
	}

	public boolean checkForSuit(int suit)
	{
		//Checks to see if we have any playable cards of a particular suit in hand.
		//  NB we're looking for playable cards, so face down cards aren't checked.

		for(int i=0; i<4; i++)
		{
			if (faceUp[i] != null && faceUp[i].getSuit() == suit)
			{
				return true;
			}
		}

		for (Card c : hand)
		{
			if (c.getSuit() == suit)
			{
				return true;
			}
		}
		return false;
	}

	public ArrayList<Card> getAvailCards()
	{
		ArrayList<Card> avail = new ArrayList<Card> (hand);

		for (int i=0; i<4; i++)
		{
			if (faceUp[i] != null) { avail.add(faceUp[i]); }
		}

		return avail;
	}

	public int getMaxRankBySuit(int suit)
	{
		//If we don't have any cards in suit return -1.
		if (!checkForSuit(suit)) { return -1; }

		int maxRank = -1;

		/*
		for(int i=0; i<4; i++)
		{
			if (faceUp[i] != null &&
					faceUp[i].getSuit() == suit &&
					faceUp[i].getRank() > maxRank) { maxRank = faceUp[i].getRank(); }
		}

		for (Card c : hand)
		{
			if (c.getSuit() == suit && c.getRank() > maxRank) { maxRank = c.getRank(); }
		}
		*/

		ArrayList<Card> list = getCardsInSuit(suit);
		for(Card c: list)
		{
			if (c.getRank() > maxRank) { maxRank = c.getRank(); }
		}
		return maxRank;
	}

	public ArrayList<Card> getCardsInSuit(int suit)
	{
		ArrayList<Card> list = new ArrayList<Card>();

		for(int i=0; i<4; i++)
		{
			if (faceUp[i] != null && faceUp[i].getSuit() == suit) { list.add(faceUp[i]); }
		}

		for (Card c : hand)
		{
			if (c.getSuit() == suit) { list.add(c); }
		}
		return list;
	}

	public int cardCount()
	{
		int count=0;

		for(int i=0; i<4; i++)
		{
			if (faceUp[i] != null) { count++; }
			if (faceDown[i] != null) { count++; }
		}

		count += hand.size();
		return count;
	}
}
