package com.me.sanguosha1v1;

import com.me.sanguosha1v1.cards.ColorType;
import com.me.sanguosha1v1.cards.skill.WuXieKeJi;
import com.me.sanguosha1v1.cards.skill.WuZhongShengYou;
import com.me.sanguosha1v1.players.BaiBan;
import com.me.sanguosha1v1.players.Player;
import com.me.sanguosha1v1.players.SunJian;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
@Slf4j
class Sanguosha1v1ApplicationTests {
    @Autowired
    private Desk desk;
    @Autowired
    private PlayerProperty playerProperty;
    private Player one;
    private Player two;

    @BeforeEach
    public void init() {
        log.info("初始化测试");
        one = new SunJian(desk);
        two = new BaiBan(desk);
        BeanUtils.copyProperties(playerProperty.getOneConfig(), one);
        BeanUtils.copyProperties(playerProperty.getTwoConfig(), two);
        one.setEnemy(two);
        two.setEnemy(one);
        one.setJudgeCards(new ArrayList<>());
        two.setJudgeCards(new ArrayList<>());
        desk.reset();
    }

    @Test
    void TestSunJian() {
        two.addCard(new WuZhongShengYou(1, ColorType.HEART));
        one.addCard(new WuXieKeJi(1, ColorType.HEART));
        two.update();
    }

}
