package com.me.sanguosha1v1.cards.skill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;

/**
 * 铁索连环
 */
public class TieSuoLianHuan extends Card {
    public TieSuoLianHuan(Integer point, ColorType color) {
        super("铁索连环", CardType.SKILL, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        from.draw(1);
    }
}
