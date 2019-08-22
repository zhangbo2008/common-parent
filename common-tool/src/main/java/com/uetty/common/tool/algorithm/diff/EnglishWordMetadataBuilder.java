package com.uetty.common.tool.algorithm.diff;


import java.util.ArrayList;
import java.util.List;

/**
 * 将整个英文单词看作一个数据元元
 * @author : Vince
 * @date: 2019/8/22 15:53
 */
public class EnglishWordMetadataBuilder implements MetadataBuilder {

    @Override
    public Metadatas build(String str) {
        List<String> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (!isMatch(c)) {
                if (sb.length() > 0) {
                    list.add(sb.toString());
                    sb.delete(0, sb.length());
                }
            }
            sb.append(c);
        }
        if (sb.length() > 0) {
            list.add(sb.toString());
        }

        Metadatas metadatas = new Metadatas();
        metadatas.setData(list.toArray(new String[0]));
        return metadatas;
    }

    private boolean isMatch(char c) {
        if (c >= 'a' && c <= 'z') return true;
        if (c >= 'A' && c <= 'Z') return true;
        if (c >= '0' && c <= '9') return true;
        if (c == '_') return true;
        return false;
    }
}
