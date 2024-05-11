package com.me.sanguosha1v1.cards.skill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

/**
 * 万箭齐发
 */
public class WanJianQiFa extends Card {
    public WanJianQiFa(Integer point, ColorType color) {
        super("万箭齐发", CardType.SKILL, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        if (to.armorIs("藤甲")) {
            return;
        }

        if (to.haveCardInHandorTreasure("闪")) {
            to.doCard("闪");
        } else {
            to.hpChange(-1);
        }
    }

}
