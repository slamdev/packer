package com.mobiquityinc.packer;

import java.util.ArrayList;
import java.util.List;

public class Pkg {

    public final int maxWeight;
    public final List<Item> items;

    public Pkg(int maxWeight, List<Item> items) {
        this.maxWeight = maxWeight;
        this.items = new ArrayList<>(items);
    }
}
