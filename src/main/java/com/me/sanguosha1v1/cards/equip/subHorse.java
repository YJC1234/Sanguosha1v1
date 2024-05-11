package com.me.sanguosha1v1.cards.equip;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

public class subHorse extends Card {

    public subHorse(Integer point, ColorType color) {
        super("-1马", CardType.EQUIPMENT, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        //装备到-1马区
        Card oldSubHorse = from.getSubHorse();
        if (oldSubHorse != null) {
            from.discardOne(oldSubHorse);
        }
        from.setSubHorse(this);
    }
}
