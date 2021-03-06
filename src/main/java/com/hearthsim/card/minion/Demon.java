package com.hearthsim.card.minion;

import org.json.JSONObject;


public class Demon extends Minion {

    public Demon(String name, byte mana, byte attack, byte health, byte baseAttack, byte baseHealth, byte maxHealth) {
        super(name, mana, attack, health, baseAttack, baseHealth, maxHealth);
    }

    public Demon() {
    }

    public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("type", "Demon");
		return json;
	}
}
