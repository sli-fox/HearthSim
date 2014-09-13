package com.hearthsim.event.deathrattle;

import com.hearthsim.card.Deck;
import com.hearthsim.card.minion.Minion;
import com.hearthsim.exception.HSException;
import com.hearthsim.util.tree.HearthTreeNode;

public class DeathrattleSummonMinionAction extends DeathrattleAction {

	private final int numMinions_;
    private final Class<?> minionClass_;
    
	public DeathrattleSummonMinionAction(Class<?> minionClass, int numMnions) {
		numMinions_ = numMnions;
		minionClass_ = minionClass;
	}
	
	@Override
	public HearthTreeNode performAction(
			Minion minion,
			int thisPlayerIndex,
			HearthTreeNode boardState,
			Deck deckPlayer0,
			Deck deckPlayer1) 
		throws HSException
	{
		HearthTreeNode toRet = super.performAction(minion, thisPlayerIndex, boardState, deckPlayer0, deckPlayer1);
		for (int index = 0; index < numMinions_; ++index) {
            try {
            	Minion newMinion = (Minion)minionClass_.newInstance();
            	Minion placementTarget = toRet.data_.getCharacter(thisPlayerIndex, toRet.data_.getMinions(thisPlayerIndex).indexOf(minion)); //this minion can't be a hero
            	toRet.data_.removeMinion(thisPlayerIndex, minion);
				toRet = newMinion.summonMinion(thisPlayerIndex, placementTarget, toRet, deckPlayer0, deckPlayer1, false);
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				throw new HSException();
			}
		}
		return toRet;
	}
}