package com.hearthsim.card.spellcard;

import com.hearthsim.card.Card;
import com.hearthsim.util.BoardState;
import com.json.JSONObject;

public class SpellTheCoin extends SpellCard {

	public SpellTheCoin(boolean hasBeenUsed) {
		super("The Coin", (byte)0, hasBeenUsed);
	}

	public SpellTheCoin() {
		super("The Coin", (byte)0, false);
	}

	@Override
	public boolean equals(Object other)
	{
	   if (other == null)
	   {
	      return false;
	   }

	   if (this.getClass() != other.getClass())
	   {
	      return false;
	   }
	   
	   return true;
	}
	
	@Override
	public Object deepCopy() {
		return new SpellTheCoin(this.hasBeenUsed());
	}

	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * @param playerIndex
	 * @param minionIndex
	 * @param boardState
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public BoardState useOn(int playerIndex, int minionIndex, BoardState boardState) {
		int newMana = boardState.getMana_p0();
		newMana = newMana >= 10 ? newMana : newMana + 1;
		boardState.setMana_p0(newMana);
		return boardState;
	}

	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "SpellTheCoin");
		return json;
	}
}