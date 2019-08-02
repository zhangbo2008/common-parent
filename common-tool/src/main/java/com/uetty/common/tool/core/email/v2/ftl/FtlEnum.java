package com.uetty.common.tool.core.email.v2.ftl;

/**
 * @author : Vince
 * @date: 2019/8/2 19:34
 */
public enum FtlEnum {

    TEST("/data/ftl/test.ftl");

    FtlEnum(String filePath) {
        this.filePath = filePath;
    }

    String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
