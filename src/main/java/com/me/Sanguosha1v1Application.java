package com.me;

import com.me.sanguosha1v1.Game;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class Sanguosha1v1Application implements ApplicationRunner {

    private final Game game;

    public static void main(String[] args) {
        SpringApplication.run(Sanguosha1v1Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean isTengFangLangBegin = true;
        int tengFangLangWinCount = 0;
        int baiBanWinCount = 0;
        int tengFangLanWinCountWhenBegin = 0;
        int baiBanWinCountWhenBegin = 0;
        long averageRoundCount = 0;
        long minHp = 10000;
        int nn = 10000;
        for (int i = 0; i < nn; i++) {
            Game.Result result = game.run(isTengFangLangBegin);
            log.warn("第{}局，{}获胜，共进行{}轮,{}的血量剩余{}",
                    i + 1, result.winner.getName(), result.roundCount, result.winner.getName(), result.winner.getHp());
            if (result.winner.getName().equals("孙坚")) {
                tengFangLangWinCount++;
                minHp = 0;
                if (isTengFangLangBegin) {
                    tengFangLanWinCountWhenBegin++;
                }
            } else {
                baiBanWinCount++;
                minHp = Math.min(minHp, result.winner.getHp());
                if (!isTengFangLangBegin) {
                    baiBanWinCountWhenBegin++;
                }
            }
            averageRoundCount += result.roundCount;
            isTengFangLangBegin = !isTengFangLangBegin;
        }
        averageRoundCount = averageRoundCount / nn;
        double tengFangLanWinRate = (double) tengFangLangWinCount / nn;
        //保留两位小数
        String formatRate = String.format("%.2f", tengFangLanWinRate * 100);
        double dieByShanDianRate = (double) game.getTengFangLanDiedByShanDianCount() / baiBanWinCount;
        String formatDieRate = String.format("%.2f", dieByShanDianRate * 100);
        log.warn("一共{}局\n孙坚获胜{}局，{}获胜{}局\n孙坚先手获胜{}局，{}先手获胜{}局\n" +
                        "孙坚胜率为{}% 平均回合数为{}\n",
                nn, tengFangLangWinCount, game.getTwo().getName(), baiBanWinCount, tengFangLanWinCountWhenBegin,
                game.getTwo().getName(), baiBanWinCountWhenBegin,
                formatRate, averageRoundCount);
        log.warn("{}最低血量为{}", game.getTwo().getName(), minHp);
    }
}
