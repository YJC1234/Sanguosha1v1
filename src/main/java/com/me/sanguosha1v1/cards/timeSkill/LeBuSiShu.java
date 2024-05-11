package com.me.sanguosha1v1.cards.timeSkill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * 乐不思蜀
 */
@Slf4j
public class LeBuSiShu extends TimeSkillCard {
    public LeBuSiShu(Integer point, ColorType color) {
        super("乐不思蜀", CardType.TIME_SKILL, point, color);
    }


    @Override
    protected boolean handleJungeCard(Player from, Player to, Card card) {
        if (!card.isHeart()) {
            log.info("乐不思蜀生效，{}跳过出牌阶段", to.getName());
            to.setJumpingPlay(true);
        } else {
            log.info("乐不思蜀天过了！");
        }
        return false;
    }
}
