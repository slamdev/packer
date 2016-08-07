package com.mobiquityinc.packer;

import java.util.List;

public class Packer {

    private PackagesFactory factory = new PackagesFactory();

    private PackagesFormatter formatter = new PackagesFormatter();

    private PackageDefinitionsReader reader = new PackageDefinitionsReader();

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("You should pass path to packages definition file.");
            System.exit(1);
        }
        System.out.println(pack(args[0]));
    }

    public static String pack(String path) {
        return new Packer().packAsString(path);
    }

    public String packAsString(String path) {
        String definitions = reader.read(path);
        List<Pkg> packages = factory.create(definitions);
        return formatter.format(packages);
    }
}
