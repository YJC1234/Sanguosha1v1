package com.me.sanguosha1v1.cards.skill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

/**
 * 决斗
 */
public class JueDou extends Card {
    public JueDou(Integer point, ColorType color) {
        super("决斗", CardType.SKILL, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {

        boolean end = false;
        boolean turn = true;
        while (!end) {
            if (turn) {
                if (to.haveCardInHandorTreasure("杀")) {
                    to.doCard("杀");
                    turn = false;
                } else {
                    to.hpChange(-1);
                    end = true;
                }
            } else {
                if (from.haveCardInHandorTreasure("杀")) {
                    from.doCard("杀");
                    turn = true;
                } else {
                    from.hpChange(-1);
                    end = true;
                }
            }
        }
    }
}
