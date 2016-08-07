package com.mobiquityinc.packer;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;

import static com.mobiquityinc.packer.PackagesFormatter.EMPTY_ITEMS_MARK;
import static com.mobiquityinc.packer.PackagesFormatter.ITEM_SEPARATOR;
import static com.mobiquityinc.packer.TempFileUtil.createTempFile;
import static java.lang.String.join;
import static java.lang.System.lineSeparator;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

public class ITPackerTest {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Rule
    public final SystemErrRule error = new SystemErrRule().enableLog();

    @Rule
    public final SystemOutRule out = new SystemOutRule().enableLog();

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void should_exit_app_with_specified_status_when_no_path_is_passed() {
        exit.expectSystemExitWithStatus(1);
        Packer.main(new String[]{});
    }

    @Test
    public void should_send_message_to_err_when_no_path_is_passed() {
        exit.expectSystemExit();
        Packer.main(new String[]{});
        Assert.assertThat(error.getLog(), not(isEmptyString()));
    }

    @Test
    public void should_process_verification_example() {
        String content = join(lineSeparator(),
                "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)",
                "8 : (1,15.3,€34)",
                "75 : (1,85.31,€29) (2,14.55,€74) (3,3.98,€16) (4,26.24,€55) (5,63.69,€52) (6,76.25,€75) (7,60.02,€74) (8,93.18,€35) (9,89.95,€78)",
                "56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)"
        );
        Path file = createTempFile(content, temporaryFolder);
        String[] args = {file.toString()};
        Packer.main(args);
        String expected = join(lineSeparator(),
                "4",
                EMPTY_ITEMS_MARK,
                "2" + ITEM_SEPARATOR + "7",
                "8" + ITEM_SEPARATOR + "9",
                lineSeparator()
        );
        assertThat(out.getLog() + lineSeparator(), is(expected));
    }
}
