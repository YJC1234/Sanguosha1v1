package com.me.sanguosha1v1.cards.equip;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

/**
 * 木牛流马
 */
public class MuNiuLiuMa extends Card {
    public MuNiuLiuMa(Integer point, ColorType color) {
        super("木牛流马", CardType.EQUIPMENT, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        //装备到宝物区
        Card oldTreasure = from.getTreasure();
        if (oldTreasure != null) {
            from.discardOne(oldTreasure);
        }
        from.setTreasure(this);
    }
}
