package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class SilverHandRecruit extends Minion {

	private static final boolean HERO_TARGETABLE = true;
	private static final boolean SUMMONED = true;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public SilverHandRecruit() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
}
