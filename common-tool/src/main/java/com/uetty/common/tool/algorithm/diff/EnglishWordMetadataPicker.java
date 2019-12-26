package com.uetty.common.tool.algorithm.diff;


import java.util.ArrayList;
import java.util.List;

/**
 * 采拣英文单词（[0-9a-zA-Z_]+）作为一个不可分割的数据元
 * <p>适合像代码文件、英文文章一类的有空格等符号作为分割符的文件比较</p>
 * @author : Vince
 * @date: 2019/8/22 15:53
 */
public class EnglishWordMetadataPicker implements MetadataPicker {

    @Override  //pick函数把,一个str扔给一个数据源.然后返回这个数据源.也就是str--数据元
    //的转化函数.
    public Metadatas doPick(String str) {
        List<String> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder(); //这个处理string速度快.
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

    public static void main(String[] args) {
        new EnglishWordMetadataPicker().doPick("dsfadsfasdf_asdf 3423423 34234 324");
    }
}
