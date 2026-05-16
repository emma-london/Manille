package com.abruce.manille;

import java.util.Random;


import android.os.Bundle;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ManilleActivity extends Activity implements View.OnClickListener
{

	private Hand myHand;
	private Hand oppHand;

	private ImageView[] myHandIVs;
	private ImageView[] myTableIVs;
	private ImageView[] oppHandIVs;
	private ImageView[] oppTableIVs;

	private ImageView myPlayedIV;
	private ImageView oppPlayedIV;
	private ImageView trumpsIV;

	private TextView scoreTV;

	private Card myPlayedCard;
	private Card oppPlayedCard;

	private int trumpSuit;
	private boolean myTurn;  //Who plays next - true = player, false = opponent
	private boolean myLead;  //Who lead for this trick - true = player

	private int myScore;
	private int oppScore;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manille);

		Resources res = getResources();

		//iv.setImageResource(getResources().getIdentifier("drawable/card01.png", null, null));
		//iv.setImageDrawable(getResources().getDrawable(R.drawable.card01));
		Card.setCardBack(res.getDrawable(R.drawable.cardback));
		Card.setCardBlank(res.getDrawable(R.drawable.card_blank));

		ManilleApp.setActivity(this);

		setupIVs();

		scoreTV = (TextView)findViewById(R.id.textView2);

		/*
		Card c= new Card();

		int id=res.getIdentifier(c.getResourceName(), "drawable", getPackageName());
		iv.setImageDrawable(res.getDrawable(id));
		*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.manille, menu);
		return true;
	}

	@Override
	public void onClick(View v)
	{
		if (!myTurn)
		{
			Toast.makeText(getApplicationContext(), "Not your turn", Toast.LENGTH_SHORT).show();
			return;
		}

		//1 id the clicked card
		//2 check no. 1 - is a click valid now?
		//3 pass the click to myHand
		//  myHand performs check no. 2 - is this card a valid move?
		//4 If play isn't valid do something (flash red, toast?)
		//4b If play ok do the visuals
		//     i    transfer the Card object
		//     ii   update the table IVs
		//     iii  handle the gap where the card used to be (flip table card/show blank)
		//5 hand execution over to turn manager

		boolean table=false;
		int card=-1;


		//R.id.IVMyHand1-8, R.id.IVMyTable1-4:
		int id = v.getId();

		if (id == R.id.IVMyHand1) {
			table = false;
			card = 0;
		} else if (id == R.id.IVMyHand2) {
			table = false;
			card = 1;
		} else if (id == R.id.IVMyHand3) {
			table = false;
			card = 2;
		} else if (id == R.id.IVMyHand4) {
			table = false;
			card = 3;
		} else if (id == R.id.IVMyHand5) {
			table = false;
			card = 4;
		} else if (id == R.id.IVMyHand6) {
			table = false;
			card = 5;
		} else if (id == R.id.IVMyHand7) {
			table = false;
			card = 6;
		} else if (id == R.id.IVMyHand8) {
			table = false;
			card = 7;
		} else if (id == R.id.IVMyTable1) {
			table = true;
			card = 0;
		} else if (id == R.id.IVMyTable2) {
			table = true;
			card = 1;
		} else if (id == R.id.IVMyTable3) {
			table = true;
			card = 2;
		} else if (id == R.id.IVMyTable4) {
			table = true;
			card = 3;
		}



		if (myHand.userCardPlayed(table, card, myLead))
		{
			//OK to play this card.
			Card c = myHand.playCard(table, card);
			myPlayedCard = c;
			myPlayedIV.setImageDrawable(c.getDrawable());
			myPlayedIV.invalidate();

			//Is the trick complete?
			final boolean result;
			if (oppPlayedCard != null)
			{
				result = !GameRules.whoWins(oppPlayedCard, myPlayedCard);
			}
			else
			{
				oppPlayedCard = AI.follow(myPlayedCard);
				oppPlayedIV.setImageDrawable(oppPlayedCard.getDrawable());
				oppPlayedIV.invalidate();

				result = GameRules.whoWins(myPlayedCard, oppPlayedCard);
			}

			String s;
			if (result) { s = "You win a trick"; }
			else {s = "You lose a trick"; }

			Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

			oppPlayedIV.postDelayed(new Runnable() {
				@Override
				public void run()
				{ nextHand(result); } }, 2500);


		}
		else
		{
			//Not a valid move
			//TODO: Make this check more granular and give an explanation of why it's not valid.
			Toast.makeText(getApplicationContext(), "Not a valid move", Toast.LENGTH_SHORT).show();
			return;
		}

	}

	private void nextHand(boolean turn)
	{
		int cardScore = myPlayedCard.getValue() + oppPlayedCard.getValue();
		if (turn) { myScore += cardScore; }
		else { oppScore += cardScore; }

		scoreTV.setText(Integer.toString(myScore) + " v " + Integer.toString(oppScore));

		myPlayedCard = null;
		oppPlayedCard = null;

		myPlayedIV.setImageDrawable(Card.getCardBlank());
		oppPlayedIV.setImageDrawable(Card.getCardBlank());

		if (myHand.cardCount() == 0)
		{
			String s;
			if (myScore>oppScore) {s=" - you win!";}
			else if (myScore<oppScore) {s=" - you lose";}
			else { s=" - it's a draw"; }
			Toast.makeText(getApplicationContext(), "Game Over"+s, Toast.LENGTH_SHORT).show();
			return;
		}
		setTurn(turn);
		setLead(turn);

		if (!turn)
		{
			Card c = AI.lead();
			oppPlayedCard = c;
			oppPlayedIV.setImageDrawable(c.getDrawable());

			setTurn(true);
		}
	}

	private void setupIVs()
	{
		myHandIVs = new ImageView[8];
		myTableIVs = new ImageView[4];

		oppHandIVs = new ImageView[8];
		oppTableIVs = new ImageView[4];

		myHandIVs[0] = (ImageView)findViewById(R.id.IVMyHand1);
		myHandIVs[1] = (ImageView)findViewById(R.id.IVMyHand2);
		myHandIVs[2] = (ImageView)findViewById(R.id.IVMyHand3);
		myHandIVs[3] = (ImageView)findViewById(R.id.IVMyHand4);
		myHandIVs[4] = (ImageView)findViewById(R.id.IVMyHand5);
		myHandIVs[5] = (ImageView)findViewById(R.id.IVMyHand6);
		myHandIVs[6] = (ImageView)findViewById(R.id.IVMyHand7);
		myHandIVs[7] = (ImageView)findViewById(R.id.IVMyHand8);

		myTableIVs[0] = (ImageView)findViewById(R.id.IVMyTable1);
		myTableIVs[1] = (ImageView)findViewById(R.id.IVMyTable2);
		myTableIVs[2] = (ImageView)findViewById(R.id.IVMyTable3);
		myTableIVs[3] = (ImageView)findViewById(R.id.IVMyTable4);

		oppHandIVs[0] = (ImageView)findViewById(R.id.IVOppHand1);
		oppHandIVs[1] = (ImageView)findViewById(R.id.IVOppHand2);
		oppHandIVs[2] = (ImageView)findViewById(R.id.IVOppHand3);
		oppHandIVs[3] = (ImageView)findViewById(R.id.IVOppHand4);
		oppHandIVs[4] = (ImageView)findViewById(R.id.IVOppHand5);
		oppHandIVs[5] = (ImageView)findViewById(R.id.IVOppHand6);
		oppHandIVs[6] = (ImageView)findViewById(R.id.IVOppHand7);
		oppHandIVs[7] = (ImageView)findViewById(R.id.IVOppHand8);

		oppTableIVs[0] = (ImageView)findViewById(R.id.IVOppTable1);
		oppTableIVs[1] = (ImageView)findViewById(R.id.IVOppTable2);
		oppTableIVs[2] = (ImageView)findViewById(R.id.IVOppTable3);
		oppTableIVs[3] = (ImageView)findViewById(R.id.IVOppTable4);

		for(int i=0; i<8; i++)
		{
			myHandIVs[i].setOnClickListener(this);
			if (i<4) { myTableIVs[i].setOnClickListener(this); }
		}

		myPlayedIV = (ImageView)findViewById(R.id.IVPlay1);
		oppPlayedIV = (ImageView)findViewById(R.id.IVPlay2);
		trumpsIV = (ImageView)findViewById(R.id.IVTrumps);
	}

	public void newGame()
	{
		Card.resetUnusedCardIds();

		myHand = new Hand(myHandIVs, myTableIVs, true);
		oppHand = new Hand(oppHandIVs, oppTableIVs, false);

		myScore = 0;
		oppScore = 0;
		/*
		Card c= new Card();
		ImageView iv = (ImageView)findViewById(R.id.IVPlay1);

		int id=res.getIdentifier(c.getResourceName(), "drawable", getPackageName());
		iv.setImageDrawable(res.getDrawable(id));
		*/


		Random rnd = new Random();

		//TODO trump suit choice
		setTrumpSuit(rnd.nextInt(4));

		setTurn(rnd.nextBoolean());
		setLead(getTurn());

		if (!myLead)
		{
			Card c = AI.lead();
			oppPlayedCard = c;
			oppPlayedIV.setImageDrawable(c.getDrawable());

			setTurn(true);
		}


	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.new_game) {
			newGame();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	public void setTrumpSuit(int suit)
	{
		//0=Hearts, 1=Diamonds, 2=Clubs, 3=Spades
		//TODO Throw exception if suit is out of range.
		trumpSuit = suit;

		int d=0;

		switch (suit)
		{
			case 0: d = R.drawable.suit0; break;
			case 1: d = R.drawable.suit1; break;
			case 2: d = R.drawable.suit2; break;
			case 3: d = R.drawable.suit3; break;
			default: break;
		}

		trumpsIV.setImageDrawable(null);

//		((BitmapDrawable)trumpsIV.getDrawable()).getBitmap().recycle();

//		Resources res = getResources();
//		String suitName = "suit" + String.format("%01d", suit);
//		int id=res.getIdentifier(suitName, "drawable", getPackageName());
		trumpsIV.setImageResource(d);



	}

	public void setTurn(boolean t)
	{
		myTurn = t;

		ImageView iOpp = (ImageView)findViewById(R.id.IVTurnOpp);
		ImageView iMy = (ImageView)findViewById(R.id.IVTurnMy);
		if (myTurn)
		{
			//Set the bottom arrow visible, top invisible
			iOpp.setVisibility(View.INVISIBLE);
			iMy.setVisibility(View.VISIBLE);
		}
		else
		{
			//Set the top arrow visible, bottom invisible
			iMy.setVisibility(View.INVISIBLE);
			iOpp.setVisibility(View.VISIBLE);
		}
	}

	public boolean getTurn()	{ return myTurn; }
	public void setLead(boolean l)	{ myLead = l;  }
	public boolean getLead()	{ return myLead; }

	public Card getmyPlayedCard() {return myPlayedCard;}
	public Card getoppPlayedCard() {return oppPlayedCard;}
	public int getTrumpSuit() {return trumpSuit;}
	public Hand getMyHand() {return myHand;}
	public Hand getOppHand() {return oppHand;}
}
