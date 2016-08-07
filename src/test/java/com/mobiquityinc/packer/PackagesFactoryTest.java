package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static com.mobiquityinc.packer.PackagesFactory.MAX_WEIGHT;
import static java.util.Collections.singletonList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PackagesFactoryTest {

    @InjectMocks
    private PackagesFactory factory;

    @Mock
    private ItemsFactory itemsFactory;

    @Test(expected = APIException.class)
    public void should_throw_exception_if_weight_not_found() {
        factory.create("");
    }

    @Test
    public void should_return_the_same_size_as_input_lines() {
        when(itemsFactory.create(1, "")).thenReturn(singletonList(new Item(0, 1, 1)));
        when(itemsFactory.create(2, "")).thenReturn(singletonList(new Item(0, 1, 2)));
        List<Pkg> packages = factory.create("1 : \n2 : ");
        assertThat(packages.size(), is(2));
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_weight_not_a_number() {
        factory.create("a : ");
    }

    @Test
    public void should_return_weight_the_same_as_in_input() {
        List<Pkg> packages = factory.create("1 : ");
        assertThat(packages.get(0).maxWeight, is(1));
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_weight_greater_then_allowed() {
        int weight = MAX_WEIGHT + 1;
        factory.create(weight + " : ");
    }

    @Test
    public void should_call_item_factory_with_items_definition() {
        List<Item> items = singletonList(new Item(0, 0, 0));
        when(itemsFactory.create(1, "fake")).thenReturn(items);
        List<Pkg> packages = factory.create("1 : fake");
        assertThat(packages.get(0).items, is(items));
    }

    @Test
    public void should_not_change_packages_if_their_price_is_different() {
        when(itemsFactory.create(1, "(1,1.0,€1)")).thenReturn(singletonList(new Item(1, 1, 1)));
        when(itemsFactory.create(1, "(1,1.0,€2)")).thenReturn(singletonList(new Item(1, 1, 2)));
        List<Pkg> packages = factory.create("1 : (1,1.0,€1)\n1 : (1,1.0,€2)");
        assertThat(packages.size(), is(2));
    }

    @Test
    public void should_remove_heavier_package_of_the_same_price() {
        when(itemsFactory.create(1, "(1,1.0,€1)")).thenReturn(singletonList(new Item(1, 1, 1)));
        when(itemsFactory.create(2, "(1,2.0,€1)")).thenReturn(singletonList(new Item(1, 2, 1)));
        List<Pkg> packages = factory.create("1 : (1,1.0,€1)\n2 : (1,2.0,€1)");
        assertThat(packages.size(), is(1));
        assertThat(packages.get(0).maxWeight, is(1));
    }
}
