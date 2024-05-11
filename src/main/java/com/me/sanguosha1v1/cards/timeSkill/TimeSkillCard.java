package com.me.sanguosha1v1.cards.timeSkill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * 延时锦囊
 */
@Slf4j
abstract public class TimeSkillCard extends Card {

    public TimeSkillCard(String name, CardType type, Integer point, ColorType color) {
        super(name, type, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        if (to.haveJudgeCard(this.getName())) {
            throw new RuntimeException("出现bug，对方已经有" + this.getName() + "了!!!");
        }
        to.addJudgeCard(this);
    }

    /**
     * 卡牌判定
     * 只有是判定失败或被无懈的闪电返回true 因为要额外处理
     */
    public boolean judge(Player from, Player to) {
        boolean isEffect = handle_WuXieKeJi(from, to);
        if (!isEffect) {
            return true;
        }
        Card card = to.drawOneForJudge();
        log.info("{}的{}的判定结果是{}", to.getName(), this, card);
        return handleJungeCard(from, to, card);
    }

    /**
     * 具体子类锦囊，如乐不思蜀等实现对判定结果的处理
     */
    protected abstract boolean handleJungeCard(Player from, Player to, Card card);

}
