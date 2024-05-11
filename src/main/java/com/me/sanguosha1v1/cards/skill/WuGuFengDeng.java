package com.me.sanguosha1v1.cards.skill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

/**
 * 五谷丰登
 */
public class WuGuFengDeng extends Card {

    public WuGuFengDeng(Integer point, ColorType color) {
        super("五谷丰登", CardType.SKILL, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        //TODO 五谷目前两边摸1
        from.draw(1);
        to.draw(1);
    }
}
