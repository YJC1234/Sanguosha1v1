package com.me.sanguosha1v1;

import com.me.sanguosha1v1.cards.Card;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "app")
@Component
@Data
public class PlayerProperty {
    private PlayerConfig oneConfig;
    private PlayerConfig twoConfig;

    @Data
    public static class PlayerConfig {
        private String name;
        private int hp;
        private int maxHp;
        private List<Card> handCards;
    }
}
