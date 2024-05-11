package com.me.sanguosha1v1.cards.equip;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

public class Armor extends Card {
    public Armor(String name, Integer point, ColorType color) {
        super(name, CardType.EQUIPMENT, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        //装备到防具区
        Card oldArmor = from.getArmor();
        if (oldArmor != null) {
            from.discardOne(oldArmor);
        }
        from.setArmor(this);
    }
}
