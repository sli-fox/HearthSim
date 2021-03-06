package com.hearthsim.card.spellcard.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.card.spellcard.SpellCard;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.tree.HearthTreeNode;

public class MarkOfTheWild extends SpellCard {


	/**
	 * Constructor
	 * 
	 * @param hasBeenUsed Whether the card has already been used or not
	 */
	public MarkOfTheWild(boolean hasBeenUsed) {
		super((byte)2, hasBeenUsed);
	}

	/**
	 * Constructor
	 * 
	 * Defaults to hasBeenUsed = false
	 */
	public MarkOfTheWild() {
		this(false);
	}

	@Override
	public Object deepCopy() {
		return new MarkOfTheWild(this.hasBeenUsed);
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * This card heals the target minion up to full health and gives it taunt.  Cannot be used on heroes.
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	@Override
	protected HearthTreeNode use_core(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		if (isHero(targetMinion)) {
			//cant't use it on the heroes
			return null;
		}
		
		HearthTreeNode toRet = super.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);
		if (toRet != null) {
			targetMinion.setAttack((byte)(targetMinion.getAttack() + 2));
			targetMinion.setHealth((byte)(targetMinion.getHealth() + 2));
			targetMinion.setMaxHealth((byte)(targetMinion.getMaxHealth() + 2));
			targetMinion.setTaunt(true);
		}
		return toRet;
	}
}
