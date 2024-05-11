package com.me.sanguosha1v1.cards.base;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

/**
 * 闪
 */
public class Shan extends Card {
    public Shan(Integer point, ColorType color) {
        super("闪", CardType.BASIC, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        //什么都不做，不会使用"闪"
    }
}
