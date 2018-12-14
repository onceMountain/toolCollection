package com.cy.commander.common.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FileUtils {
    /**
     * 默认精确匹配查找文件（包含扩展名）
     * @param folder
     * @param fileName
     * @return
     */
    public static File[] searchFile(File folder, final String fileName) {
        return searchFile(folder, fileName, false);
    }

    // 递归查找包含关键字的文件
    public static File[] searchFile(File folder, final String keyWord, Boolean isFuzzySearch) {
        // 运用内部匿名类获得文件
        File[] subFolders = folder.listFiles(new FileFilter() {
            // 实现 FileFilter 类的 accept 方法
            @Override
            public boolean accept(File pathname) {
                // 目录
                if (pathname.isDirectory()) {
                    return true;
                }

                if (isFuzzySearch) {
                    // 模糊匹配，文件包名含关键字即可
                    if (pathname.isFile() && pathname.getName().toLowerCase().contains(keyWord.toLowerCase())) {
                        return true;
                    }

                } else {
                    //文件名等于关键字
                    if (pathname.isFile() && pathname.getName().equals(keyWord)) {
                        return true;
                    }
                }
                return false;
            }
        });

        List<File> fileList = new ArrayList<>();
        for (File subFolder : subFolders != null ? subFolders : new File[0]) {
            if (subFolder.isFile()) {
                // 如果是文件则将文件添加到结果列表中
                fileList.add(subFolder);
            } else {
                // 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
                File[] foldFiles = searchFile(subFolder, keyWord, isFuzzySearch);
                // 文件保存到集合中
                fileList.addAll(new ArrayList<>(Arrays.asList(foldFiles)));
            }
        }
        // 转换为数组
        File[] files = new File[fileList.size()];
        fileList.toArray(files);
        return files;
    }

    /**
     * 递归删除文件和文件夹
     * @param root 要删除的根目录
     */
    public static boolean deleteFile(File root) {
        if (root != null && root.exists()) {
            if (root.isDirectory()) {
                File[] childFiles = root.listFiles();
                if (childFiles != null) {
                    for (File child : childFiles) {
                        deleteFile(child);
                    }
                }
            }
            return root.delete();
        }
        return false;
    }

    /**
     * 清空文件夹
     * @param dir 要清空文件夹
     */
    public static boolean cleanUpDir(File dir) {
        if (dir != null && dir.exists() && dir.isDirectory()) {
            File[] childFiles = dir.listFiles();
            if (childFiles != null) {
                for (File child : childFiles) {
                    deleteFile(child);
                }
            }
            return true;
        }
        return false;
    }
}
