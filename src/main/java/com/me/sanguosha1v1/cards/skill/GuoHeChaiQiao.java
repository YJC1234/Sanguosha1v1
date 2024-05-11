package com.me.sanguosha1v1.cards.skill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * 过河拆桥
 */
@Slf4j
public class GuoHeChaiQiao extends Card {
    public GuoHeChaiQiao(Integer point, ColorType color) {
        super("过河拆桥", CardType.SKILL, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        if (!to.haveAnyCard()) {
            log.info("{}没有任何牌，过河拆桥失败了", to.getName());
            return;
        }
        //可被重写的过河拆桥选择策略
        Card card = from.ChooseOneCard_GuoHeChaiQiao(to);
        to.discardOne(card);
        log.info("{}使用过河拆桥，从{}处拆掉了{}", from.getName(), to.getName(), card);
    }
}
