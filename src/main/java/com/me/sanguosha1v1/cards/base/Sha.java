package com.me.sanguosha1v1.cards.base;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

/**
 * 杀
 */
public class Sha extends Card {
    public Sha(Integer point, ColorType color) {
        super("杀", CardType.BASIC, point, color);
    }

    //这里重写是因为雷杀，火杀名字不同
    public Sha(String name, Integer point, ColorType color) {
        super(name, CardType.BASIC, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        if (from.isDrunk()) {
            from.setDrunk(false);
            to.handleSha(2, from, this);
        } else {
            to.handleSha(1, from, this);
        }
    }
}
