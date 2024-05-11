package com.me.sanguosha1v1.cards.timeSkill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * 兵粮寸断
 */
@Slf4j
public class BingLiangCunDuan extends TimeSkillCard {
    public BingLiangCunDuan(Integer point, ColorType color) {
        super("兵粮寸断", CardType.TIME_SKILL, point, color);
    }


    @Override
    protected boolean handleJungeCard(Player from, Player to, Card card) {
        if (!card.isClub()) {
            log.info("兵粮寸断生效，{}跳过摸牌阶段", to.getName());
            to.setJumpingDraw(true);
        } else {
            log.info("兵粮寸断天过了！");
        }
        return false;
    }
}
