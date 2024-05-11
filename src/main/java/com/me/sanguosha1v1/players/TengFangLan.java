package com.me.sanguosha1v1.players;

import com.me.sanguosha1v1.Desk;
import com.me.sanguosha1v1.cards.Card;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 滕芳兰
 */
@Slf4j
public class TengFangLan extends Player {
    public TengFangLan(Desk desk) {
        super(desk);
    }

    /**
     * 落宠：
     * 准备阶段或当你每回合首次受到伤害后，你可以选择一项，令一名角色：
     * 1.回复1点体力；2.失去1点体力；3.弃置两张牌；4.摸两张牌。每轮每项每名角色限一次。
     */
    /**
     * 哀尘:
     * 锁定技，当你进入濒死状态时，若“落宠”中可选选项数大于1，则你回复体力值至1，然后移除“落宠”中的一个选项。
     */

    /**
     * 移除顺序为:2,4,1,3
     */
    private Integer luochongCount = 4; //表示能使用的选项数
    private List<Boolean> luochongOptions; //表示每个选项是否可用

    private Integer beAttackedRound;//记录被攻击的回合数，判断是否是本回合首次受到伤害

    /**
     * 落宠技能核心逻辑
     */
    private void handle_luochong() {
        //优先回血
        if (hp < maxHp && luochongCount >= 2 && luochongOptions.get(0)) {
            log.info("{}发动技能“落宠”，选择回复1点体力", name);
            hpChange(1);
            luochongOptions.set(0, false);
            return;
        }
        //其次对面有牌就拆
        if (enemy.haveAnyHandCardOrEquip() && luochongOptions.get(2)
        ) {
            //选择弃置其2张牌
            log.info("{}发动技能“落宠”，选择弃置对方2张牌", name);
            Card card1 = ChooseOneCard_luochong(enemy);
            enemy.discardOne(card1);
            log.info("{}弃掉{}的{}", name, enemy.getName(), card1);
            if (!enemy.haveAnyHandCardOrEquip()) {
                return;
            }
            Card card2 = ChooseOneCard_luochong(enemy);
            enemy.discardOne(card2);
            log.info("{}弃掉{}的{}", name, enemy.getName(), card2);
            luochongOptions.set(2, false);
            return;
        }
        //其次是摸牌
        if (luochongOptions.get(3) && luochongCount >= 3) {
            //选择摸两张牌
            log.info("{}发动技能“落宠”，选择摸两张牌", name);
            draw(2);
            luochongOptions.set(3, false);
            return;
        }
        /**
         * 这个目前永远不选择
         */
        if (luochongOptions.get(1) && luochongCount >= 4) {
            //选择对方失去1点体力
            log.info("{}发动技能“落宠”，选择对方失去1点体力", name);
            enemy.hpChange(-1);
            luochongOptions.set(1, false);
        }
        return;
    }

    /**
     * 每轮开始时刷新落宠
     */
    @Override
    public void startNewRoundCount() {
        if (luochongOptions == null) {
            luochongOptions = new ArrayList<>(4);
            for (int i = 0; i < 4; i++) {
                luochongOptions.add(true);
            }
        }
        luochongOptions.set(0, true);
        luochongOptions.set(1, true);
        luochongOptions.set(2, true);
        luochongOptions.set(3, true);
    }

    /**
     * 哀尘:
     * 锁定技，当你进入濒死状态时，若“落宠”中可选选项数大于1，则你回复体力值至1，然后移除“落宠”中的一个选项。
     */


    @Override
    protected void prepareStage() {
        super.prepareStage();
        log.info("准备阶段，{}发动技能落宠", name);
        handle_luochong();
    }


    @Override
    protected void afterBeDamage(int n) {
        if (Objects.equals(beAttackedRound, round)) {
            return;
        }
        log.info("{}在本回合首次受到伤害，发动技能“落宠”", name);
        beAttackedRound = round;
        handle_luochong();
    }

    @Override
    protected void isDying() {
        //进入濒死，需要失去一个选项
        if (luochongCount > 1) {
            luochongCount--;
            log.info("{}进入濒死状态，发动技能“哀尘”，移除一个选项，还剩{}个选项", name, luochongCount);
        }
        super.isDying(false);
    }

    @Override
    protected void isDying(boolean isShanDian) {
        //进入濒死，需要失去一个选项
        if (luochongCount > 1) {
            luochongCount--;
            log.info("{}进入濒死状态，发动技能“哀尘”，移除一个选项，还剩{}个选项", name, luochongCount);
        }
        super.isDying(isShanDian);
    }

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

            if (hp < maxHp && tryUseCard("桃")) {
                continue;
            }

            if (tryEquipSomething()) {
                continue;
            }

            if ((haveSubHorse() || !enemy.haveAddHorse()) && enemy.haveAnyCard() &&
                    haveCardInHandorTreasure("顺手牵羊")) {
                List<Card> cards = getCardsInHandorTreasure("顺手牵羊");
                //如果对方有闪电，直接用
                if (enemy.haveJudgeCard("闪电")) {
                    useCard(cards.getFirst(), enemy);
                    continue;
                }
                //如果手上没有其他顺手牵羊，就屯一下,否则就用
                if (cards.size() > 1 && enemy.haveAnyHandCardOrEquip()) {
                    useCard(cards.getFirst(), enemy);
                    continue;
                }
            }

            if (enemy.haveAnyCard() && haveCardInHandorTreasure("过河拆桥")) {
                List<Card> cards = getCardsInHandorTreasure("过河拆桥");
                //如果对方有闪电，直接用
                if (enemy.haveJudgeCard("闪电")) {
                    useCard(cards.getFirst(), enemy);
                    continue;
                }
                //如果手上没有其他过河拆桥，就屯一下,否则就用
                if (cards.size() > 1 && enemy.haveAnyHandCardOrEquip()) {
                    useCard(cards.getFirst(), enemy);
                    continue;
                }
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

            if (tryUseCard("决斗")) {
                continue;
            }
            if (handleUseSha()) {
                continue;
            }
            if (hp >= handCards.size()) {
                break;
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


    /**
     * 上装备的策略
     */
    private boolean tryEquipSomething() {
        //上武器的策略
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
            //如果enemy手牌数小于1，优先上古锭刀
            if (enemy.getHandCards().size() <= 1) {
                if (weaponIs("古锭刀")) {
                    return false;
                }
                if (tryUseCard("古锭刀")) {
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
        if (!armorsInHand.isEmpty()) {
            //优先上白银狮子
            for (Card card : armorsInHand) {
                if (card.nameIs("白银狮子")) {
                    useCard(card, this);
                    return true;
                }
            }
            if (!haveArmor()) {
                useCard(armorsInHand.getFirst(), this);
                return true;
            }
        }
        //上-1/+1,有啥用啥
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

    private Card chooseOne_muma() {
        //保留木马的优先级，与弃牌策略相反
        List<String> priority = List.of("闪电", "顺手牵羊", "过河拆桥", "无懈可击", "桃", "+1马");
        for (String cardName : priority) {
            Card find = handCards.stream().filter(card -> card.getName().equals(cardName)).findFirst().orElse(null);
            if (find != null) {
                return find;
            }
        }
        return null;
    }

    @Override
    protected Card chooseDiscardOne() {
        //弃牌优先级，在前的保留，其他的优先级最低
        List<String> priority = List.of("闪电", "顺手牵羊", "过河拆桥", "桃", "无懈可击", "+1马", "藤甲");
        Card discard = null;
        for (Card handCard : handCards) {
            if (!priority.contains(handCard.getName())) {
                return handCard;
            }
        }
        for (String cardName : priority) {
            Card find = handCards.stream().filter(card -> card.getName().equals(cardName)).findFirst().orElse(null);
            if (find != null) {
                discard = find;
            }
        }
        return discard;
    }

    @Override
    public Card ChooseOneCard_ShunShouQianYang(Player to) {
        //有闪电，优先拆闪电
        if (enemy.haveJudgeCard("闪电")) {
            return enemy.getJudgeCard("闪电");
        }
        //否则随便拆1张吧
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
     * 落宠的弃牌策略，这里区分是因为落宠不能拆闪电
     */
    private Card ChooseOneCard_luochong(Player to) {
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
        log.error("ChooseOneCard_luochong出错");
        throw new RuntimeException("ChooseOneCard_luochong出错");
    }

    @Override
    public boolean isUse_WuXieKeJi(Card card, Player from, Player to) {
        //如果是自己的锦囊，不用无懈
        if (from == this) {
            return to == this;
        }
        //如果对方用了拆，且自己有白银狮子或木马,用无懈
        if (card.nameIs("过河拆桥") && (armorIs("白银狮子") || haveTreasure())) {
            return false;
        }
        return true;
    }
}


