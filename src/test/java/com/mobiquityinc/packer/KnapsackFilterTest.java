package com.mobiquityinc.packer;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class KnapsackFilterTest {

    @Test
    public void should_remove_item_if_its_weight_greater_than_allowed() {
        KnapsackFilter filter = new KnapsackFilter(asList(new Item(0, 10, 1), new Item(1, 5, 1)), 9);
        List<Item> items = filter.filter();
        assertThat(items.size(), is(1));
        assertThat(items.get(0).index, is(1));
    }

    @Test
    public void should_left_items_with_sum_of_weights_equals_to_allowed() {
        KnapsackFilter filter = new KnapsackFilter(asList(new Item(0, 5, 1), new Item(1, 5, 1)), 10);
        List<Item> items = filter.filter();
        assertThat(items.size(), is(2));
        assertThat(items.get(0).index, is(1));
        assertThat(items.get(1).index, is(0));
    }

    @Test
    public void should_left_item_with_most_valuable_weight() {
        KnapsackFilter filter = new KnapsackFilter(asList(new Item(0, 5, 1), new Item(1, 4, 2)), 5);
        List<Item> items = filter.filter();
        assertThat(items.size(), is(1));
        assertThat(items.get(0).index, is(1));
    }

    @Test
    public void should_left_optimal_items_with_despite_of_valuable_weight() {
        KnapsackFilter filter = new KnapsackFilter(asList(
                new Item(1, 14.55, 74),
                new Item(2, 3.98, 16),
                new Item(3, 26.24, 55),
                new Item(4, 60.02, 74)
        ), 75);
        List<Item> items = filter.filter();
        assertThat(items.size(), is(2));
        assertThat(items.get(0).index, is(4));
        assertThat(items.get(1).index, is(1));
    }
}