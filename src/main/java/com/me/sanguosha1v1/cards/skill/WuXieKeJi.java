package com.me.sanguosha1v1.cards.skill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

/**
 * 无懈可击
 */
public class WuXieKeJi extends Card {
    public WuXieKeJi(Integer point, ColorType color) {
        super("无懈可击", CardType.SKILL, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
    }
}
