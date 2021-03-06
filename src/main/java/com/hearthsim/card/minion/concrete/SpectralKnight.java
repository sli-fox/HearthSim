package com.hearthsim.card.minion.concrete;

import com.hearthsim.card.minion.Minion;

public class SpectralKnight extends Minion {

	private static final boolean HERO_TARGETABLE = false;
	private static final boolean SUMMONED = false;
	private static final boolean TRANSFORMED = false;
	private static final byte SPELL_DAMAGE = 0;
	
	public SpectralKnight() {
        super();
        spellDamage_ = SPELL_DAMAGE;
        heroTargetable_ = HERO_TARGETABLE;
        summoned_ = SUMMONED;
        transformed_ = TRANSFORMED;
	}
}
