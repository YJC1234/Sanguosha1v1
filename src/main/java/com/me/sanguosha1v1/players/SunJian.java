package com.me.sanguosha1v1.players;


import com.me.sanguosha1v1.Desk;
import com.me.sanguosha1v1.cards.Card;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
        /**
         * //如果在一次出牌决策中使用了牌，就重复决策,直到不使用牌为止
         */
        boolean isUseMuMa = false;
        while (true) {
            if (tryUseCard("无中生有")) {
                continue;
            }
            if (tryUseCard("铁索连环")) {
                continue;
            }
            if (hp >= 8) {
                hp = 7;//开个小挂
            }

            if (hp < maxHp - 3 && tryUseCard("桃")) {
                continue;
            }
            if (hp < maxHp - 3 && tryUseCard("桃园结义")) {
                continue;
            }

            if (tryEquipSomething()) {
                continue;
            }

            if ((haveSubHorse() || !enemy.haveAddHorse())
                    && enemy.haveJudgeCard("闪电")
                    && haveCardInHandorTreasure("顺手牵羊")) {
                List<Card> cards = getCardsInHandorTreasure("顺手牵羊");
                useCard(cards.getFirst(), enemy);
                continue;
            }

            if (enemy.haveJudgeCard("闪电")
                    && haveCardInHandorTreasure("过河拆桥")) {
                List<Card> cards = getCardsInHandorTreasure("过河拆桥");
                useCard(cards.getFirst(), enemy);
                continue;
            }

            if (!enemy.haveJudgeCard("乐不思蜀") && tryUseCard("乐不思蜀")) {
                continue;
            }
            if ((haveSubHorse() || !enemy.haveAddHorse()) && enemy.haveJudgeCard("兵粮寸断")
                    && tryUseCard("兵粮寸断")) {
                continue;
            }
            if (tryUseCard("南蛮入侵")) {
                continue;
            }
            if (tryUseCard("万箭齐发")) {
                continue;
            }

            //溢出刀，如果手牌小于体力就不进攻
            if (handCards.size() <= hp) {
                break;
            }

            if (handleUseSha()) {
                continue;
            }
            /**
             * 即将弃牌，如果有木马，存入1张进入木马，存牌策略与弃牌策略相反
             */
            if (haveTreasure() && !isUseMuMa && treasureCards.size() < 5) {
                Card card = chooseOne_muma();
                if (card != null) {
                    removeOne(card);
                    treasureCards.add(card);
                    isUseMuMa = true;
                }
            }
            /**
             * 即将弃牌，如果要弃的牌是顺拆，使用
             */
            Card discardOne = chooseDiscardOne();
            if (discardOne != null && enemy.haveAnyHandCardOrEquip()
                    && (discardOne.nameIs("顺手牵羊") || discardOne.nameIs("过河拆桥"))) {
                if (discardOne.nameIs("顺手牵羊") && !(haveSubHorse() || !enemy.haveAddHorse())) {
                    break;
                }
                useCard(discardOne, enemy);
                continue;
            }
            break;
        }
    }


    private boolean tryEquipSomething() {
        //上武器
        List<Card> weaponsInHand = getWeaponsInHand();
        for (Card card : weaponsInHand) {
            useCard(card, this);
        }
        //上防具
        List<Card> armorsInHand = getArmorsInHand();
        for (Card card : armorsInHand) {
            useCard(card, this);
        }
        //上-1/+1
        List<Card> horsesInHand = getSubHorsesInHand();
        if (!horsesInHand.isEmpty() && !haveSubHorse()) {
            useCard(horsesInHand.getFirst(), this);
            return true;
        }
        List<Card> addHorsesInHand = getAddHorsesInHand();
        if (!addHorsesInHand.isEmpty() && !haveAddHorse()) {
            useCard(addHorsesInHand.getFirst(), this);
            return true;
        }
        //上木马
        Card treasureInHand = getTreasureInHand();
        if (treasureInHand != null && !haveTreasure()) {
            useCard(treasureInHand, this);
            return true;
        }
        return false;
    }

    /**
     * 放入木马牌的策略
     */
    private Card chooseOne_muma() {
        List<String> priority = List.of("无懈可击", "桃", "+1马", "过河拆桥", "闪", "顺手牵羊");
        for (String cardName : priority) {
            Card find = handCards.stream().filter(card -> card.getName().equals(cardName)).findFirst().orElse(null);
            if (find != null) {
                return find;
            }
        }
        return null;
    }

    /**
     * 主动弃牌时的策略
     *
     * @return
     */
    @Override
    protected Card chooseDiscardOne() {
        if (!(haveArmor() || haveAddHorse() || haveTreasure()) || hp <= 5) {
            List<String> priority = List.of("无懈可击", "桃", "闪", "杀", "过河拆桥", "顺手牵羊");
            for (Card card : handCards) {
                if (!priority.contains(card.getName())) {
                    return card;
                }
            }
            priority = priority.reversed();
            for (String cardName : priority) {
                //如果手上有名称cardName的牌，选择
                List<Card> cards = handCards.stream().filter(card -> card.getName().equals(cardName)).toList();
                if (!cards.isEmpty()) {
                    return cards.getFirst();
                }
            }
        }
        //弃牌策略
        List<String> priority2 = List.of("无懈可击", "过河拆桥", "+1马", "藤甲", "桃", "顺手牵羊", "闪", "杀");
        for (Card card : handCards) {
            if (!priority2.contains(card.getName())) {
                return card;
            }
        }
        priority2 = priority2.reversed();
        for (String cardName : priority2) {
            //如果手上有名称cardName的牌，选择
            List<Card> cards = handCards.stream().filter(card -> card.getName().equals(cardName)).toList();
            if (!cards.isEmpty()) {
                if (cards.getFirst().nameIs("闪") && armorIs("藤甲") && cards.size() == 1) {
                    continue;
                }
                return cards.getFirst();
            }
        }
        //所有都不满足，弃掉第一张
        return handCards.getFirst();
    }

    @Override
    public Card ChooseOneCard_ShunShouQianYang(Player to) {
        //如果有闪电，先拆闪电
        if (enemy.haveJudgeCard("闪电")) {
            return enemy.getJudgeCard("闪电");
        }
        if (enemy.haveAnyCardInHand()) {
            return enemy.getRandomCardInHand();
        }
        if (enemy.haveWeapon()) {
            return enemy.getWeapon();
        }
        if (enemy.haveSubHorse()) {
            return enemy.getSubHorse();
        }
        if (enemy.haveAddHorse()) {
            return enemy.getAddHorse();
        }
        if (enemy.haveArmor()) {
            return enemy.getArmor();
        }
        if (enemy.haveTreasure()) {
            return enemy.getTreasure();
        }
        //不可能到这里
        log.error("ChooseOneCard_ShunShouQianYang出错");
        throw new RuntimeException("ChooseOneCard_ShunShouQianYang出错");
    }

    @Override
    public Card ChooseOneCard_GuoHeChaiQiao(Player to) {
        return ChooseOneCard_ShunShouQianYang(to);
    }

    /**
     * 无懈可击的策略
     */
    @Override
    public boolean isUse_WuXieKeJi(Card card, Player from, Player to) {
        if (from == this) { //自己用的锦囊，不打反无懈
            return to == this;
        }
        if (card.nameIs("无中生有")) {
            return false;
        }
        if (!(haveArmor() || haveAddHorse() || haveTreasure()) || hp <= 2) {
            if (card.nameIs("决斗")) {
                return false;
            }
            if (card.nameIs("南蛮入侵")) {
                //如果手上没杀 且没有藤甲，返回false,否则返回true
                return haveCardInHandorTreasure("杀") || armorIs("藤甲");
            }
            if (card.nameIs("万箭齐发")) {
                return haveCardInHandorTreasure("闪") || armorIs("藤甲");
            }
        }
        if (hp > 4) {
            return !(card.nameIs("顺手牵羊") || card.nameIs("过河拆桥"));
        }
        if (card.nameIs("决斗") || card.nameIs("顺手牵羊") || card.nameIs("过河拆桥")) {
            return false;
        }
        if (card.nameIs("南蛮入侵")) {
            //如果手上没杀 且没有藤甲，返回false,否则返回true
            return haveCardInHandorTreasure("杀") || armorIs("藤甲");
        }
        if (card.nameIs("万箭齐发")) {
            return haveCardInHandorTreasure("闪") || armorIs("藤甲");
        }
        return true;
    }
}
