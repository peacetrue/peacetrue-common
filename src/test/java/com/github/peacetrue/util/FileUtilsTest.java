package com.github.peacetrue.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.peacetrue.test.SourcePathUtils.getTestResourceAbsolutePath;

/**
 * @author peace
 **/
class FileUtilsTest {

    @Test
    void toPath() {
        Path path = Paths.get("/a");
        Assertions.assertSame(path, FileUtils.toPath(path));
        Assertions.assertEquals(path, FileUtils.toPath("/a"));
        Assertions.assertEquals(path, FileUtils.toPath(new File("/a")));
        Object value = new Object();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> FileUtils.toPath(value));
    }

    @Test
    void getRelativePath() {
        Path basePath = Paths.get("/a/b");
        Path absolutePath = Paths.get("/a1/b/c");
        Assertions.assertThrows(IllegalArgumentException.class, () -> FileUtils.getRelativePath(basePath, absolutePath));
        Assertions.assertEquals(Paths.get("c"), FileUtils.getRelativePath(basePath, Paths.get("/a/b/c")));
        Assertions.assertEquals(Paths.get(""), FileUtils.getRelativePath(basePath, Paths.get("/a/b")));
    }

    @Test
    void isChildRelativePath() {
        Assertions.assertTrue(FileUtils.isParentRelativePath(Paths.get("..")));
        Assertions.assertTrue(FileUtils.isParentRelativePath(Paths.get("aa/../bb/../..")));
        Assertions.assertFalse(FileUtils.isParentRelativePath(Paths.get("aa")));
        Assertions.assertFalse(FileUtils.isParentRelativePath(Paths.get("/aa")));
    }

    @Test
    void createFolderIfAbsent() throws IOException {
        String absentPathString = getTestResourceAbsolutePath("/FileUtilsTest");
        Path absentPath = Paths.get(absentPathString);
        FileUtils.createFolderIfAbsent(absentPath);
        Assertions.assertTrue(Files.exists(absentPath), "成功创建不存在的文件");
        FileUtils.createFolderIfAbsent(absentPath);
        Assertions.assertTrue(Files.exists(absentPath), "成功创建已存在的文件");
        Files.delete(absentPath);
    }

    @Test
    void createFile() throws IOException {
        Path absentPath = Paths.get(getTestResourceAbsolutePath("/FileUtilsTest.java"));
        Assertions.assertFalse(Files.exists(absentPath));
        FileUtils.createFile(absentPath);
        Assertions.assertTrue(Files.exists(absentPath));
        Files.delete(absentPath);

        absentPath = Paths.get("test.txt");
        Assertions.assertFalse(Files.exists(absentPath));
        FileUtils.createFile(absentPath);
        Assertions.assertTrue(Files.exists(absentPath));
        Files.delete(absentPath);
    }

    @Test
    void createFileIfAbsent() throws IOException {
        String absentPathString = getTestResourceAbsolutePath("/FileUtilsTest.java");
        Path absentPath = Paths.get(absentPathString);
        FileUtils.createFileIfAbsent(absentPath);
        Assertions.assertTrue(Files.exists(absentPath), "成功创建不存在的文件");
        FileUtils.createFileIfAbsent(absentPath);
        Assertions.assertTrue(Files.exists(absentPath), "成功创建已存在的文件");
        Files.delete(absentPath);
    }

    @Test
    void createFileSafely() throws IOException {
        String fileName = "FileUtilsTest";
        createFileSafely(fileName, ".java");
        createFileSafely(fileName, "");
    }

    private void createFileSafely(String fileName, String fileExtension) throws IOException {
        String path = getTestResourceAbsolutePath("/" + fileName + fileExtension);
        Path filePath = FileUtils.createFileSafely(Paths.get(path));
        Assertions.assertEquals(path, filePath.toString());

        Path filePath1 = FileUtils.createFileSafely(filePath);
        Assertions.assertEquals(fileName + "(1)" + fileExtension, filePath1.getFileName().toString());

        Path filePath2 = FileUtils.createFileSafely(filePath);
        Assertions.assertEquals(fileName + "(2)" + fileExtension, filePath2.getFileName().toString());

        Files.delete(filePath);
        Files.delete(filePath1);
        Files.delete(filePath2);
    }

    @Test
    void deleteCountToString() {
        Assertions.assertEquals(new FileUtils.DeletedCounts().toString(), new FileUtils.DeletedCounts().toString());
    }

    @Test
    void deleteRecursively() throws IOException {
        Path root = Paths.get(getTestResourceAbsolutePath("/deleteRecursively"));
        Assertions.assertThrows(NoSuchFileException.class, () -> FileUtils.deleteRecursively(root));

        FileUtils.createFileIfAbsent(root);
        FileUtils.DeletedCounts deletedCounts = new FileUtils.DeletedCounts();
        deletedCounts = deletedCounts.increase(FileUtils.deleteRecursively(root));
        Assertions.assertEquals(1, deletedCounts.getRegularFile());
        Assertions.assertEquals(0, deletedCounts.getDirectoryFile());
        Assertions.assertEquals(1, deletedCounts.getTotal());

        FileUtils.createFileIfAbsent(root.resolve("1.txt"));
        FileUtils.createFileIfAbsent(root.resolve("2.txt"));
        FileUtils.createFileIfAbsent(root.resolve("3.txt"));
        deletedCounts = FileUtils.deleteRecursively(root);
        Assertions.assertEquals(3, deletedCounts.getRegularFile());
        Assertions.assertEquals(1, deletedCounts.getDirectoryFile());
        Assertions.assertEquals(4, deletedCounts.getTotal());
    }
}
