package com.uetty.common.tool.core.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态显示sql代码块工具
 * @author vince
 */
public class DynamicSqlLoader {

	public static final String BLOCK_OPEN_FLAG = "#[";
	public static final String BLOCK_MIDDLE_FLAG = "]{";
	public static final String BLOCK_CLOSE_FLAG = "}#";
	
	private static final char[] BLOCK_OPEN = BLOCK_OPEN_FLAG.toCharArray(); // 匹配开头
	private static final char[] BLOCK_MIDDLE = BLOCK_MIDDLE_FLAG.toCharArray(); // 匹配中间部分
	private static final char[] BLOCK_CLOSE = BLOCK_CLOSE_FLAG.toCharArray(); // 匹配结尾
	
	private String rawTempSql;
	private List<DynamicBlock> blockList = new ArrayList<DynamicBlock>();
	private Map<String, Boolean> namespaceModeMap = new HashMap<String, Boolean>();

	
	public DynamicSqlLoader(String tempSql) {
		this.rawTempSql = tempSql;
	}
	
	/**
	 * 设置某个命名空间的代码块是否显示
	 */
	public void setNamespaceMode(String namespace, boolean isShow) {
		namespaceModeMap.put(namespace, isShow);
	}

	/**
	 *  检测动态代码块区域
	 */
	private void testBlock() {
		char[] charArray = rawTempSql.toCharArray();
		List<DynamicBlock> openStack = new ArrayList<DynamicBlock>(); // open标志栈
		List<DynamicBlock> middleStack = new ArrayList<DynamicBlock>(); // middle标志栈
		for (int i = 0; i < charArray.length;) { // 使用游标依次检测各字符位
			if (openStack.size() == middleStack.size()) { // 允许open标志检测
				boolean matchOpen = matchOpen(i, charArray);
				if (matchOpen) {
					DynamicBlock ml = new DynamicBlock();
					ml.open = i;
					openStack.add(ml);
					blockList.add(ml);
					i += BLOCK_OPEN.length;
					continue;
				}
			}
			if (openStack.size() > middleStack.size()) { // 允许middle标志检测
				boolean matchMiddle = matchMiddle(i, charArray);
				if (matchMiddle) {
					DynamicBlock ml = openStack.get(openStack.size() - 1);
					ml.middle = i;
					ml.namespace = rawTempSql.substring(ml.open + BLOCK_OPEN.length, ml.middle).trim();
					middleStack.add(ml);
					i += BLOCK_MIDDLE.length;
					continue;
				}
			}
			if (openStack.size() > 0 && openStack.size() == middleStack.size()) { // 允许close标志检测
				boolean matchClose = matchClose(i, charArray);
				if (matchClose) {
					DynamicBlock ml = middleStack.get(middleStack.size() - 1);
					ml.close = i;
					middleStack.remove(middleStack.size() - 1);
					openStack.remove(openStack.size() - 1);
					i += BLOCK_CLOSE.length;
					continue;
				}
			}
			i++;
		}
		
		if (middleStack.size() == openStack.size() && middleStack.size() > 0) { // 未关闭的sql块
			DynamicBlock ml = middleStack.remove(middleStack.size() - 1);
			throw new RuntimeException("no '" + new String(BLOCK_CLOSE) + "' matched to close namespace [" + ml.namespace + "] in '" + rawTempSql.substring(ml.middle + BLOCK_MIDDLE.length) + "'");
		}
		if (openStack.size() > 0) { // 只有open标记的sql块
			DynamicBlock ml = openStack.remove(openStack.size() - 1);
			throw new RuntimeException("'" + new String(BLOCK_MIDDLE) + "' not found in tempSql '" + rawTempSql.substring(ml.open + BLOCK_OPEN.length) + "'");
		}
	}
	
	
	private boolean matchOpen(int index, char[] charArray) {
		if (charArray.length < BLOCK_OPEN.length + index) {
			return false;
		}
		for (int i = index; i < BLOCK_OPEN.length + index; i++) {
			if (BLOCK_OPEN[i - index] != charArray[i]) {
				return false;
			}
		}
		return true;
	}
	
	private boolean matchMiddle(int index, char[] charArray) {
		if (charArray.length < BLOCK_MIDDLE.length + index) {
			return false;
		}
		for (int i = index; i < BLOCK_MIDDLE.length + index; i++) {
			if (BLOCK_MIDDLE[i - index] != charArray[i]) {
				return false;
			}
		}
		return true;
	}
	
	private boolean matchClose(int index, char[] charArray) {
		if (charArray.length < BLOCK_CLOSE.length + index) {
			return false;
		}
		for (int i = index; i < BLOCK_CLOSE.length + index; i++) {
			if (BLOCK_CLOSE[i - index] != charArray[i]) {
				return false;
			}
		}
		return true;
	}
	
	public String loadSql() {
		testBlock();
		
		StringBuffer sb = new StringBuffer();
		List<Integer> closeIndexStack = new ArrayList<Integer>();
		int index = 0;
		for (int i = 0; i < blockList.size(); ) {
			DynamicBlock block = blockList.get(i);
			
			if (block.open < index) {
				i++; // 该区块在游标之前，忽略跳过
			} else {
				// 当前open标志在游标之后，执行后面代码
				if (closeIndexStack.size() == 0 // 没有待关闭的close标志
						|| block.open < closeIndexStack.get(closeIndexStack.size() - 1)) { // 或在待关闭的close标志前，可以开启open标志
					sb.append(rawTempSql.substring(index, block.open));
					Boolean boo = namespaceModeMap.get(block.namespace);
					if (boo != null && boo) {
						index = block.middle + BLOCK_MIDDLE.length;
						closeIndexStack.add(block.close);// 栈中添加待关闭的close标志位置
					} else { // 不显示该块
						index = block.close + BLOCK_CLOSE.length;
					}
					i++; // 当前open标志被使用了，指向下一个
				}
			}
			
			
			int nextOpen = Integer.MAX_VALUE; // 下一个open标志的位置
			if (i < blockList.size()) {
				nextOpen = blockList.get(i).open;
			}
			
			for (int j = closeIndexStack.size() - 1; j >= 0; j--) {
				Integer closeIndex = closeIndexStack.get(j);
				if (closeIndex < nextOpen) { // 该close标志是可完成关闭的
					sb.append(rawTempSql.substring(index, closeIndex));
					index = closeIndex + BLOCK_CLOSE.length; // 更新游标
					closeIndexStack.remove(j);// 将close标志位置从待关闭栈中移除
				} else {
					// 不可关闭
					break;
				}
			}
				
		}
		sb.append(rawTempSql.substring(index));
		
		return sb.toString();
	}
	
	// 动态代码块区域
	class DynamicBlock {
		public String namespace;
		public int open;
		public int middle;
		public int close;
	}
	
	
	public static void main(String[] args) {
		String SEARCH_REQUIRE_LIST = "SELECT DISTINCT(req.`id`),req.*,ou.`name` owner_name,"
				+ "au.`name` apply_name,v.`title_prefix` v_title_prefix,v.`title` v_title,v.`title_suffix` v_title_suffix "
				+ "FROM `t_require` req \n"
				+ "#[userSearch]{"
				+ "LEFT JOIN `t_user` ou ON req.`owner` = ou.`uid` "
				+ "  #[applyUser]{"
				+ "    LEFT JOIN `t_user` au #[userSearch]{"
				+ "      ON req.`apply` = au.`uid`"
				+ "    }#"
				+ "  }# \n "
				+ "}#"
				+ "#[versionSearch]{"
				+ "  LEFT JOIN `t_versions` v "
				+ "    #[noshow]{"
				+ "      #[userSearch ]{"
				+ "        ON req.`vid` = v.`id` \n"
				+ "      }#"
				+ "    }#"
				+ "}#"
				+ "LEFT JOIN `t_product` pmo ON req.`prod_module_id` = pmo.`id` \n "// 后面第二版产品模块编辑功能做了之后，得改下表名
				+ "LEFT JOIN `t_develop_module_option` dmo ON req.`dev_module_id` = dmo.`id` \n "
				+ "WHERE 1=1 {}";
		
		DynamicSqlLoader loader = new DynamicSqlLoader(SEARCH_REQUIRE_LIST);
		loader.setNamespaceMode("userSearch", true);
		loader.setNamespaceMode("versionSearch", true);
		System.out.println(loader.loadSql());
	}
}
