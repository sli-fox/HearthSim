package com.hearthsim.card

import com.hearthsim.card.minion.Beast
import com.hearthsim.card.minion.Minion
import com.hearthsim.json.registry.CardRegistry
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

@Slf4j
class ImplementedCardList {

    private static ImplementedCardList instance

    public ArrayList<ImplementedCard> list_;
    public Map<Class<?>, ImplementedCard> map_;

    //todo: give singleton constructor, then use that in Card constructor

    public synchronized static ImplementedCardList getInstance() {
        if (!instance) {
            instance = new ImplementedCardList()
        }
        return instance
    }


    public class ImplementedCard implements Comparable<ImplementedCard> {

        public Class<?> cardClass_;
        public String name_;
        public String type_;
        public String charClass_;
        public String rarity_;
        public String text_;
        public boolean taunt_;
        public boolean divineShield_;
        public boolean windfury_;
        public boolean charge_;
        public boolean stealth_;
        public int mana_;
        public int attack_;
        public int health_;

        @Override
        public int compareTo(ImplementedCard o) {
            int result = Integer.compare(this.mana_, o.mana_);
            if (result == 0) {
                result = this.name_.compareTo(o.name_);
            }
            return result;
        }

    }

    ImplementedCardList() {
        list_ = new ArrayList<ImplementedCard>();
        map_ = new HashMap<Class<?>, ImplementedCard>();

        def slurper = new JsonSlurper()
        def implementedCardsFromJson = slurper.parse(getClass().getResourceAsStream('/implemented_cards.json'))

        def registry = CardRegistry.instance
        implementedCardsFromJson.each { implementedCardFromJson ->
            def cardDefinition = registry.cardByName(implementedCardFromJson.name)

            def className = implementedCardFromJson['class']
            def implementedCard = new ImplementedCard(
                    cardClass_: className,
                    name_: cardDefinition.name,
                    type_: cardDefinition.type.toLowerCase(),
                    charClass_: cardDefinition.playerClass.toLowerCase(),
                    rarity_: cardDefinition.rarity?.toLowerCase(),
                    mana_: cardDefinition.cost?: 0,
                    attack_: (cardDefinition.attack == null) ? -1 : cardDefinition.attack, //return -1 if it's 0. only if null.
                    health_: (cardDefinition.health == null) ? -1 : cardDefinition.health,
                    taunt_: cardDefinition.mechanics?.contains('Taunt') ?: false, //todo: extract mechanics as constants
                    divineShield_: cardDefinition.mechanics?.contains('Divine Shield') ?: false,
                    windfury_: cardDefinition.mechanics?.contains('Windfury') ?: false,
                    charge_: cardDefinition.mechanics?.contains('Charge') ?: false,
                    stealth_: cardDefinition.mechanics?.contains('Stealth') ?: false,

            )
            list_ << implementedCard

            def clazz = Class.forName(className)
            map_[clazz] = implementedCard
        }

    }

    public ArrayList<ImplementedCard> getCardList() {
        return list_;
    }

    // todo: existing clients need to filter out collectibles
    public ImplementedCard getCardForClass(Class<?> clazz) {
        def card =map_.get(clazz)
        if (!card) {
            if (clazz == Minion || clazz == Beast) {
                return null
            } else {
                throw new RuntimeException("unable to find card for class [$clazz]")
            }
        }

        return card;
    }
}
