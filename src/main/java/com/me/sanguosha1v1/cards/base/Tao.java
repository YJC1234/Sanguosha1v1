package com.me.sanguosha1v1.cards.base;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

/**
 * 桃
 */
public class Tao extends Card {
    public Tao(Integer point, ColorType color) {
        super("桃", CardType.BASIC, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        to.hpChange(1);
    }
}
