package com.me.sanguosha1v1.cards.skill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * 火攻
 */
@Slf4j
public class HuoGong extends Card {

    public HuoGong(Integer point, ColorType color) {
        super("火攻", CardType.SKILL, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        //随即展示to1张手牌
        Card card = to.getRandomCardInHand();
        log.info("{}展示了手牌{}", to.getName(), card);
        //如果手牌中有同花色的牌，弃之并对其造成1点伤害
        for (Card handCard : from.getHandCards()) {
            if (handCard.getColor() == card.getColor()) {
                int damage = 1;
                from.discardOne(handCard);
                //如果to有藤甲，伤害+1
                if (to.armorIs("藤甲")) {
                    damage++;
                }
                log.info("{}弃掉了{}，对{}造成了{}点伤害", from.getName(), handCard, to.getName(), damage);
                to.hpChange(-damage);
                return;
            }
        }
    }
}
