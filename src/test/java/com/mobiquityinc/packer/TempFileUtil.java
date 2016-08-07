package com.mobiquityinc.packer;

import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public final class TempFileUtil {

    private TempFileUtil() {
        // Utility class
    }

    public static Path createTempFile(String content, TemporaryFolder temporaryFolder) {
        try {
            File file = temporaryFolder.newFile();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(content);
            }
            return file.toPath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
