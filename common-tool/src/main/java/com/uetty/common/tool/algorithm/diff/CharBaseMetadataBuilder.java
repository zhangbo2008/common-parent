package com.uetty.common.tool.algorithm.diff;

/**
 * 将单个字符看作一个数据元
 * @author : Vince
 * @date: 2019/8/22 15:50
 */
public class CharBaseMetadataBuilder implements MetadataBuilder {
    @Override
    public Metadatas build(String str) {
        Metadatas metas = new Metadatas();
        metas.setData(new String[str.length()]);
        for (int i = 0; i < str.length(); i++) {
            metas.getData()[i] = str.substring(i, i + 1);
        }
        return metas;
    }
}