package com.github.peacetrue.util;

import lombok.Getter;
import lombok.ToString;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * 路径工具类。
 *
 * @author peace
 * @see Files
 * @see Path
 */
public abstract class FileUtils {

    /** 父级相对路径 */
    public static final String PARENT_RELATIVE_PATH = "..";
    /** 当前相对路径 */
    public static final String CURRENT_RELATIVE_PATH = ".";

    private FileUtils() {
    }

    /**
     * 是否父级相对路径，父级相对路径在父级路径的范围外，即以 .. 起始。
     *
     * @param relativePath 相对路径
     * @return true 如果是父级相对路径，否则 false
     */
    public static boolean isParentRelativePath(Path relativePath) {
        return relativePath.normalize().startsWith(PARENT_RELATIVE_PATH);
    }

    /**
     * 获取相对路径，相对路径 = 绝对路径 - 基础路径。
     *
     * @param basePath     基础路径
     * @param absolutePath 绝对路径
     * @return 相对路径
     */
    public static Path getRelativePath(Path basePath, Path absolutePath) {
        if (!absolutePath.startsWith(basePath)) {
            throw new IllegalArgumentException("absolutePath must be start with basePath");
        }
        int beginIndex = basePath.getNameCount();
        int endIndex = absolutePath.getNameCount();
        if (beginIndex == endIndex) return Paths.get("");
        return absolutePath.subpath(beginIndex, endIndex);
    }

    /**
     * 转换为路径。
     *
     * @param value 可转换为路径的任意值
     * @return 路径
     * @throws UnsupportedOperationException 如果参数 value 的类型不是 {@link String} 或 {@link Path} 或 {@link File}，抛出此异常
     */
    public static Path toPath(Object value) throws UnsupportedOperationException {
        if (value instanceof Path) return (Path) value;
        if (value instanceof String) return Paths.get((String) value);
        if (value instanceof File) return ((File) value).toPath();
        String message = "The value must be String or Path or File, Other type is not supported";
        throw new UnsupportedOperationException(message);
    }

    /**
     * 创建目录，当其不存在时。
     *
     * @param path 目录路径
     * @return 入参目录路径
     * @throws IOException 创建目录过程中发生异常
     */
    public static Path createFolderIfAbsent(Path path) throws IOException {
        return Files.exists(path) ? path : Files.createDirectories(path);
    }

    /**
     * 创建文件，同时创建父级目录（如果不存在）；如果文件存在会覆盖。
     *
     * @param path 文件路径
     * @return 入参文件路径
     * @throws IOException 创建文件过程中发生异常
     */
    public static Path createFile(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null) createFolderIfAbsent(parent);
        return Files.createFile(path);
    }

    /**
     * 创建文件，当其不存在时。
     *
     * @param path 文件路径
     * @return 入参目录路径
     * @throws IOException 创建文件过程中发生异常
     */
    public static Path createFileIfAbsent(Path path) throws IOException {
        return Files.exists(path) ? path : createFile(path);
    }

    /**
     * 安全地创建文件，文件已存在时，重命名为一个不存在的文件。
     * <p>
     * 示例：
     * <pre>
     * /xx/abc.zip = /xx/abc(1).zip
     * /xx/abc(1).zip = /xx/abc(2).zip
     * </pre>
     *
     * @param path 文件路径
     * @return 最终创建的文件路径，如果重命名了，则与入参文件路径不同
     * @throws IOException 创建文件过程中发生异常
     */
    public static Path createFileSafely(Path path) throws IOException {
        if (Files.notExists(path)) return FileUtils.createFile(path);

        String fileName = path.getFileName().toString();
        int index = fileName.lastIndexOf('.');
        String name = index == -1 ? fileName : fileName.substring(0, index);
        String extension = index == -1 ? "" : '.' + fileName.substring(index + 1);
        int i = 0;
        do {
            fileName = String.format("%s(%s)%s", name, ++i, extension);
            path = path.resolveSibling(fileName);
        } while (Files.exists(path));
        return FileUtils.createFile(path);
    }


    /**
     * 递归删除文件。
     *
     * @param path 文件路径
     * @return 删除的文件数目
     * @throws IOException 删除文件过程中发生异常
     */
    public static DeletedCounts deleteRecursively(Path path) throws IOException {
        return deleteRecursively(path, new DeletedCounts());
    }

    /**
     * 递归删除文件，如果删除异常能知道异常之前已经删除了多少文件。
     *
     * @param path          文件路径
     * @param deletedCounts 删除的文件数
     * @return 删除的文件数目
     * @throws IOException 删除文件过程中发生异常
     */
    public static DeletedCounts deleteRecursively(Path path, DeletedCounts deletedCounts) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                deletedCounts.regularFile++;
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                deletedCounts.directoryFile++;
                return FileVisitResult.CONTINUE;
            }
        });
        return deletedCounts;
    }

    /**
     * 删除文件数目。
     *
     * @author peace
     */
    @Getter
    @ToString
    public static class DeletedCounts {

        /** 规则文件数 */
        private int regularFile;
        /** 目录文件数 */
        private int directoryFile;

        /** 设置删除的规则文件数和目录文件数都为 0。 */
        public DeletedCounts() {
            this(0, 0);
        }

        private DeletedCounts(int regularFile, int directoryFile) {
            this.regularFile = regularFile;
            this.directoryFile = directoryFile;
        }

        /**
         * 拷贝一个删除数目对象。
         *
         * @param deletedCounts 删除数目对象
         */
        public DeletedCounts(DeletedCounts deletedCounts) {
            this.regularFile = deletedCounts.regularFile;
            this.directoryFile = deletedCounts.directoryFile;
        }

        /**
         * 获取总数。
         *
         * @return 总数
         */
        public int getTotal() {
            return regularFile + directoryFile;
        }

        /**
         * 递增删除文件数目，返回新对象。
         *
         * @param increment 删除文件数目对象
         * @return 新的删除文件数目对象
         */
        public DeletedCounts increase(DeletedCounts increment) {
            DeletedCounts deletedCounts = new DeletedCounts(this);
            increase(deletedCounts, increment);
            return deletedCounts;
        }

        /**
         * 递增删除文件数目。
         *
         * @param source    源始删除文件对象
         * @param increment 递增删除文件对象
         */
        public static void increase(DeletedCounts source, DeletedCounts increment) {
            source.regularFile += increment.regularFile;
            source.directoryFile += increment.directoryFile;
        }
    }

}
