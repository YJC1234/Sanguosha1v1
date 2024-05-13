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
        List<String> priority = List.of("无懈可击", "过河拆桥");
        for (String cardName : priority) {
            Card find = handCards.stream().filter(card -> card.getName().equals(cardName)).findFirst().orElse(null);
            if (find != null) {
                return find;
            }
        }
        return null;
    }

    /**
     * 手上由多少张是name的牌
     */
    private int getHandCardCount(String cardName) {
        return (int) handCards.stream().filter(card -> card.nameIs(cardName)).count();
    }

    private Card getCardFromPriority(List<String> priority) {
        for (Card card : handCards) {
            if (!priority.contains(card.getName())) {
                //不在Priority里，非核心牌，直接扔掉
                return card;
            }
        }
        priority = priority.reversed();
        for (String cardName : priority) {
            //如果手上有名称cardName的牌，选择
            List<Card> cards = handCards.stream().filter(card -> card.getName().equals(cardName)).toList();
            if (!cards.isEmpty()) {
                Card card = cards.isEmpty() ? null : cards.getFirst();
                //留的全是无懈，这个牌可以扔掉了
                if (getHandCardCount("无懈可击") == hp) {
                    return card;
                }
                //只有1个桃，保留
                if (card.nameIs("桃") && getCardsInHandorTreasure("桃").size() == 1) {
                    continue;
                }
                //桃和无懈加起来超过hp，其他就可以扔掉了
                if (getHandCardCount("桃") + getHandCardCount("无懈可击") > hp) {
                    return card;
                }
                //没+1，保留1闪
                if (card.nameIs("闪") && getCardsInHandorTreasure("闪").size() == 1 && !haveAddHorse()) {
                    continue;
                }
                //保留1杀，防决斗/南蛮
                if (card.nameIs("杀") && getCardsInHandorTreasure("杀").size() == 1) {
                    continue;
                }
                return cards.getFirst();
            }
        }
        return handCards.getFirst();
    }


    /**
     * 主动弃牌时的策略
     * 核心逻辑
     *
     * @return
     */
    @Override
    protected Card chooseDiscardOne() {
        List<String> priority;


        if (haveAddHorse() || armorIs("藤甲")) {
            priority = List.of("无懈可击", "过河拆桥", "+1马", "-1马", "桃", "杀");
            return getCardFromPriority(priority);
        }
        //这时候没防装了，需要全力保护状态。
        if (hp < 7) {
            priority = List.of("桃", "闪", "无懈可击", "+1马", " 过河拆桥", "杀", "顺手牵羊");
            return getCardFromPriority(priority);
        }
        priority = List.of("无懈可击", "过河拆桥", "桃", "闪", "顺手牵羊", "+1马", "-1马", "顺手牵羊");
        return getCardFromPriority(priority);
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
        if (!(haveArmor() || haveAddHorse() || haveTreasure())) {
            if (card.nameIs("决斗")) {
                return getCardsInHandorTreasure("桃") != null;
            }
            if (card.nameIs("南蛮入侵")) {
                //如果手上没杀 且没有藤甲，返回false,否则返回true
                return haveCardInHandorTreasure("杀") || armorIs("藤甲")
                        || getCardsInHandorTreasure("桃") != null;
            }
            if (card.nameIs("万箭齐发")) {
                return haveCardInHandorTreasure("闪") || armorIs("藤甲")
                        || getCardsInHandorTreasure("桃") != null;
            }
        }
        if (hp > 2) {
            if (card.nameIs("顺手牵羊") || card.nameIs("过河拆桥")) {
                return false;
            }
            if (card.nameIs("决斗")) {
                return getCardInHandOrTreasure("桃") != null;
            }
            return true;
        }
        if (card.nameIs("顺手牵羊") || card.nameIs("过河拆桥")) {
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
