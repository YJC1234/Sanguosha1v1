package com.me.sanguosha1v1.cards.skill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * 顺手牵羊
 */
@Slf4j
public class ShunShouQianYang extends Card {
    public ShunShouQianYang(Integer point, ColorType color) {
        super("顺手牵羊", CardType.SKILL, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        if (!to.haveAnyCard()) {
            log.info("{}没有任何牌，顺手牵羊失败了", to.getName());
            return;
        }
        //可被重写的顺手牵羊选择策略
        Card card = from.ChooseOneCard_ShunShouQianYang(to);
        to.removeOne(card);
        from.addCard(card);
        log.info("{}使用顺手牵羊，从{}处获得了{}", from.getName(), to.getName(), card);
    }
}
