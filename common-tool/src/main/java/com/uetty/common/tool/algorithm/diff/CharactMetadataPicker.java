package com.uetty.common.tool.algorithm.diff;

/**
 * 采拣单个字符作为一个不可分割的数据元
 * <p>每个字符作为一个数据元，内存消耗极大，适合短文本使用(3000字符数以下，具体数值与机器内存相关)</p>
 * @author : Vince
 * @date: 2019/8/22 15:50
 */
public class CharactMetadataPicker implements MetadataPicker {
    @Override
    public Metadatas doPick(String str) {
        Metadatas metas = new Metadatas();//字符串里面每一个字符都当成一个元,
//        Metadatas 用于存储所有的数据元
        metas.setData(new String[str.length()]);
        for (int i = 0; i < str.length(); i++) {
            metas.getData()[i] = str.substring(i, i + 1);
        }
        return metas;
    }
    public static void main(String[] args) {
System.out.println("3333333");

   new CharactMetadataPicker().     doPick("dasfasdfasdf");


    }
}


