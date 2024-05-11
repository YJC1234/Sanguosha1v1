package com.me.sanguosha1v1.cards.skill;

import com.me.sanguosha1v1.cards.Card;
import com.me.sanguosha1v1.cards.CardType;
import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.players.Player;
import lombok.extern.slf4j.Slf4j;

/**
 * 借刀杀人
 */
@Slf4j
public class JieDaoShaRen extends Card {
    public JieDaoShaRen(Integer point, ColorType color) {
        super("借刀杀人", CardType.SKILL, point, color);
    }

    @Override
    public void realActive(Player from, Player to) {
        if (!to.haveWeapon()) {
            throw new RuntimeException("出现bug，对没有武器的人用了借刀杀人!!!");
        }
        //TODO: 目前的借刀杀人策略是有杀直接杀
        if (to.haveCardInHandorTreasure("杀")) {
            to.useCard("杀", from);
        } else {
            //获得to的武器
            Card weapon = to.getWeapon();
            to.setWeapon(null);
            from.addCard(weapon);
            log.info("{}没有出杀，武器转移给了{}", to.getName(), from.getName());
        }
    }
}
