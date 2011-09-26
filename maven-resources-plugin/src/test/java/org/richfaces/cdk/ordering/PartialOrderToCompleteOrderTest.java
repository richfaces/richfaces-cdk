package org.richfaces.cdk.ordering;

import static java.util.Arrays.asList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Ordering;

public class PartialOrderToCompleteOrderTest {

    private static PartialOrderToCompleteOrder<Integer> order;

    @Before
    public void initialOrder() {
        order = new PartialOrderToCompleteOrder<Integer>();
    }

    @Test
    public void testNone() {
        Assert.assertEquals(asList(), order.getCompletelyOrdered());
    }

    @Test
    public void testSingle() {
        order.addPartialOrder(asList(1));
        Assert.assertEquals(asList(1), order.getCompletelyOrdered());
    }

    @Test
    public void testBasic() {
        order.addPartialOrder(asList(1, 2, 3));
        Assert.assertEquals(asList(1, 2, 3), order.getCompletelyOrdered());
    }

    @Test(expected = IllegalPartialOrderingException.class)
    public void testWrong() {
        order.addPartialOrder(asList(1, 2, 3));
        order.addPartialOrder(asList(1, 3, 2));
    }

    @Test
    public void testComplex() {
        order.addPartialOrder(asList(2, 3));
        order.addPartialOrder(asList(3));
        order.addPartialOrder(asList(2, 3));
        order.addPartialOrder(asList(1, 3, 4));
        order.addPartialOrder(asList(1, 4));
        order.addPartialOrder(asList(2, 4));
        order.addPartialOrder(asList(1, 2, 3, 4));
        order.addPartialOrder(asList(1, 2, 3, 4));
        Assert.assertEquals(asList(1, 2, 3, 4), order.getCompletelyOrdered());
    }
    
    @Test
    public void testCustomOrderingWithMissingValue() {
        order.addPartialOrder(asList(1, 2, 4, 5));
        Assert.assertEquals(asList(1, 2, 4, 5), order.getCompletelyOrdered());
        
        Ordering<Integer> completeOrdering = order.getCompleteOrdering();
        Assert.assertEquals(asList(2, 4, 3), completeOrdering.sortedCopy(asList(4, 2, 3)));
    }
    
    @Test
    public void testCustomOrderingWithMissingValue1() {
        order.addPartialOrder(asList(1, 9));
        order.addPartialOrder(asList(3));
        order.addPartialOrder(asList(6));
        order.addPartialOrder(asList(8));
        order.addPartialOrder(asList(2));
        order.addPartialOrder(asList(4));
        order.addPartialOrder(asList(7));
        Assert.assertEquals(asList(1, 2, 3, 4, 6, 7, 8, 9), order.getCompletelyOrdered());
        
        Ordering<Integer> completeOrdering = order.getCompleteOrdering();
        Assert.assertEquals(asList(1, 2, 3, 4, 6, 7, 8, 9, 5), completeOrdering.sortedCopy(asList(5, 6, 7, 8, 9, 1, 2, 3, 4)));
    }
    
    @Test
    public void testRealWorldUseCase1() {
        order.addPartialOrder(asList(1, 2, 3, 5, 6, 7, 4, 8));
        order.addPartialOrder(asList(9, 1, 2, 10, 3, 4, 11, 12));
    }
}
