package com.hearthsim.card.minion.heroes;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Hero;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.BoardState;
import com.hearthsim.util.DeepCopyable;
import com.hearthsim.util.HearthTreeNode;

public class Priest extends Hero {
	
	public Priest() {
		this("", (byte)30);
	}

	public Priest(String name, byte health) {
		this(name, (byte)0, (byte)0, health, (byte)0, (byte)0, false, false, false, false, false);
	}
	
	public Priest(
			String name,
			byte attack,
			byte extraAttackUntilTurnEnd,
			byte health,
			byte armor,
			byte weaponCharge,
			boolean windFury,
			boolean hasAttacked,
			boolean hasWindFuryAttacked,
			boolean frozen,
			boolean hasBeenUsed) {
	
		super(name, attack, extraAttackUntilTurnEnd, health, armor, weaponCharge, windFury, hasAttacked, hasWindFuryAttacked, frozen, hasBeenUsed);
	}
	
	@Override
	public DeepCopyable deepCopy() {
		return new Priest(
				this.name_, 
				this.attack_,
				this.extraAttackUntilTurnEnd_,
				this.health_,
				this.armor_,
				this.weaponCharge_,
				this.windFury_,
				this.hasAttacked_,
				this.hasWindFuryAttacked_,
				this.frozen_,
				this.hasBeenUsed_
				);
	}
	
	/**
	 * Use the hero ability on a given target
	 * 
	 * Priest: Heals a target for 2
	 * 
	 * @param thisPlayerIndex The player index of the hero
	 * @param targetPlayerIndex The player index of the target character
	 * @param targetMinionIndex The minion index of the target character
	 * @param boardState
	 * @param deck
	 * @return
	 */
	@Override
	public HearthTreeNode<BoardState> useHeroAbility(
			int thisPlayerIndex,
			int targetPlayerIndex,
			int targetMinionIndex,
			HearthTreeNode<BoardState> boardState,
			Deck deck)
		throws HSException
	{
		HearthTreeNode<BoardState> toRet = super.useHeroAbility(thisPlayerIndex, targetPlayerIndex, targetMinionIndex, boardState, deck);
		if (toRet != null) {
			if (targetMinionIndex == 0) {
				Minion target = boardState.data_.getHero(targetPlayerIndex);
				target.takeHeal((byte)2, targetPlayerIndex, targetMinionIndex, boardState, deck);
			} else {
				Minion target = boardState.data_.getMinion(targetPlayerIndex, targetMinionIndex - 1);
				target.takeHeal((byte)2, targetPlayerIndex, targetMinionIndex, boardState, deck);
			}
		}
		return toRet;
	}
	
}