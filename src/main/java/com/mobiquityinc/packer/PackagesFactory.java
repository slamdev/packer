package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.util.stream.Collectors.toList;

public class PackagesFactory {

    static final int MAX_WEIGHT = 100;

    private static final Pattern MULTILINE_PATTERN = Pattern.compile("\\r?\\n");

    private static final Pattern WEIGHT_PATTERN = Pattern.compile("(\\d+) : (.*)");

    private ItemsFactory itemsFactory = new ItemsFactory();

    public List<Pkg> create(String definitions) {
        if (definitions.isEmpty()) {
            throw new APIException("String should contains package weight.");
        }
        List<Pkg> packages = MULTILINE_PATTERN.splitAsStream(definitions)
                .map(this::toPackage)
                .collect(toList());
        return filterByValuableWeight(packages);
    }

    private Pkg toPackage(String definition) {
        Matcher matcher = WEIGHT_PATTERN.matcher(definition);
        if (!matcher.find()) {
            throw new APIException("String should contains package weight.");
        }
        int weight = parseInt(matcher.group(1));
        validateWeight(weight);
        List<Item> items = itemsFactory.create(weight, matcher.group(2));
        return new Pkg(weight, items);
    }

    private void validateWeight(int weight) {
        if (weight > MAX_WEIGHT) {
            throw new APIException("Package weight should be less or equals to " + MAX_WEIGHT + ".");
        }
    }

    private List<Pkg> filterByValuableWeight(List<Pkg> packages) {
        packages.removeIf(pkg1 -> packages.stream()
                .anyMatch(pkg2 -> pkg1 != pkg2
                        && getTotalPrice(pkg1) == getTotalPrice(pkg2)
                        && getTotalWeight(pkg1) >= getTotalWeight(pkg2))
        );
        return packages;
    }

    private double getTotalWeight(Pkg pkg) {
        return pkg.items.stream().mapToDouble(item -> item.weight).sum();
    }

    private int getTotalPrice(Pkg pkg) {
        return pkg.items.stream().mapToInt(item -> item.price).sum();
    }
}
