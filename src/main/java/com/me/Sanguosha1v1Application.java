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
        int nn = 20000;
        for (int i = 0; i < nn; i++) {
            Game.Result result = game.run(isTengFangLangBegin);
            log.warn("第{}局，{}获胜，共进行{}轮", i + 1, result.winner.getName(), result.roundCount);
            if (result.winner.getName().equals("滕芳兰")) {
                tengFangLangWinCount++;
                if (isTengFangLangBegin) {
                    tengFangLanWinCountWhenBegin++;
                }
            } else {
                baiBanWinCount++;
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
        log.warn("一共{}局\n滕芳兰获胜{}局，{}获胜{}局\n滕芳兰先手获胜{}局，{}先手获胜{}局\n滕芳兰被闪电劈死有{}局\n" +
                        "滕芳兰胜率为{}% 平均回合数为{}\n滕芳兰输的局中{}%被闪电劈死",
                nn, tengFangLangWinCount, game.getTwo().getName(), baiBanWinCount, tengFangLanWinCountWhenBegin,
                game.getTwo().getName(), baiBanWinCountWhenBegin,
                game.getTengFangLanDiedByShanDianCount(), formatRate, averageRoundCount, formatDieRate);
    }
}
