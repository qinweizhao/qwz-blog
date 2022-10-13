package com.qinweizhao.blog.util;

import com.qinweizhao.blog.exception.ForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * File utilities.
 *
 * @author johnniang
 * @since 4/9/19
 */
@Slf4j
public class FileUtils {


    private FileUtils() {
    }


    /**
     * Convert an InputStream to a String.
     *
     * @param inputStream input stream
     * @return the string content read through the input stream without closing the InputStream
     */
    public static String readString(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
    }

    /**
     * Checks if the given path is empty.
     *
     * @param path path must not be null
     * @return true if the given path is empty; false otherwise
     * @throws IOException e
     */
    public static boolean isEmpty(@NonNull Path path) throws IOException {
        Assert.notNull(path, "Path must not be null");

        if (!Files.isDirectory(path) || Files.notExists(path)) {
            return true;
        }

        try (Stream<Path> pathStream = Files.list(path)) {
            return !pathStream.findAny().isPresent();
        }
    }

    /**
     * Checks directory traversal vulnerability.
     *
     * @param parentPath  parent path must not be null.
     * @param pathToCheck path to check must not be null
     */
    public static void checkDirectoryTraversal(@NonNull String parentPath, @NonNull String pathToCheck) {
        checkDirectoryTraversal(Paths.get(parentPath), Paths.get(pathToCheck));
    }

    /**
     * Checks directory traversal vulnerability.
     *
     * @param parentPath  parent path must not be null.
     * @param pathToCheck path to check must not be null
     */
    public static void checkDirectoryTraversal(@NonNull Path parentPath, @NonNull Path pathToCheck) {
        Assert.notNull(parentPath, "Parent path must not be null");
        Assert.notNull(pathToCheck, "Path to check must not be null");

        if (pathToCheck.normalize().startsWith(parentPath)) {
            return;
        }

        throw new ForbiddenException("你没有权限访问 " + pathToCheck).setErrorData(pathToCheck);
    }


}
