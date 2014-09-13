package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

public class CircleOfHealing extends SpellCard {

	private static final byte HEAL_AMOUNT = 4;
	
	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public CircleOfHealing(boolean hasBeenUsed) {
		super("Circle of Healing", (byte)0, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public CircleOfHealing() {
		this(false);
	}
	
	@Override
	public Object deepCopy() {
		return new CircleOfHealing(this.hasBeenUsed_);
	}

	
	/**
	 * 
	 * Circle of Healing
	 * 
	 * Heals all minions for 4
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (targetPlayerIndex > 0 || !(targetMinion instanceof Hero)) 
			return null;
		
		HearthTreeNode toRet = super.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		for (Minion minion : toRet.data_.getMinions_p0()) {
			toRet = minion.takeHeal(HEAL_AMOUNT, 0, toRet, deckPlayer0, deckPlayer1);
		}

		for (Minion minion : toRet.data_.getMinions_p1()) {
			toRet = minion.takeHeal(HEAL_AMOUNT, 1, toRet, deckPlayer0, deckPlayer1);
		}

		return toRet;
	}
}