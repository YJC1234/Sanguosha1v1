package com.me.sanguosha1v1;

import com.me.sanguosha1v1.exception.DeskEmptyException;
import com.me.sanguosha1v1.players.BaiBan;
import com.me.sanguosha1v1.players.Player;
import com.me.sanguosha1v1.players.TengFangLan;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


/**
 * 游戏框架
 */

@Component
@RequiredArgsConstructor
@Data
@Slf4j
public class Game {
    private final PlayerProperty playerProperty;

    private final Desk desk;
    private Player one;
    private Player two;
    private int round; //回合数
    private int roundCount; //轮次，回合数/2 + 1

    /**
     * 辅助
     */
    private Integer tengFangLanDiedByShanDianCount = 0;

    //内部类，表示游戏结果
    public static class Result {
        public Player winner;
        public Player loser;
        public int roundCount; //结束轮次
    }

    /**
     * 初始化游戏
     */
    public void reset() {
        round = 0;
        roundCount = 1;
        desk.reset();

        one = new TengFangLan(desk);
        two = new BaiBan(desk);

        one.setEnemy(two);
        BeanUtils.copyProperties(playerProperty.getOneConfig(), one);
        one.setHandCards(desk.draw(4)); //随机抽4张初试手牌
        one.setTreasureCards(new ArrayList<>(5));
        one.setJudgeCards(new ArrayList<>());

        two.setEnemy(one);
        BeanUtils.copyProperties(playerProperty.getTwoConfig(), two);
        two.setHandCards(desk.draw(4)); //随机抽4张初试手牌
        two.setTreasureCards(new ArrayList<>(5));
        two.setJudgeCards(new ArrayList<>());

    }

    public Result run(boolean isOneBegin) {
        reset();
        try {
            while (!isOver()) {
                //新一轮开始
                if (round % 2 == 0) {
                    startNewRoundCount();
                }
                update(isOneBegin);
                round++;
                roundCount = round / 2 + 1;
            }
            return end();
        } catch (Exception e) {
            handleException(e);
        }
        return null;
    }

    /**
     * 每轮的开始
     */
    private void startNewRoundCount() {
        one.startNewRoundCount();
        two.startNewRoundCount();
    }

    private Result end() {
        Result result = new Result();
        if (one.getHp() <= 0) {
            result.winner = two;
            result.loser = one;
        } else {
            result.winner = one;
            result.loser = two;
        }
        result.roundCount = roundCount;
        log.info("游戏结束，胜利者：{}，剩余血量：{}，结束轮次：{}",
                result.winner.getName(), result.winner.getHp(), result.roundCount);
        if (result.loser == one && result.loser.isDiedByShanDian()) {
            tengFangLanDiedByShanDianCount++;
        }
        return result;
    }

    private void handleException(Exception e) {
        if (e instanceof DeskEmptyException) {
            log.error("牌堆已空，游戏结束");
        } else {
            log.error("未知异常", e);
        }
    }

    private boolean isOver() {
        if (one.getHp() <= 0 || two.getHp() <= 0) {
            return true;
        }
        return false;
    }

    private void update(boolean isOneBegin) {
        one.setRound(round);
        two.setRound(round);
        showDetail();
        if ((round % 2 == 0 && isOneBegin) || (round % 2 == 1 && !isOneBegin)) {
            one.update();
        } else {
            two.update();
        }
    }

    private void showDetail() {
        log.info("\n\n");
        log.info("回合数：{}，轮次：{}", round, roundCount);
        showPlayer(one);
        showPlayer(two);
        //debug:查看一下抽牌堆+弃牌堆+双方手牌+双方装备区+双方判定区的牌的牌数，理论应该不变
//        log.info("test :{}",
//                desk.getDrawCards().size() + desk.getDiscardCards().size()
//                        + one.getHandCards().size() + two.getHandCards().size()
//                        + one.getEquips().size() + two.getEquips().size()
//                        + one.getJudgeCards().size() + two.getJudgeCards().size());

        log.info("");
    }

    private void showPlayer(Player two) {
        log.info("{} : {}/{}", two.getName(), two.getHp(), two.getMaxHp());
        log.info("手牌:{}", two.getHandCards());
        log.info("装备:{}", two.getEquips());
        if (two.haveTreasure()) {
            log.info("木马的牌:{}", two.getTreasureCards());
        }
        log.info("判定区:{}", two.getJudgeCards());
    }


}
