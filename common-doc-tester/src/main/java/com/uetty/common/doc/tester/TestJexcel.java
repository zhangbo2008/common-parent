package com.uetty.common.doc.tester;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.uetty.common.tool.core.excel.JexcelReader;
import com.uetty.common.tool.core.json.codehaus.JacksonUtil;

public class TestJexcel {

	public static void main(String[] args) throws IOException {
//		File file = new File("/home/vince/会议决议模板-地区配置文件数字读取问题.xls");
		File file = new File("/home/vince/项目启动申请excel.xlsx");
		
		JexcelReader jr = new JexcelReader(file);
		List<List<String>> listList = jr.toListList(1);
		for (List<String> list : listList) {
			System.out.println(new JacksonUtil().withIndentOutput().obj2Json(list));
		}
		jr.close();
	}
}
