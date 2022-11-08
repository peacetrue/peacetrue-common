package com.github.peacetrue;

import com.github.peacetrue.test.SourcePathUtils;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author peace
 **/
public class HelpAntora {

    public static final Path PROJECT_DIR = Paths.get(SourcePathUtils.getProjectAbsolutePath());
    public static final Path EXAMPLES_DIR = PROJECT_DIR.resolve("docs/antora/modules/ROOT/examples");
    public static final Path BEAN_DIR = EXAMPLES_DIR.resolve("bean");
    public static final Path POSIX_FILE_ATTRIBUTE_VIEW_DIR = BEAN_DIR.resolve("PosixFileAttributeView");
}
