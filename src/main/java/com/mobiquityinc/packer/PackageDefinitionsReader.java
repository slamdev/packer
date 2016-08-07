package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;

import java.io.IOException;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Paths.get;

public class PackageDefinitionsReader {

    public String read(String path) {
        if (path == null) {
            throw new APIException("Path should not be null or empty.");
        }
        Path file = get(path);
        if (!exists(file)) {
            throw new APIException("Path should exists.");
        }
        return getFileContent(path);
    }

    private String getFileContent(String path) {
        try {
            return new String(readAllBytes(get(path)), UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
