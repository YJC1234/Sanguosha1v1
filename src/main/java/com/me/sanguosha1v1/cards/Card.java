package com.me.sanguosha1v1.cards;

import com.me.sanguosha1v1.players.Player;
import lombok.Data;

/**
 * 卡牌基类
 */
@Data
public abstract class Card {
    protected String name; //牌名
    protected CardType type; //类型
    protected Integer point; //点数
    protected ColorType color; //花色

    public Card(String name, CardType type, Integer point, ColorType color) {
        this.name = name;
        this.type = type;
        this.point = point;
        this.color = color;
    }

    /**
     * 卡牌生效前的拦截
     */
    public void active(Player from, Player to) {
        if (type != CardType.SKILL) {
            realActive(from, to);
            return;
        }
        boolean isEffect = handle_WuXieKeJi(from, to);
        if (!isEffect) {
            return;
        }
        realActive(from, to);
    }

    /**
     * 对于锦囊的无懈可击的处理
     * 返回值表示该锦囊是否有效
     */
    protected boolean handle_WuXieKeJi(Player from, Player to) {
        boolean isEffect = true;
        Player enemy = from.getEnemy();
        while (true) {
            //锦囊有效，对方用无懈吗
            if (!enemy.haveCardInHandorTreasure("无懈可击")) {
                break;
            }
            isEffect = enemy.isUse_WuXieKeJi(this, from, to);
            if (isEffect) { //对方不用无懈
                break;
            }
            enemy.doCard("无懈可击");
            //对方用了无懈，自己反无懈吗
            if (!from.haveCardInHandorTreasure("无懈可击")) {
                break;
            }
            isEffect = from.isUse_WuXieKeJi(this, from, to);
            if (!isEffect) { //自己不打反无懈
                break;
            }
            from.doCard("无懈可击");
        }
        return isEffect;
    }


    /**
     * 卡牌的效果，需要被重写
     */
    abstract public void realActive(Player from, Player to);

    /**
     * 是否为黑色
     */
    public boolean isBlack() {
        return color == ColorType.SPADE || color == ColorType.CLUB;
    }

    /**
     * 是否为红色
     */
    public boolean isRed() {
        return color == ColorType.HEART || color == ColorType.DIAMOND;
    }

    /**
     * 是否为闪电判定牌(黑桃2-9)
     */
    public boolean isLightning() {
        return color == ColorType.SPADE && point >= 2 && point <= 9;
    }

    /**
     * 是否为红桃
     */
    public boolean isHeart() {
        return color == ColorType.HEART;
    }

    /**
     * 是否为梅花
     */
    public boolean isClub() {
        return color == ColorType.CLUB;
    }

    /**
     * 是否是某个牌名
     */
    public boolean nameIs(String name) {
        return this.name.equals(name);
    }

    /**
     * 是否是基本
     */
    public boolean isBasic() {
        return type == CardType.BASIC;
    }

    /**
     * 是否是普通锦囊
     */
    public boolean isSkill() {
        return type == CardType.SKILL;
    }


    @Override
    public String toString() {
        String showPoint =
                switch (point) {
                    case 1 -> "A";
                    case 11 -> "J";
                    case 12 -> "Q";
                    case 13 -> "K";
                    default -> point.toString();
                };
        //牌名+花色+点数
        return "[" + name + " " + color + showPoint + "]";
    }
}
