package com.mobiquityinc.packer;

import java.util.List;
import java.util.Objects;

import static java.lang.System.lineSeparator;
import static java.util.stream.Collectors.joining;

public class PackagesFormatter {

    static final String EMPTY_ITEMS_MARK = "-";

    static final String ITEM_SEPARATOR = ",";

    public String format(List<Pkg> packages) {
        return packages.stream().map(pkg -> pkg.items).map(this::toItemsString).collect(joining(lineSeparator()));
    }

    private String toItemsString(List<Item> items) {
        if (items.isEmpty()) {
            return EMPTY_ITEMS_MARK;
        }
        return items.stream().map(item -> item.index).map(Objects::toString).collect(joining(ITEM_SEPARATOR));
    }
}
