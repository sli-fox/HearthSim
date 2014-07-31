package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Beast;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSInvalidPlayerIndexException;
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.event.attack.AttackAction;

import com.hearthsim.event.deathrattle.DeathrattleAction;


public class TimberWolf extends Beast {

	private static final String NAME = "Timber Wolf";
	private static final byte MANA_COST = 1;
	private static final byte ATTACK = 1;
	private static final byte HEALTH = 1;
	
	private static final boolean TAUNT = false;
	private static final boolean DIVINE_SHIELD = false;
	private static final boolean WINDFURY = false;
	private static final boolean CHARGE = false;
	
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	
	public TimberWolf() {
		this(
				MANA_COST,
				ATTACK,
				HEALTH,
				ATTACK,
				(byte)0,
				HEALTH,
				HEALTH,
				TAUNT,
				DIVINE_SHIELD,
				WINDFURY,
				CHARGE,
				false,
				false,
				false,
				false,
				SUMMONED,
				TRANSFORMED,
				false,
				false,
				null,
				null,
				true,
				false
			);
	}
	
	public TimberWolf(	
			byte mana,
			byte attack,
			byte health,
			byte baseAttack,
			byte extraAttackUntilTurnEnd,
			byte baseHealth,
			byte maxHealth,
			boolean taunt,
			boolean divineShield,
			boolean windFury,
			boolean charge,
			boolean hasAttacked,
			boolean hasWindFuryAttacked,
			boolean frozen,
			boolean silenced,
			boolean summoned,
			boolean transformed,
			boolean destroyOnTurnStart,
			boolean destroyOnTurnEnd,
			DeathrattleAction deathrattleAction,
			AttackAction attackAction,
			boolean isInHand,
			boolean hasBeenUsed) {
		
		super(
			NAME,
			mana,
			attack,
			health,
			baseAttack,
			extraAttackUntilTurnEnd,
			baseHealth,
			maxHealth,
			taunt,
			divineShield,
			windFury,
			charge,
			hasAttacked,
			hasWindFuryAttacked,
			frozen,
			silenced,
			summoned,
			transformed,
			destroyOnTurnStart,
			destroyOnTurnEnd,
			deathrattleAction,
			attackAction,
			isInHand,
			hasBeenUsed);
	}
	
	@Override
	public Object deepCopy() {
		return new TimberWolf(
				this.mana_,
				this.attack_,
				this.health_,
				this.baseAttack_,
				this.extraAttackUntilTurnEnd_,
				this.baseHealth_,
				this.maxHealth_,
				this.taunt_,
				this.divineShield_,
				this.windFury_,
				this.charge_,
				this.hasAttacked_,
				this.hasWindFuryAttacked_,
				this.frozen_,
				this.silenced_,
				this.summoned_,
				this.transformed_,
				this.destroyOnTurnStart_,
				this.destroyOnTurnEnd_,
				this.deathrattleAction_,
				this.attackAction_,
				this.isInHand_,
				this.hasBeenUsed_);
	}
	
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * Override for the temporary buff to attack
	 * 
	 * @param thisCardIndex The index (position) of the card in the hand
	 * @param playerIndex The index of the target player.  0 if targeting yourself or your own minions, 1 if targeting the enemy
	 * @param minionIndex The index of the target minion.
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * 
	 * @return The boardState is manipulated and returned
	 */
	@Override
	public HearthTreeNode use_core(
			int targetPlayerIndex,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		HearthTreeNode toRet = super.use_core(targetPlayerIndex, targetMinion, boardState, deckPlayer0, deckPlayer1);
		if (toRet != null) {
			for (Minion minion : toRet.data_.getMinions_p0()) {
				if (minion != this && minion instanceof Beast) {
					minion.setAttack((byte)(minion.getAttack() + 1));
				}
			}			
		}
		return toRet;
	}
	
	/**
	 * Called when this minion is silenced
	 * 
	 * Override for the aura effect
	 * 
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode silenced(int thisPlayerIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		HearthTreeNode toRet = boardState;
		if (!silenced_) {
			for (Minion minion : toRet.data_.getMinions(thisPlayerIndex)) {
				if (minion != this && minion instanceof Beast) {
					minion.setAttack((byte)(minion.getAttack() - 1));
				}
			}
		}
		toRet = super.silenced(thisPlayerIndex, toRet, deckPlayer0, deckPlayer1);
		return toRet;
	}
	
	/**
	 * Called when this minion dies (destroyed)
	 * 
	 * Override for the aura effect
	 * 
	 * @param thisPlayerIndex The player index of this minion
	 * @param thisMinionIndex The minion index of this minion
	 * @param boardState 
	 * @param deck
	 * @throws HSInvalidPlayerIndexException
	 */
	@Override
	public HearthTreeNode destroyed(int thisPlayerIndex, HearthTreeNode boardState, Deck deckPlayer0, Deck deckPlayer1) throws HSInvalidPlayerIndexException {
		HearthTreeNode toRet = super.destroyed(thisPlayerIndex, boardState, deckPlayer0, deckPlayer1);
		if (!silenced_) {
			for (Minion minion : toRet.data_.getMinions(thisPlayerIndex)) {
				if (minion != this && minion instanceof Beast) {
					minion.setAttack((byte)(minion.getAttack() - 1));
				}
			}
		}
		return super.destroyed(thisPlayerIndex, toRet, deckPlayer0, deckPlayer1);
	}
	
	private HearthTreeNode doBuffs(
			int thisMinionPlayerIndex,
			int placedMinionPlayerIndex,
			Minion placedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1) throws HSInvalidPlayerIndexException
	{
		if (placedMinionPlayerIndex != thisMinionPlayerIndex)
			return boardState;
		if (!silenced_) {
			if (placedMinion != this && placedMinion instanceof Beast)
				placedMinion.setAttack((byte)(placedMinion.getAttack() + 1));
		}
		return boardState;		
	}
	
	/**
	 * 
	 * Called whenever another minion is placed on board
	 * 
	 * Override for the aura effect
	 *
	 * @param thisMinionPlayerIndex The player index of this minion
	 * @param placedMinionPlayerIndex The index of the placed minion's player.
	 * @param placedMinion The placed minion
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionPlacedEvent(
			int thisMinionPlayerIndex,
			int placedMinionPlayerIndex,
			Minion placedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return this.doBuffs(thisMinionPlayerIndex, placedMinionPlayerIndex, placedMinion, boardState, deckPlayer0, deckPlayer1);
	}

	/**
	 * 
	 * Called whenever another minion comes on board
	 * 
	 * Override for the aura effect
	 *
	 * @param thisMinionPlayerIndex The player index of this minion
	 * @param summonedMinionPlayerIndex The index of the summoned minion's player.
	 * @param summonedMinion The summoned minion
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionSummonedEvent(
			int thisMinionPlayerIndex,
			int summonedMinionPlayerIndex,
			Minion summonedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return this.doBuffs(thisMinionPlayerIndex, summonedMinionPlayerIndex, summonedMinion, boardState, deckPlayer0, deckPlayer1);
	}
	
	/**
	 * 
	 * Called whenever another minion is summoned using a spell
	 * 
	 * @param thisMinionPlayerIndex The player index of this minion
	 * @param transformedMinionPlayerIndex The index of the transformed minion's player.
	 * @param transformedMinion The transformed minion (the minion that resulted from a transformation)
	 * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
	 * @param deckPlayer0 The deck of player0
	 * @param deckPlayer0 The deck of player1
	 * 
	 * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode minionTransformedEvent(
			int thisMinionPlayerIndex,
			int transformedMinionPlayerIndex,
			Minion transformedMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1)
		throws HSInvalidPlayerIndexException
	{
		return this.doBuffs(thisMinionPlayerIndex, transformedMinionPlayerIndex, transformedMinion, boardState, deckPlayer0, deckPlayer1);
	}
}
