package com.hearthsim.json.registry

import groovy.transform.ToString

@ToString
class CardDefinition {

    String id
    String name
    String type
    String set
    String rarity
    Integer cost
    String playerClass
    String text
    List<String> mechanics
    Integer attack
    Integer health
    Boolean collectible
}
