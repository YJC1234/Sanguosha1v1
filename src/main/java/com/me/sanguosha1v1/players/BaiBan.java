package com.me.sanguosha1v1.players;

import com.me.sanguosha1v1.Desk;
import com.me.sanguosha1v1.cards.Card;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BaiBan extends Player {
    public BaiBan(Desk desk) {
        super(desk);
    }

    @Override
    public void playStage() {
        if (round < 100) {
            log.info("挂机中");
            return;
        }
        /**
         * //如果在一次出牌决策中使用了牌，就重复决策,直到不使用牌为止
         */
        while (true) {
            if (tryUseCard("无中生有")) {
                continue;
            }
            if (tryUseCard("铁索连环")) {
                continue;
            }
            if (hp < maxHp && tryUseCard("桃")) {
                continue;
            }

            if (tryEquipSomething()) {
                continue;
            }

            if (canArrive() && enemy.haveAnyHandCardOrEquip() && tryUseCard("顺手牵羊")) {
                continue;
            }
            if (enemy.haveAnyHandCardOrEquip() && tryUseCard("过河拆桥")) {
                continue;
            }
            if (enemy.haveAnyCardInHand() && tryUseCard("火攻")) {
                continue;
            }
            if (tryUseCard("决斗")) {
                continue;
            }
            if (tryUseCard("南蛮入侵")) {
                continue;
            }
            if (tryUseCard("万箭齐发")) {
                continue;
            }
            if (!haveJudgeCard("闪电") && tryUseCard("闪电")) {
                continue;
            }
            if (!enemy.haveJudgeCard("乐不思蜀") && tryUseCard("乐不思蜀")) {
                continue;
            }
            if ((haveSubHorse() || !enemy.haveAddHorse()) && enemy.haveJudgeCard("兵粮寸断")
                    && tryUseCard("兵粮寸断")) {
                continue;
            }
            if (enemy.haveWeapon() && enemy.canArrive() && tryUseCard("借刀杀人")) {
                continue;
            }
            //杀的策略
            if (handleUseSha()) {
                continue;
            }
            break;
        }
    }

    /**
     * 上装备的策略
     */
    private boolean tryEquipSomething() {
        //上武器的策略(白板要啥策略?)
        List<Card> weaponsInHand = getWeaponsInHand();
        if (!weaponsInHand.isEmpty()) {
            //如果enemy有防具，优先上青釭剑
            if (enemy.haveArmor()) {
                if (weaponIs("青釭剑")) {
                    return false;
                }
                if (tryUseCard("青釭剑")) {
                    return true;
                }
            }
            //如果enemy没+1或者自己有-1，优先上诸葛连弩
            if (!enemy.haveAddHorse() || haveSubHorse()) {
                if (weaponIs("诸葛连弩")) {
                    return false;
                }
                if (tryUseCard("诸葛连弩")) {
                    return true;
                }
            }
            //然后优先上麒麟弓
            if (weaponIs("麒麟弓")) {
                return false;
            }
            if (tryUseCard("麒麟弓")) {
                return true;
            }
            //然后如果没有武器就上个武器，有就不上了
            if (!haveWeapon()) {
                useCard(weaponsInHand.getFirst(), this);
                return true;
            }
        }
        //上防具的策略
        List<Card> armorsInHand = getArmorsInHand();
        if (!armorsInHand.isEmpty() && !haveArmor()) {
            useCard(armorsInHand.getFirst(), this);
            return true;
        }
        //上-1/+1,一个样，有啥用啥
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
        //上木马，也一样
        Card treasureInHand = getTreasureInHand();
        if (treasureInHand != null && !haveTreasure()) {
            useCard(treasureInHand, this);
            return true;
        }
        return false;

    }


    @Override
    protected Card chooseDiscardOne() {
        //测试弃牌策略，随机弃1张
        if (!handCards.isEmpty()) {
            return handCards.getFirst();
        }
        if (haveWeapon()) {
            return getWeapon();
        }
        if (haveSubHorse()) {
            return getSubHorse();
        }
        if (haveAddHorse()) {
            return getAddHorse();
        }
        if (haveArmor()) {
            return getArmor();
        }
        if (haveTreasure()) {
            return getTreasure();
        }
        log.error("弃牌策略出错");
        return null;
    }

    @Override
    public Card ChooseOneCard_ShunShouQianYang(Player to) {
        //先拿木马
        if (to.haveTreasure()) {
            return to.getTreasure();
        }
        //再拿白银狮子
        if (to.armorIs("白银狮子")) {
            return to.getArmor();
        }
        //再拿+1
        if (to.haveAddHorse()) {
            return to.getAddHorse();
        }
        //再拿其他防具
        if (to.haveArmor()) {
            return to.getArmor();
        }
        //再拿其他牌
        if (to.haveAnyCardInHand()) {
            return to.handCards.getFirst();
        }
        if (to.haveWeapon()) {
            return to.getWeapon();
        }
        if (to.haveSubHorse()) {
            return to.getSubHorse();
        }
        //不可能到达这里
        return null;
    }

    @Override
    public Card ChooseOneCard_GuoHeChaiQiao(Player to) {
        return ChooseOneCard_ShunShouQianYang(to);
    }

    @Override
    public boolean isUse_WuXieKeJi(Card card, Player from, Player to) {
        if (from == this) {
            //这是自己用的锦囊，保持有效
            return true;
        } else {
            //对方用的锦囊，保持无效
            return false;
        }
    }
}
