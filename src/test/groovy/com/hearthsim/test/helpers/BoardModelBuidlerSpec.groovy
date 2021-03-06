package com.hearthsim.test.helpers

import com.hearthsim.card.minion.concrete.ElvenArcher
import com.hearthsim.model.BoardModel
import com.hearthsim.model.PlayerSide
import com.hearthsim.test.HearthBaseSpec

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static com.hearthsim.test.IsByteEqual.isByteEqual
import static org.hamcrest.CoreMatchers.*
import static spock.util.matcher.HamcrestSupport.expect

class BoardModelBuidlerSpec extends HearthBaseSpec {

    def "no-param building "() {
        when:
        BoardModel boardModel = new BoardModelBuilder().make {}

        then:
        expect boardModel, notNullValue()
        expect boardModel.currentPlayer, notNullValue()
        expect boardModel.waitingPlayer, notNullValue()
        [CURRENT_PLAYER, WAITING_PLAYER].each { PlayerSide side ->
            def playerModel = boardModel.modelForSide(side)
            //todo: see if there is an 'isTrue' matcher. better yet one for collections
            //todo: see if we want to use hamcrest at all...
            assert playerModel.hero != null
            assert playerModel.hero.armor == 0
            assert playerModel.hero.weaponCharge == 0
            assert playerModel.hero.health == 30

            assert playerModel.deck == null
            assert playerModel.hand.isEmpty()
            assert playerModel.minions.isEmpty()
            assert playerModel.overload == 0
            assert playerModel.spellDamage == 0
            assert playerModel.mana == 0
            assert playerModel.mana == 0
            assert boardModel.getFatigueDamage(side) == 1
        }
    }

    def 'build field from map'() {
        when:
        BoardModel boardModel = new BoardModelBuilder().make {
            currentPlayer {
                field([
                        [mana: 2, attack: 5, maxHealth: 3],
                ])
            }
        }

        then:
        def currentPlayer = boardModel.getCurrentPlayer()
        expect currentPlayer.minions.size(), is(1)

        def firstMinion = currentPlayer.minions[0]
        expect firstMinion.mana, isByteEqual(2)
        expect firstMinion.attack, isByteEqual(5)
        expect firstMinion.health, isByteEqual(3)
        expect firstMinion.maxHealth, isByteEqual(3)
        expect firstMinion.baseHealth, isByteEqual(3)
    }

    def 'build hand from class'() {
        when:
        BoardModel boardModel = new BoardModelBuilder().make {
            currentPlayer {
                hand([ElvenArcher])
            }
        }

        then:
        def currentPlayer = boardModel.getCurrentPlayer()
        expect currentPlayer.hand.size(), equalTo(1)
        expect currentPlayer.hand[0], isA(ElvenArcher)
    }

    def 'build hero attributes'() {
        when:
        BoardModel boardModel = new BoardModelBuilder().make {
            currentPlayer {
                mana(1)
                fatigueDamage(1)
                overload(1)
                spellDamage(1)
            }
        }

        then:
        def currentPlayer = boardModel.modelForSide(CURRENT_PLAYER)
        expect currentPlayer.mana, equalTo(1)
        expect currentPlayer.maxMana, equalTo(1)
        expect boardModel.getFatigueDamage(CURRENT_PLAYER), equalTo(1)

        def playerModel = currentPlayer
        expect playerModel.overload, isByteEqual(1)
        expect playerModel.spellDamage, isByteEqual(1)
    }

    def 'build current and waiting player'() {
        when:
        BoardModel boardModel = new BoardModelBuilder().make {
            currentPlayer {
                fatigueDamage(1)
            }
            waitingPlayer {
                fatigueDamage(2)
            }
        }

        then:
        expect boardModel.getFatigueDamage(CURRENT_PLAYER), equalTo(1)
        expect boardModel.getFatigueDamage(WAITING_PLAYER), equalTo(2)
    }


}
