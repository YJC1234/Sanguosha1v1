package com.me.sanguosha1v1.cards;

/**
 * 花色
 */
public enum ColorType {
    //黑桃，红桃，梅花，方片
    SPADE, HEART, CLUB, DIAMOND;

    @Override
    public String toString() {
        return switch (this) {
            case SPADE -> "♠";
            case HEART -> "♥";
            case CLUB -> "♣";
            case DIAMOND -> "♦";
            default -> "";
        };
    }
}
