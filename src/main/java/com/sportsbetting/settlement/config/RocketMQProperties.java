package com.sportsbetting.settlement.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.rocketmq")
public class RocketMQProperties {

    private String betSettlementsTopic = "bet-settlements";
}
