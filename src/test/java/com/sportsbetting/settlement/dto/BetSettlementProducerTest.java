package com.sportsbetting.settlement.dto;

import com.sportsbetting.settlement.config.RocketMQProperties;
import com.sportsbetting.settlement.service.BetSettlementProducer;
import com.sportsbetting.settlement.service.InMemorySettlementQueue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BetSettlementProducerTest {

    @Mock
    private RocketMQProperties rocketMQProperties;

    @Test
    void send_offersToInMemoryQueue() {
        when(rocketMQProperties.getBetSettlementsTopic()).thenReturn("bet-settlements");

        InMemorySettlementQueue inMemoryQueue = new InMemorySettlementQueue();
        BetSettlementProducer producer = new BetSettlementProducer(rocketMQProperties, inMemoryQueue);

        BetSettlementMessage message = BetSettlementMessage.builder()
                .betId("b1")
                .userId("u1")
                .eventId("e1")
                .betAmount(new BigDecimal("100"))
                .won(true)
                .build();

        producer.send(message);

        assertThat(inMemoryQueue.getQueue()).hasSize(1);
        assertThat(inMemoryQueue.getQueue().poll()).isEqualTo(message);
    }
}
