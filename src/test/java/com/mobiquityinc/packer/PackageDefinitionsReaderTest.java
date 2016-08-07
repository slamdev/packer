package com.mobiquityinc.packer;

import com.mobiquityinc.exception.APIException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;

import static com.mobiquityinc.packer.TempFileUtil.createTempFile;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PackageDefinitionsReaderTest {

    private final PackageDefinitionsReader reader = new PackageDefinitionsReader();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test(expected = APIException.class)
    public void should_throw_exception_if_path_is_null() {
        reader.read(null);
    }

    @Test(expected = APIException.class)
    public void should_throw_exception_if_path_is_not_exists() {
        reader.read("fake-path");
    }

    @Test
    public void should_return_content_from_file() {
        String content = "some-content";
        Path file = createTempFile(content, temporaryFolder);
        assertThat(reader.read(file.toString()), is(content));
    }
}
