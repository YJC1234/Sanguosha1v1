package com.me.sanguosha1v1.players;


import com.me.sanguosha1v1.Desk;
import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.base.Sha;
import com.me.sanguosha1v1.cards.equip.Armor;
import com.me.sanguosha1v1.cards.equip.Weapon;
import com.me.sanguosha1v1.cards.timeSkill.TimeSkillCard;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 武将基类
 * 有一些基本属性，如血量、手牌、装备区等
 */
@Data
@RequiredArgsConstructor
@Slf4j
abstract public class Player {
    protected final Desk desk;

    protected String name;
    /**
     * 基本信息
     */
    protected Integer hp;
    protected Integer maxHp;
    protected List<Card> handCards;
    protected boolean isDrunk; //是否处于酒状态
    protected boolean haveDrunk; //本回合是否已经喝酒
    protected Integer shaCount; //本回合可以使用杀的次数
    //由于1v1只需判断+1，所以不用计算攻击距离

    /**
     * 装备区
     */
    protected Card weapon; //武器
    protected Card armor; //防具
    protected Card subHorse; //-1马
    protected Card addHorse; //+1马
    protected Card treasure; //宝物(木牛流马)

    /**
     * 宝物(木牛流马区)，最多5张
     */
    protected List<Card> treasureCards;

    /**
     * 判定区
     */
    protected List<TimeSkillCard> judgeCards;
    protected boolean isJumpingDraw; //跳过摸牌?辅助判定
    protected boolean isJumpingPlay; //跳过出牌?辅助判定

    /**
     * 对手
     */
    protected Player enemy;

    /**
     * 辅助
     */
    protected Integer round;//当前的回合数
    protected boolean diedByShanDian; //是否被闪电劈死

    protected List<String> weaponPriority;//武器使用优先队列
    protected List<String> armorPriority;//防具使用优先队列


    //目标为自己/所有人的牌名
    public static List<String> targetSelfNames =
            List.of("桃园结义", "铁索连环", "无中生有", "五谷丰登", "桃", "酒", "-1马", "+1马", "木牛流马", "闪电");


    public void update() {
        before();   //进入回合时的一些辅助操作

        log.info("{}进入回合", name);
        prepareStage();//准备阶段
        judgeStage(); //判定阶段
        if (hp <= 0) {
            //已经被闪电劈死了
            return;
        }
        if (!isJumpingDraw) {
            drawStage(); //摸牌阶段
        }
        if (!isJumpingPlay) {
            playStage(); //出牌阶段
        }
        if (hp <= 0) {
            //修复借刀把自己砍死后弃牌bug-.-
            return;
        }
        discardStage(); //弃牌阶段
        endStage(); //结束阶段

        after();    //离开回合时的一些辅助操作
    }


    private void before() {
        isDrunk = false;
        haveDrunk = false;
        isJumpingDraw = false;
        isJumpingPlay = false;
        shaCount = 1;
        diedByShanDian = false;
    }

    private void after() {
    }

    /**
     * 每轮开始时
     */
    public void startNewRoundCount() {
        //腾芳兰重写
    }

    /**
     * 准备阶段，可以被重写
     */
    protected void prepareStage() {
    }

    private void judgeStage() {
        if (judgeCards.isEmpty()) {
            return;
        }
        //后贴的先判定，所以先反转judgeCards
        judgeCards = judgeCards.reversed();
        List<TimeSkillCard> removeJudgeCards = new ArrayList<>();
        Card failShanDian = null;
        for (TimeSkillCard card : judgeCards) {
            boolean isShanDian = card.judge(enemy, this);
            if (isShanDian) { //判定失败的闪电
                //移动至对手的判定区，如果移动失败，仍然留下
                if (!enemy.haveJudgeCard("闪电")) {
                    log.info("{}的闪电判定失败或者被无懈，移动至{}的判定区", name, enemy.getName());
                    enemy.addJudgeCard(card);
                    removeJudgeCards.add(card);
                    failShanDian = card;
                } else {
                    log.info("{}的闪电判定失败，但{}已经有闪电了，保留", name, enemy.getName());
                }
            } else {
                removeJudgeCards.add(card);
            }
        }
        //判定完成，将所有judgeCards中的removeJudgeCards移除，除了失败闪电，其他进入弃牌堆
        judgeCards.removeAll(removeJudgeCards);
        for (TimeSkillCard removeJudgeCard : removeJudgeCards) {
            if (removeJudgeCard != failShanDian) {
                desk.discard(removeJudgeCard);
            }
        }
    }

    protected void drawStage() {
        //抽2牌，特殊武将可以重写
        draw(2);
        log.info("{}的手牌为{}", name, handCards);
    }

    /**
     * 出牌阶段的策略，需要被重写
     */
    abstract public void playStage();

    protected void discardStage() {
        if (handCards.size() > hp) {
            List<Card> discard = discard(handCards.size() - hp);
            log.info("弃牌阶段，{}弃掉了{}", name, discard);
        }
    }

    protected void endStage() {
        //目前啥也不干
    }

    /**
     * ---------------辅助方法-------------------
     *
     */

    /**
     * 对enemy是否有距离
     */
    public boolean canArrive() {
        if (!enemy.haveAddHorse() || haveSubHorse()) {
            return true;
        }
        if (weapon != null && !weaponIs("诸葛连弩")) {
            return true;
        }
        return false;
    }


    /**
     * 摸n张牌
     */
    public void draw(int count) {
        List<Card> drewCards = desk.draw(count);
        handCards.addAll(drewCards);
        log.info("{}摸了{}张牌,为{}", name, count, drewCards);
    }

    /**
     * 依据策略弃n张牌
     */
    public List<Card> discard(int count) {
        List<Card> removeCards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (!haveAnyHandCardOrEquip()) {
                return removeCards;
            }
            Card card = chooseDiscardOne();
            discardOne(card);
            removeCards.add(card);
        }
        return removeCards;
    }


    /**
     * 是否有手牌
     */
    public boolean haveAnyCardInHand() {
        return !handCards.isEmpty();
    }

    /**
     * 是否有装备
     */
    public boolean haveAnyEquip() {
        return weapon != null || armor != null || subHorse != null || addHorse != null || treasure != null;
    }

    /**
     * 是否有判定牌
     */
    public boolean haveAnyJudge() {
        return !judgeCards.isEmpty();
    }

    /**
     * 是否有手牌或装备牌或判定区的牌
     */
    public boolean haveAnyCard() {
        return haveAnyCardInHand() || haveAnyEquip() || haveAnyJudge();
    }

    /**
     * 是否有手牌或装备牌
     */
    public boolean haveAnyHandCardOrEquip() {
        return haveAnyCardInHand() || haveAnyEquip();
    }

    /**
     * 是否有武器/防具/-1马/+1马/宝物
     */
    public boolean haveWeapon() {
        return weapon != null;
    }

    public boolean haveArmor() {
        return armor != null;
    }

    public boolean haveSubHorse() {
        return subHorse != null;
    }

    public boolean haveAddHorse() {
        return addHorse != null;
    }

    public boolean haveTreasure() {
        return treasure != null;
    }

    /**
     * 手牌或木马中是否有某个牌名的牌
     */
    public boolean haveCardInHandorTreasure(String cardName) {
        if (handCards.stream().anyMatch(card -> card.getName().equals(cardName))) {
            return true;
        }
        if (haveTreasure() && treasureCards.stream().anyMatch(card -> card.getName().equals(cardName))) {
            return true;
        }
        return false;
    }

    /**
     * 获取手牌中某个牌名的牌
     */
    public Card getCardInHandOrTreasure(String cardName) {
        Card findInHand =
                handCards.stream().filter(card -> card.getName().equals(cardName)).findFirst().orElse(null);
        if (findInHand != null) {
            return findInHand;
        }
        return treasureCards.stream().filter(card -> card.getName().equals(cardName)).findFirst().orElse(null);
    }

    /**
     * 获取手牌中某个牌名的所有牌
     */
    public List<Card> getCardsInHandorTreasure(String cardName) {
        List<Card> findInHand = handCards.stream().filter(card -> card.getName().equals(cardName)).toList();
        //把不可变转化为可变
        findInHand = new ArrayList<>(findInHand);
        if (haveTreasure()) {
            for (Card treasureCard : treasureCards) {
                if (treasureCard.getName().equals(cardName)) {
                    findInHand.add(treasureCard);
                }
            }
        }
        return findInHand;
    }

    /**
     * 获取随机1张手牌
     */
    public Card getRandomCardInHand() {
        return handCards.get(new Random().nextInt(handCards.size()));
    }

    /**
     * 从手牌中弃掉对应的牌
     */
    public void discardOneInHand(Card card) {
        handCards.remove(card);
        desk.discard(card);
    }

    /**
     * 从手牌中弃掉一张某个牌名的牌
     */
    public void discardOneInHand(String cardName) {
        Card card = getCardInHandOrTreasure(cardName);
        if (card != null) {
            handCards.remove(card);
            desk.discard(card);
        }
    }

    /**
     * 获取判定区某个牌名的牌
     */
    public Card getJudgeCard(String cardName) {
        return judgeCards.stream().filter(card -> card.getName().equals(cardName)).findFirst().orElse(null);
    }

    /**
     * 从手牌/装备区/判定区中移除某张牌
     */
    public void removeOne(Card card) {
        if (handCards.contains(card)) {
            handCards.remove(card);
        } else if (judgeCards.contains(card)) {
            judgeCards.remove(card);
        } else if (weapon == card) {
            weapon = null;
        } else if (armor == card) {
            armor = null;
            if (card.nameIs("白银狮子")) {
                log.info("{}的白银狮子被弃掉，回复1点体力", name);
                hpChange(1);
            }
        } else if (subHorse == card) {
            subHorse = null;
        } else if (addHorse == card) {
            addHorse = null;
        } else if (treasure == card) {
            treasure = null;
            //同时要弃掉treasureCards所有牌
            treasureCards.forEach(desk::discard);
            treasureCards.clear();
        } else if (treasureCards.contains(card)) {
            treasureCards.remove(card);
        }
    }

    /**
     * 从手牌/装备区/判定区中丢弃某张牌
     */
    public void discardOne(Card card) {
        removeOne(card);
        desk.discard(card);
    }

    /**
     * 获取装备区所有牌的信息
     */
    public List<Card> getEquips() {
        //如果不为空，加入
        List<Card> equips = new ArrayList<>();
        if (weapon != null) {
            equips.add(weapon);
        }
        if (armor != null) {
            equips.add(armor);
        }
        if (subHorse != null) {
            equips.add(subHorse);
        }
        if (addHorse != null) {
            equips.add(addHorse);
        }
        if (treasure != null) {
            equips.add(treasure);
        }
        return equips;
    }

    /**
     * 使用某张牌(触发效果)
     */
    public void useCard(Card card, Player to) {
        //如果Card是基本或锦囊，使用后进入弃牌堆，否则不进入弃牌堆
        if (card.isBasic() || card.isSkill()) {
            discardOne(card);
        } else {
            removeOne(card);
        }
        log.info("{}对{}使用了{}", name, to.getName(), card);
        card.active(this, to);
    }

    public void useCard(String cardName, Player to) {
        Card card = getCardInHandOrTreasure(cardName);
        if (card == null) {
            throw new RuntimeException("手牌中没有" + cardName);
        }
        useCard(card, to);
    }

    /**
     * 打出某张牌(不触发效果)
     */
    public void doCard(String cardName) {
        Card card = getCardInHandOrTreasure(cardName);
        if (card == null) {
            throw new RuntimeException("手牌中没有" + cardName);
        }
        discardOne(card);
        log.info("{}打出了{}", name, card);
    }

    /**
     * 添加某张牌到手牌
     */
    public void addCard(Card card) {
        handCards.add(card);
    }

    /**
     * 判断判定区里有没有指定牌名的牌
     */
    public boolean haveJudgeCard(String cardName) {
        return judgeCards.stream().anyMatch(card -> card.getName().equals(cardName));
    }

    /**
     * 添加一张牌到判定区
     */
    public void addJudgeCard(TimeSkillCard card) {
        judgeCards.add(card);
    }

    /**
     * 从抽牌堆拿一张牌用于判定
     */
    public Card drawOneForJudge() {
        Card card = desk.drawOne();
        desk.discard(card);
        return card;
    }

    /**
     * 获取手牌中的所有武器
     */
    public List<Card> getWeaponsInHand() {
        return handCards.stream().filter(card -> card instanceof Weapon).toList();
    }

    /**
     * 获取手牌中的所有防具
     */
    public List<Card> getArmorsInHand() {
        return handCards.stream().filter(card -> card instanceof Armor).toList();
    }

    /**
     * 获取手牌所有-1马
     */
    public List<Card> getSubHorsesInHand() {
        return handCards.stream().filter(card -> card.getName().equals("-1马")).toList();
    }

    /**
     * 获取手牌的所有+1马
     */
    public List<Card> getAddHorsesInHand() {
        return handCards.stream().filter(card -> card.getName().equals("+1马")).toList();
    }

    /**
     * 获取木牛流马
     */
    public Card getTreasureInHand() {
        return handCards.stream().filter(card -> card.getName().equals("木牛流马")).findFirst().orElse(null);
    }

    /**
     * 生命值变化
     */
    public void hpChange(int n) {
        hpChange(n, false);
    }

    public void hpChange(int n, boolean isShanDian) {
        if (n > 0) {
            if (hp + n > maxHp) {
                n = maxHp - hp;
            }
            hp += n;
            log.info("{}回复了{}点体力，体力值为{}/{}", name, n, hp, maxHp);
        } else {
            if (n < -1 && armorIs("白银狮子")) {
                n = -1;
                log.info("{}的白银狮子发动，伤害减少至1", name);
            }
            hp += n;
            log.info("{}受到了{}点伤害，体力值为{}/{}", name, -n, hp, maxHp);
            if (hp <= 0) {
                isDying(isShanDian);
            }
            afterBeDamage(-n);
        }
    }

    /**
     * 受到n点伤害时的操作，腾芳兰重写
     */
    protected void afterBeDamage(int n) {
    }

    /**
     * 濒死
     */
    protected void isDying() {
        isDying(false);
    }

    protected void isDying(boolean isShanDian) {
        log.info("{}进入濒死，需要回复{}点体力", name, 1 - hp);
        while (hp <= 0) {
            if (haveCardInHandorTreasure("桃")) {
                doCard("桃");
                hpChange(1);
            } else if (haveCardInHandorTreasure("酒")) {
                doCard("酒");
                hpChange(1);
            } else {
                log.info("{}死亡", name);
                if (isShanDian) {
                    diedByShanDian = true;
                }
                return;
            }
        }
    }

    /**
     * 武器是不是某个牌名
     */
    public boolean weaponIs(String cardName) {
        return weapon != null && weapon.getName().equals(cardName);
    }

    /**
     * 防具是不是某个牌名
     */
    public boolean armorIs(String cardName) {
        return armor != null && armor.getName().equals(cardName);
    }

    /**
     * 尝试用某张手牌
     */
    public boolean tryUseCard(String cardName, Player to) {
        if (haveCardInHandorTreasure(cardName)) {
            useCard(cardName, to);
            return true;
        }
        return false;
    }

    public boolean tryUseCard(String cardName) {
        if (!haveCardInHandorTreasure(cardName)) {
            return false;
        }
        Card card = getCardInHandOrTreasure(cardName);
        if (card instanceof Weapon || card instanceof Armor || targetSelfNames.contains(card.getName())) {
            useCard(card, this);
        } else {
            useCard(card, enemy);
        }
        return true;
    }

    /**
     * --------------------------核心部分！！！-----------------------
     * ---------------------可被重写的策略相关------------------------
     */

    /**
     * 主动选择弃牌的策略
     */
    protected abstract Card chooseDiscardOne();

    /**
     * 顺手牵羊选择对方牌的策略
     */
    public abstract Card ChooseOneCard_ShunShouQianYang(Player to);

    /**
     * 过河拆桥选择对方牌的策略
     */
    public abstract Card ChooseOneCard_GuoHeChaiQiao(Player to);

    /**
     * 使用无懈可击的策略
     * 如果from为this,表示自己使用被无懈了，否则表示对方使用
     * 返回值为该锦囊是否有效
     * 比如from==this，返回false，表示自己用的锦囊无效，也就是代表被无懈了不打反无懈了
     */
    public abstract boolean isUse_WuXieKeJi(Card card, Player from, Player to);


    /**
     * ---------------------牌的逻辑-----------------------
     */

    /**
     * 受到from的杀
     */
    public void handleSha(int damage, Player from, Card card) {
        if (!weaponIs("青釭剑")) {
            if (armorIs("藤甲")) {
                //普通杀挡掉，雷杀不挡，火杀伤害+1
                if (card.nameIs("杀")) {
                    log.info("{}的藤甲挡掉了{}", name, card);
                    return;
                } else if (card.nameIs("火杀")) {
                    damage++;
                }
            }
            if (armorIs("仁王盾")) {
                //挡掉黑色的杀
                if (card.isBlack()) {
                    log.info("{}的仁王盾 挡掉了{}", name, card);
                    return;
                }
            }
            if (armorIs("八卦阵")) {
                //判定(如果牌堆上方的牌是红色，就成功)
                Card judgeCard = drawOneForJudge();
                log.info("{}发动八卦阵判定，判定结果为{}", name, judgeCard);
                if (judgeCard.isRed()) {
                    log.info("判定成功，挡掉了{}", card);
                    return;
                } else {
                    log.info("判定失败");
                }
            }
        }
        if (haveCardInHandorTreasure("闪")) {
            //有闪就打出
            doCard("闪");
        } else {
            log.info("{}被{}的{}打中", name, from.getName(), card);
            if (!haveAnyCardInHand() && enemy.weaponIs("古锭刀")) {
                damage++;
                log.info("{}的古锭刀发动，伤害+1", enemy.getName());
            }
            if (haveAnyCardInHand() && enemy.weaponIs("寒冰剑")) {
                for (int i = 0; i < 2; i++) {
                    if (!haveAnyHandCardOrEquip()) {
                        break;
                    }
                    Card choose = enemy.ChooseOneCard_GuoHeChaiQiao(this);
                    log.info("{}的寒冰剑发动，{}的{}被弃掉", enemy.getName(), name, choose);
                    discardOne(choose);
                }
                return;
            }
            hpChange(-damage);
            if (enemy.weaponIs("麒麟弓")) {
                if (haveAddHorse()) {
                    discardOne(addHorse);
                    log.info("{}的麒麟弓发动，{}的+1马被弃掉", enemy.getName(), name);
                } else if (haveSubHorse()) {
                    discardOne(subHorse);
                    log.info("{}的麒麟弓发动，{}的-1马被弃掉", enemy.getName(), name);
                }
            }

        }
    }

    /**
     * 杀的策略,返回值表示是否出了杀
     */
    public boolean handleUseSha() {
        if (canArrive()
                && (haveCardInHandorTreasure("杀") || haveCardInHandorTreasure("火杀") || haveCardInHandorTreasure("雷杀"))
                && (shaCount > 0 || weaponIs("诸葛连弩"))) {
            //首先获取手牌所有杀
            List<Sha> shaInHand = new ArrayList<>();
            for (Card card : handCards) {
                if (card instanceof Sha) {
                    shaInHand.add((Sha) card);
                }
            }
            //细节：如果对方有仁王盾，自己没有青红剑，就不用黑色杀
            if (enemy.armorIs("仁王盾") && !weaponIs("青红剑")) {
                shaInHand.removeIf(Card::isBlack);
            }
            Sha usedSha = null;
            //细节，如果对方有藤甲，不用普通杀，优先用火杀
            if (enemy.armorIs("藤甲")) {
                shaInHand.removeIf(sha -> sha.nameIs("杀"));
                for (Sha sha : shaInHand) {
                    if (sha.nameIs("火杀")) {
                        usedSha = sha;
                        break;
                    }
                }
            }
            if (shaInHand.isEmpty()) {
                return false;
            }
            if (usedSha == null) {
                usedSha = shaInHand.getFirst();
            }
            if (!haveDrunk && haveCardInHandorTreasure("酒") &&
                    !(enemy.armorIs("白银狮子") && !weaponIs("青釭剑"))) {
                useCard("酒", this);
            }
            shaCount--;
            useCard(usedSha, enemy);
            return true;
        }
        return false;
    }

    /**
     * 判断new武器优先级是否比old武器更高
     */
    public boolean isWeaponPriorityHigher(String newWeapon, String oldWeapon) {
        //如果new不在列表，返回false
        if (!weaponPriority.contains(newWeapon)) {
            return false;
        }
        if (!weaponPriority.contains(oldWeapon)) {
            return true;
        }
        return weaponPriority.indexOf(newWeapon) < weaponPriority.indexOf(oldWeapon);
    }

    /**
     * 判断new防具优先级是否比old防具更高
     */
    public boolean isArmorPriorityHigher(String newArmor, String oldArmor) {
        //如果new不在列表，返回false
        if (!armorPriority.contains(newArmor)) {
            return false;
        }
        if (!armorPriority.contains(oldArmor)) {
            return true;
        }
        return armorPriority.indexOf(newArmor) < armorPriority.indexOf(oldArmor);
    }


}
