package com.me.sanguosha1v1.cards.base;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

public class Jiu extends Card {
    public Jiu(Integer point, ColorType color) {
        super("酒", CardType.BASIC, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        to.setDrunk(true);
        to.setHaveDrunk(true);
    }
}
