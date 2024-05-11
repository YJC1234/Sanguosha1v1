package com.me.sanguosha1v1.cards.skill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

/**
 * 桃园结义
 */
public class TaoYuanJieYi extends Card {

    public TaoYuanJieYi(Integer point, ColorType color) {
        super("桃园结义", CardType.SKILL, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        from.hpChange(1);
        to.hpChange(1);
    }
}
