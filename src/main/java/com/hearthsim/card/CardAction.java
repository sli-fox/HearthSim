package com.hearthsim.card;

import java.util.ArrayList;

import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.model.PlayerSide;
import com.hearthsim.util.factory.BoardStateFactoryBase;
import com.hearthsim.util.tree.HearthTreeNode;

public class CardAction {

	private Card card;
	
	public CardAction(Card card) {
		this.card = card;
	}
			
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 *
     *
     *
     * @param side
     * @param targetMinion The target minion (can be a Hero)
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     * @param deckPlayer0 The deck for player0
     * @param deckPlayer1 The deck for player1
     * @param singleRealizationOnly For cards with random effects, setting this to true will return only a single realization of the random event.
     *
     * @return The boardState is manipulated and returned
	 */
	public HearthTreeNode useOn(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		//A generic card does nothing except for consuming mana
		HearthTreeNode toRet = this.use_core(side, targetMinion, boardState, deckPlayer0, deckPlayer1, singleRealizationOnly);

		//Notify all other cards/characters of the card's use
		if (toRet != null) {
			ArrayList<Minion> tmpList = new ArrayList<Minion>(7);
            for (Card card : toRet.data_.getCurrentPlayerHand()) {
                toRet = card.otherCardUsedEvent(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this.card, toRet, deckPlayer0, deckPlayer1);
            }
			toRet = toRet.data_.getCurrentPlayerHero().otherCardUsedEvent(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this.card, toRet, deckPlayer0, deckPlayer1);
			{
                for (Minion minion : PlayerSide.CURRENT_PLAYER.getPlayer(toRet).getMinions()) {
                    tmpList.add(minion);
                }
				for (Minion minion : tmpList) {
					if (!minion.isSilenced())
						toRet = minion.otherCardUsedEvent(PlayerSide.CURRENT_PLAYER, PlayerSide.CURRENT_PLAYER, this.card, toRet, deckPlayer0, deckPlayer1);
				}
			}
            for (Card card : toRet.data_.getWaitingPlayerHand()) {
                toRet = card.otherCardUsedEvent(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this.card, toRet, deckPlayer0, deckPlayer1);
            }
			toRet = toRet.data_.getWaitingPlayerHero().otherCardUsedEvent(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this.card, toRet, deckPlayer0, deckPlayer1);
			{
				tmpList.clear();
                for (Minion minion : PlayerSide.WAITING_PLAYER.getPlayer(toRet).getMinions()) {
                    tmpList.add(minion);
                }
				for (Minion minion : tmpList) {
					if (!minion.isSilenced())
						toRet = minion.otherCardUsedEvent(PlayerSide.WAITING_PLAYER, PlayerSide.CURRENT_PLAYER, this.card, toRet, deckPlayer0, deckPlayer1);
				}
			}

			//check for and remove dead minions
			toRet = BoardStateFactoryBase.handleDeadMinions(toRet, deckPlayer0, deckPlayer1);			
		}
		
		
		return toRet;
	}
	
	/**
	 * 
	 * Use the card on the given target
	 * 
	 * This is the core implementation of card's ability
	 * 
	 *
     *
     * @param side
     * @param boardState The BoardState before this card has performed its action.  It will be manipulated and returned.
     *
     * @return The boardState is manipulated and returned
	 */
	protected HearthTreeNode use_core(
			PlayerSide side,
			Minion targetMinion,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1,
			boolean singleRealizationOnly)
		throws HSException
	{
		//A generic card does nothing except for consuming mana
		boardState.data_.getCurrentPlayer().subtractMana(this.card.mana_);
		boardState.data_.removeCard_hand(this.card);
		return boardState;
	}
	
}
