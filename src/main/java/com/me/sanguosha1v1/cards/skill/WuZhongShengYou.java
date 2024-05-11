package com.me.sanguosha1v1.cards.skill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

/**
 * 无中生有
 */
public class WuZhongShengYou extends Card {

    public WuZhongShengYou(Integer point, ColorType color) {
        super("无中生有", CardType.SKILL, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        from.draw(2);
    }
}
