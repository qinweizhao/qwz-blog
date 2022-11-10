package com.qinweizhao.blog.model.support;

import lombok.Data;
import lombok.ToString;

import java.util.Comparator;
import java.util.List;

/**
 * Theme file.
 *
 * @since 2019/04/02
 */
@Data
@ToString
public class ThemeFile implements Comparator<ThemeFile> {

    private String name;

    private String path;

    private Boolean isFile;

    private Boolean editable;

    private List<ThemeFile> node;

    @Override
    public int compare(ThemeFile leftFile, ThemeFile rightFile) {
        if (leftFile.isFile && !rightFile.isFile) {
            return 1;
        }

        if (!leftFile.isFile && rightFile.isFile) {
            return -1;
        }

        return leftFile.getName().compareTo(rightFile.getName());
    }
}
