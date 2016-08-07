package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import org.junit.Test;

import java.util.List;

import static com.mobiquityinc.packer.ItemsFactory.*;
import static java.lang.String.join;
import static java.util.Collections.nCopies;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ItemsFactoryTest {

    private final ItemsFactory factory = new ItemsFactory();

    @Test
    public void should_return_empty_list_if_empty_definition_passed() {
        List<Item> items = factory.create(10, "");
        assertThat(items.size(), is(0));
    }

    @Test
    public void should_return_the_same_items_count_as_in_definition() {
        List<Item> items = factory.create(10, "(1,1.0,€1) (2,2.0,€2)");
        assertThat(items.size(), is(2));
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_no_data_in_definition() {
        factory.create(10, "()");
    }

    @Test
    public void should_return_the_same_item_index_as_in_definition() {
        List<Item> items = factory.create(10, "(1,2.0,€3)");
        assertThat(items.get(0).index, is(1));
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_index_is_not_a_number() {
        factory.create(10, "(a,2.0,€3)");
    }

    @Test
    public void should_return_the_same_item_weight_as_in_definition() {
        List<Item> items = factory.create(10, "(1,2.2,€3)");
        assertThat(items.get(0).weight, is(2.2));
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_weight_is_not_a_number() {
        factory.create(10, "(1,a,€2)");
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_weight_is_not_a_double() {
        factory.create(10, "(1,1,€2)");
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_weight_is_greater_than_allowed() {
        factory.create(10, "(1," + (MAX_WEIGHT + 1) + ",€2)");
    }

    @Test
    public void should_return_the_same_item_price_as_in_definition() {
        List<Item> items = factory.create(10, "(1,1.0,€2)");
        assertThat(items.get(0).price, is(2));
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_price_is_not_a_number() {
        factory.create(10, "(1,1.0,€a)");
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_price_is_greater_than_allowed() {
        factory.create(10, "(1,1.0,€" + MAX_PRICE + 1 + ")");
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_items_size_greater_then_allowed() {
        String definition = join(" ", nCopies(MAX_ITEMS_SIZE + 1, "(1,1.0,€2)"));
        factory.create(10, definition);
    }
}
