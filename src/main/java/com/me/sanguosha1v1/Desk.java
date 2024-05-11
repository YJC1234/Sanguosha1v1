package com.me.sanguosha1v1;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.cards.base.Jiu;
import com.me.sanguosha1v1.cards.base.Sha;
import com.me.sanguosha1v1.cards.base.Shan;
import com.me.sanguosha1v1.cards.base.Tao;
import com.me.sanguosha1v1.cards.equip.*;
import com.me.sanguosha1v1.cards.skill.*;
import com.me.sanguosha1v1.cards.timeSkill.BingLiangCunDuan;
import com.me.sanguosha1v1.cards.timeSkill.LeBuSiShu;
import com.me.sanguosha1v1.cards.timeSkill.ShanDian;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 牌堆
 */
@Data
@Component
@Slf4j
public class Desk {
    private List<Card> drawCards; //抽牌堆
    private List<Card> discardCards; //弃牌堆


    /**
     * 初始化牌堆
     */
    public void reset() {
        //初始化整个军争牌堆
        drawCards = new ArrayList<>();
        discardCards = new ArrayList<>();

        //杀，共44张
        //红桃10 10 11,方片6 7 8 9 10 k,
        // 梅花2 3 4 5 6 7 8 8 9 9 10 10 j j, 黑桃8 8 9 9 10 10 j j
        drawCards.add(new Sha(10, ColorType.HEART));
        drawCards.add(new Sha(10, ColorType.HEART));
        drawCards.add(new Sha(11, ColorType.HEART));
        drawCards.add(new Sha(6, ColorType.DIAMOND));
        drawCards.add(new Sha(7, ColorType.DIAMOND));
        drawCards.add(new Sha(8, ColorType.DIAMOND));
        drawCards.add(new Sha(9, ColorType.DIAMOND));
        drawCards.add(new Sha(10, ColorType.DIAMOND));
        drawCards.add(new Sha(13, ColorType.DIAMOND));
        drawCards.add(new Sha(2, ColorType.CLUB));
        drawCards.add(new Sha(3, ColorType.CLUB));
        drawCards.add(new Sha(4, ColorType.CLUB));
        drawCards.add(new Sha(5, ColorType.CLUB));
        drawCards.add(new Sha(6, ColorType.CLUB));
        drawCards.add(new Sha(7, ColorType.CLUB));
        drawCards.add(new Sha(8, ColorType.CLUB));
        drawCards.add(new Sha(8, ColorType.CLUB));
        drawCards.add(new Sha(9, ColorType.CLUB));
        drawCards.add(new Sha(9, ColorType.CLUB));
        drawCards.add(new Sha(10, ColorType.CLUB));
        drawCards.add(new Sha(10, ColorType.CLUB));
        drawCards.add(new Sha(11, ColorType.CLUB));
        drawCards.add(new Sha(11, ColorType.CLUB));
        drawCards.add(new Sha(8, ColorType.SPADE));
        drawCards.add(new Sha(8, ColorType.SPADE));
        drawCards.add(new Sha(9, ColorType.SPADE));
        drawCards.add(new Sha(9, ColorType.SPADE));
        drawCards.add(new Sha(10, ColorType.SPADE));
        drawCards.add(new Sha(10, ColorType.SPADE));
        drawCards.add(new Sha(11, ColorType.SPADE));
        drawCards.add(new Sha(11, ColorType.SPADE));
        //火杀 红桃4 7 10, 方片4 5
        drawCards.add(new Sha("火杀", 4, ColorType.HEART));
        drawCards.add(new Sha("火杀", 7, ColorType.HEART));
        drawCards.add(new Sha("火杀", 10, ColorType.HEART));
        drawCards.add(new Sha("火杀", 4, ColorType.DIAMOND));
        drawCards.add(new Sha("火杀", 5, ColorType.DIAMOND));
        //雷杀 梅花5 6 7 8, 黑桃4 5 6 7 8
        drawCards.add(new Sha("雷杀", 5, ColorType.CLUB));
        drawCards.add(new Sha("雷杀", 6, ColorType.CLUB));
        drawCards.add(new Sha("雷杀", 7, ColorType.CLUB));
        drawCards.add(new Sha("雷杀", 8, ColorType.CLUB));
        drawCards.add(new Sha("雷杀", 4, ColorType.SPADE));
        drawCards.add(new Sha("雷杀", 5, ColorType.SPADE));
        drawCards.add(new Sha("雷杀", 6, ColorType.SPADE));
        drawCards.add(new Sha("雷杀", 7, ColorType.SPADE));
        drawCards.add(new Sha("雷杀", 8, ColorType.SPADE));
        //闪，共24张
        //红桃2 2 8 9 j q k, 方片2 2 3 4 5 6 6 7 7 8 8 9 10 10 j j j
        drawCards.add(new Shan(2, ColorType.HEART));
        drawCards.add(new Shan(2, ColorType.HEART));
        drawCards.add(new Shan(8, ColorType.HEART));
        drawCards.add(new Shan(9, ColorType.HEART));
        drawCards.add(new Shan(11, ColorType.HEART));
        drawCards.add(new Shan(12, ColorType.HEART));
        drawCards.add(new Shan(13, ColorType.HEART));
        drawCards.add(new Shan(2, ColorType.DIAMOND));
        drawCards.add(new Shan(2, ColorType.DIAMOND));
        drawCards.add(new Shan(3, ColorType.DIAMOND));
        drawCards.add(new Shan(4, ColorType.DIAMOND));
        drawCards.add(new Shan(5, ColorType.DIAMOND));
        drawCards.add(new Shan(6, ColorType.DIAMOND));
        drawCards.add(new Shan(6, ColorType.DIAMOND));
        drawCards.add(new Shan(7, ColorType.DIAMOND));
        drawCards.add(new Shan(7, ColorType.DIAMOND));
        drawCards.add(new Shan(8, ColorType.DIAMOND));
        drawCards.add(new Shan(8, ColorType.DIAMOND));
        drawCards.add(new Shan(9, ColorType.DIAMOND));
        drawCards.add(new Shan(10, ColorType.DIAMOND));
        drawCards.add(new Shan(10, ColorType.DIAMOND));
        drawCards.add(new Shan(11, ColorType.DIAMOND));
        drawCards.add(new Shan(11, ColorType.DIAMOND));
        drawCards.add(new Shan(11, ColorType.DIAMOND));
        //桃 共12张
        //红桃3 4 5 6 6 7 8 9 q， 方片2 3 q
        drawCards.add(new Tao(3, ColorType.HEART));
        drawCards.add(new Tao(4, ColorType.HEART));
        drawCards.add(new Tao(5, ColorType.HEART));
        drawCards.add(new Tao(6, ColorType.HEART));
        drawCards.add(new Tao(6, ColorType.HEART));
        drawCards.add(new Tao(7, ColorType.HEART));
        drawCards.add(new Tao(8, ColorType.HEART));
        drawCards.add(new Tao(9, ColorType.HEART));
        drawCards.add(new Tao(12, ColorType.HEART));
        drawCards.add(new Tao(2, ColorType.DIAMOND));
        drawCards.add(new Tao(3, ColorType.DIAMOND));
        drawCards.add(new Tao(12, ColorType.DIAMOND));
        //酒，共5张
        //方片9, 梅花3 9，黑桃3 9
        drawCards.add(new Jiu(9, ColorType.DIAMOND));
        drawCards.add(new Jiu(3, ColorType.CLUB));
        drawCards.add(new Jiu(9, ColorType.CLUB));
        drawCards.add(new Jiu(3, ColorType.SPADE));
        drawCards.add(new Jiu(9, ColorType.SPADE));


        //2.1桃园结义（1张，♡A）。
        drawCards.add(new TaoYuanJieYi(1, ColorType.HEART));
        //2.2万箭齐发（1张，♡A）。
        drawCards.add(new WanJianQiFa(1, ColorType.HEART));
        //2.3决斗（3张，♢A♧A♤A）。
        drawCards.add(new JueDou(1, ColorType.DIAMOND));
        drawCards.add(new JueDou(1, ColorType.CLUB));
        drawCards.add(new JueDou(1, ColorType.SPADE));
        //五谷丰登（2张，♡3♡4）。
        drawCards.add(new WuGuFengDeng(3, ColorType.HEART));
        drawCards.add(new WuGuFengDeng(4, ColorType.HEART));
        //顺手牵羊（5张，♢34♤34J）。
        drawCards.add(new ShunShouQianYang(3, ColorType.DIAMOND));
        drawCards.add(new ShunShouQianYang(4, ColorType.DIAMOND));
        drawCards.add(new ShunShouQianYang(3, ColorType.SPADE));
        drawCards.add(new ShunShouQianYang(4, ColorType.SPADE));
        drawCards.add(new ShunShouQianYang(11, ColorType.SPADE));
        //过河拆桥（6张，♡Q♧34♤34Q）。
        drawCards.add(new GuoHeChaiQiao(12, ColorType.HEART));
        drawCards.add(new GuoHeChaiQiao(3, ColorType.CLUB));
        drawCards.add(new GuoHeChaiQiao(4, ColorType.CLUB));
        drawCards.add(new GuoHeChaiQiao(3, ColorType.SPADE));
        drawCards.add(new GuoHeChaiQiao(4, ColorType.SPADE));
        drawCards.add(new GuoHeChaiQiao(12, ColorType.SPADE));
        //乐不思蜀（3张，♡6♧6♤6）。
        drawCards.add(new LeBuSiShu(6, ColorType.HEART));
        drawCards.add(new LeBuSiShu(6, ColorType.CLUB));
        drawCards.add(new LeBuSiShu(6, ColorType.SPADE));
        //南蛮入侵（3张，♧7♤K♤7）。
        drawCards.add(new NanManRuQin(7, ColorType.CLUB));
        drawCards.add(new NanManRuQin(13, ColorType.SPADE));
        drawCards.add(new NanManRuQin(7, ColorType.SPADE));
        //无中生有（4张，♡789J）
        drawCards.add(new WuZhongShengYou(7, ColorType.HEART));
        drawCards.add(new WuZhongShengYou(8, ColorType.HEART));
        drawCards.add(new WuZhongShengYou(9, ColorType.HEART));
        drawCards.add(new WuZhongShengYou(11, ColorType.HEART));
        //借刀杀人（2张，♧Q♧K）
        drawCards.add(new JieDaoShaRen(12, ColorType.CLUB));
        drawCards.add(new JieDaoShaRen(13, ColorType.CLUB));
        //火攻（3张，♡2♡3♢Q）。
        drawCards.add(new HuoGong(2, ColorType.HEART));
        drawCards.add(new HuoGong(3, ColorType.HEART));
        drawCards.add(new HuoGong(12, ColorType.DIAMOND));
        //兵粮寸断（2张，♧4♤10）。
        drawCards.add(new BingLiangCunDuan(4, ColorType.CLUB));
        drawCards.add(new BingLiangCunDuan(10, ColorType.SPADE));
        //铁索连环（6张，♧10JQK♤JQ）
        drawCards.add(new TieSuoLianHuan(10, ColorType.CLUB));
        drawCards.add(new TieSuoLianHuan(11, ColorType.CLUB));
        //闪电（2张，标准版♤A，EX♡Q）。
        drawCards.add(new ShanDian(1, ColorType.SPADE));
        drawCards.add(new ShanDian(12, ColorType.HEART));
        //无懈可击（7张）。
        //标准版（♧QK♤J）
        //EX（♢Q）
        //军争篇（♡AK♤K）
        drawCards.add(new WuXieKeJi(12, ColorType.CLUB));
        drawCards.add(new WuXieKeJi(13, ColorType.CLUB));
        drawCards.add(new WuXieKeJi(11, ColorType.SPADE));
        drawCards.add(new WuXieKeJi(12, ColorType.DIAMOND));
        drawCards.add(new WuXieKeJi(13, ColorType.HEART));
        drawCards.add(new WuXieKeJi(13, ColorType.SPADE));
        drawCards.add(new WuXieKeJi(1, ColorType.HEART));

        //3.1武器（来自于标准版、EX、军争篇）：
        //共计12张。
        //标准版9张
        //1：诸葛连弩♧A、诸葛连弩♢A；
        //2：雌雄双股剑♤2、青釭剑♤6；
        //3：贯石斧♢5、青龙偃月刀♤5、丈八蛇矛♤Q；
        //4：方天画戟♢Q；
        //5：麒麟弓♡5。
        //EX1张
        //2：寒冰剑♤2。
        //军争篇2张
        //2：古锭刀♤A；
        //4：朱雀羽扇♢A。
        drawCards.add(new Weapon("诸葛连弩", 1, ColorType.CLUB));
        drawCards.add(new Weapon("诸葛连弩", 1, ColorType.DIAMOND));
        drawCards.add(new Weapon("雌雄双股剑", 2, ColorType.SPADE));
        drawCards.add(new Weapon("青釭剑", 6, ColorType.SPADE));
        drawCards.add(new Weapon("贯石斧", 5, ColorType.DIAMOND));
        drawCards.add(new Weapon("青龙偃月刀", 5, ColorType.SPADE));
        drawCards.add(new Weapon("丈八蛇矛", 12, ColorType.SPADE));
        drawCards.add(new Weapon("方天画戟", 12, ColorType.DIAMOND));
        drawCards.add(new Weapon("麒麟弓", 5, ColorType.HEART));
        drawCards.add(new Weapon("寒冰剑", 2, ColorType.SPADE));
        drawCards.add(new Weapon("古锭刀", 1, ColorType.SPADE));
        drawCards.add(new Weapon("朱雀羽扇", 1, ColorType.DIAMOND));
        //3.2防具（来自标准版、EX、军争篇）：
        //共6张，均为黑色。
        //标准版2张：八卦阵♧2、八卦阵♤2。
        //EX1张：仁王盾♧2。
        //军争篇3张：白银狮子♧A、藤甲♧2、藤甲♤2。
        drawCards.add(new Armor("八卦阵", 2, ColorType.CLUB));
        drawCards.add(new Armor("八卦阵", 2, ColorType.SPADE));
        drawCards.add(new Armor("仁王盾", 2, ColorType.CLUB));
        drawCards.add(new Armor("白银狮子", 1, ColorType.CLUB));
        drawCards.add(new Armor("藤甲", 2, ColorType.CLUB));
        drawCards.add(new Armor("藤甲", 2, ColorType.SPADE));
        //3.3坐骑（来自标准版与军争篇）：
        //共7张，四张+1，三张-1。
        //标准版6张：3张+1，3张-1。
        //+1：的卢♧5、绝影♤5、爪黄飞电♡K
        //-1：赤兔♡5、紫骍♢K、大宛♤K
        //军争篇1张：骅骝♢K（+1）。
        drawCards.add(new addHorse(5, ColorType.DIAMOND));
        drawCards.add(new addHorse(5, ColorType.CLUB));
        drawCards.add(new addHorse(13, ColorType.HEART));
        drawCards.add(new subHorse(5, ColorType.HEART));
        drawCards.add(new subHorse(13, ColorType.DIAMOND));
        drawCards.add(new subHorse(13, ColorType.SPADE));
        //3.4宝物（来自界限突破扩展包）：
        //木牛流马♢5。
        drawCards.add(new MuNiuLiuMa(5, ColorType.DIAMOND));
        //洗牌
        shuffle();
    }

    /**
     * 洗牌
     */
    public void shuffle() {
        drawCards.addAll(discardCards);
        discardCards.clear();
        Collections.shuffle(drawCards);
    }

    /**
     * 抽牌
     */
    public List<Card> draw(int num) {
        List<Card> drewCards = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            if (drawCards.isEmpty()) {
                shuffle();
            }
            Card card = drawCards.removeFirst();
            drewCards.add(card);
        }
        return drewCards;
    }

    public Card drawOne() {
        return draw(1).getFirst();
    }

    /**
     * 弃牌
     */
    public void discard(List<Card> cards) {
        discardCards.addAll(cards);
    }

    public void discard(Card card) {
        log.info("{}进入弃牌堆", card);
        discardCards.add(card);
    }

    /**
     * 将牌放到抽牌堆的顶部
     */
    public void putCardsToTop(List<Card> cards) {
        drawCards.addAll(0, cards);
    }


}
