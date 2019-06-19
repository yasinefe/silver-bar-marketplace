package com.yefe.silverbar.marketplace;

import com.yefe.silverbar.marketplace.domain.Order;
import org.junit.Test;

import static com.yefe.silverbar.marketplace.domain.Order.createOrder;
import static com.yefe.silverbar.marketplace.domain.OrderType.BUY;
import static com.yefe.silverbar.marketplace.domain.OrderType.SELL;
import static org.junit.Assert.assertEquals;

public class LiveOrderBoardIntegrationTest {

    private LiveOrderBoard<String> liveOrderBoard = LiveOrderBoard.createWithLineListOrderSummaryRenderer();

    @Test
    public void shouldReturnSellOrderSummaryAsLineListOrdered() {
        Order sellOrder1 = createOrder("user1", 3.5, 306, SELL);
        Order sellOrder2 = createOrder("user2", 1.2, 310, SELL);
        Order sellOrder3 = createOrder("user3", 1.5, 307, SELL);
        Order sellOrder4 = createOrder("user4", 2.0, 306, SELL);

        liveOrderBoard.register(sellOrder1);
        liveOrderBoard.register(sellOrder2);
        liveOrderBoard.register(sellOrder3);
        liveOrderBoard.register(sellOrder4);

        String summary = liveOrderBoard.summary(SELL);

        String expectedResult = "5.5 kg for £306\n1.5 kg for £307\n1.2 kg for £310";

        assertEquals(expectedResult, summary);
    }

    @Test
    public void shouldReturnBuyOrderSummaryAsLineListOrdered() {
        Order buyOrder1 = createOrder("user1", 3.5, 306, BUY);
        Order buyOrder2 = createOrder("user2", 1.2, 310, BUY);
        Order buyOrder3 = createOrder("user3", 1.5, 307, BUY);
        Order buyOrder4 = createOrder("user4", 2.0, 306, BUY);

        liveOrderBoard.register(buyOrder1);
        liveOrderBoard.register(buyOrder2);
        liveOrderBoard.register(buyOrder3);
        liveOrderBoard.register(buyOrder4);

        String summary = liveOrderBoard.summary(BUY);

        String expectedResult = "1.2 kg for £310\n1.5 kg for £307\n5.5 kg for £306";

        assertEquals(expectedResult, summary);
    }
}
