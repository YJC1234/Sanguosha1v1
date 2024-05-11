package com.me.sanguosha1v1.players;


import com.me.sanguosha1v1.Desk;
import com.me.sanguosha1v1.cards.Card;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SunJian extends Player {
    public SunJian(Desk desk) {
        super(desk);
    }


    /**
     * 英魂：准备阶段，若你已受伤，你可以选择一名其他角色并选择一项：
     * 1.令其摸X张牌，然后弃置一张牌；2.令其摸一张牌，然后弃置X张牌（X为你已损失的体力值）。
     */

    private void handle_yinghun() {
        //单挑永远只对敌方使用
        int damageHp = maxHp - hp;
        if (damageHp <= 1) {
            return;
        }
        log.info("{}发动技能“英魂，选择对方摸1弃{}”", name, damageHp);
        enemy.draw(1);
        enemy.discard(damageHp);
    }

    @Override
    protected void prepareStage() {
        super.prepareStage();
        log.info("准备阶段，{}发动技能英魂", name);
        handle_yinghun();
    }

    /**
     * 孙坚策略部分目前未完成
     */

    @Override
    public void playStage() {

    }

    @Override
    protected Card chooseDiscardOne() {
        return null;
    }

    @Override
    public Card ChooseOneCard_ShunShouQianYang(Player to) {
        return null;
    }

    @Override
    public Card ChooseOneCard_GuoHeChaiQiao(Player to) {
        return null;
    }

    @Override
    public boolean isUse_WuXieKeJi(Card card, Player from, Player to) {
        return false;
    }
}
