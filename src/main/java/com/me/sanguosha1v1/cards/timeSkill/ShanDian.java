package com.me.sanguosha1v1.cards.timeSkill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * 闪电
 */
@Slf4j
public class ShanDian extends TimeSkillCard {
    public ShanDian(Integer point, ColorType color) {
        super("闪电", CardType.TIME_SKILL, point, color);
    }

    @Override
    protected boolean handleJungeCard(Player from, Player to, Card card) {
        if (card.isLightning()) {
            log.info("闪电炸了!对{}造成3点伤害", to.getName());
            to.hpChange(-3, true);
            return false;
        }
        return true;
    }
}
