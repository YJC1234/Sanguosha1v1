package com.me.sanguosha1v1.cards.equip;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

public class Weapon extends Card {
    public Weapon(String name, Integer point, ColorType color) {
        super(name, CardType.EQUIPMENT, point, color);
    }


    @Override
    public void realActive(Player from, Player to) {
        //获取from的武器使用优先队列

        //装备到武器区
        Card oldWeapon = from.getWeapon();
        if (oldWeapon != null) {
            if (!from.isWeaponPriorityHigher(this.name, oldWeapon.getName())) {
                return;
            }
            from.discardOne(oldWeapon);
        }
        from.setWeapon(this);
    }
}
