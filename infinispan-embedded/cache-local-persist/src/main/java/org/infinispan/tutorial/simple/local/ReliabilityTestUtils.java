package org.infinispan.tutorial.simple.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class ReliabilityTestUtils {

    public static void cleanUpCacheFiles() {
        try {
            Path path = Paths.get("tmp/cache");
            if (Files.exists(path)) {
                Files.walk(Paths.get("tmp/cache"))
                     .sorted(Comparator.reverseOrder())
                     .map(Path::toFile)
                     .forEach(File::delete);
            }
        } catch (IOException e) {
            //throw new UncheckedIOException(e);
        }
    }
}
