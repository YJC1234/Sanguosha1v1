package com.me.sanguosha1v1.cards.skill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

/**
 * 南蛮入侵
 */
public class NanManRuQin extends Card {

    public NanManRuQin(Integer point, ColorType color) {
        super("南蛮入侵", CardType.SKILL, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        if (to.armorIs("藤甲")) {
            return;
        }
        if (to.haveCardInHandorTreasure("杀")) {
            to.doCard("杀");
        } else {
            to.hpChange(-1);
        }
    }
}
