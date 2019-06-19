package com.yefe.silverbar.marketplace.renderer;

import org.junit.Test;

import static com.yefe.silverbar.marketplace.domain.OrderSummary.createOrderSummary;
import static java.math.BigDecimal.valueOf;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;

public class LineListOrderSummaryRendererTest {

    private OrderSummaryRenderer<String> orderSummaryRenderer = new LineListOrderSummaryRenderer();

    @Test
    public void shouldReturnEmptyStringWhenOrderSummaryListIsEmpty() {
        assertEquals("", orderSummaryRenderer.render(emptyList()));
    }

    @Test
    public void shouldReturnSingleLineWhenOrderSummaryListHasOneItem() {
        String result = orderSummaryRenderer.render(singletonList(
                createOrderSummary(valueOf(5.5), valueOf(306))
        ));
        assertEquals("5.5 kg for £306", result);
    }

    @Test
    public void shouldReturnMultipleLineWhenOrderSummaryListHasMultipleItems() {
        String result = orderSummaryRenderer.render(asList(
                createOrderSummary(valueOf(5.5), valueOf(306)),
                createOrderSummary(valueOf(1.5), valueOf(307)),
                createOrderSummary(valueOf(1.2), valueOf(310))
        ));
        assertEquals("5.5 kg for £306\n1.5 kg for £307\n1.2 kg for £310", result);
    }

}
