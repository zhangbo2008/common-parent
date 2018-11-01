package com.uetty.common.tool.core.string;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class EscapeHtmlUtil {

	static Map<String, String> REPLACE_MAP = new LinkedHashMap<>();
	static {
		
		StringBuffer escape1 = new StringBuffer();
		escape1.append("(?i)");
		escape1.append("(<script([ ]+[^ >]+?)*>.*?</script>)");
		escape1.append("|");
		escape1.append("(<video([ ]+[^ >]+?)*>.*?</video>)");
		escape1.append("|");
		escape1.append("(<audio([ ]+[^ >]+?)*>.*?</audio>)");
		escape1.append("|");
		escape1.append("(<style([ ]+[^ >]+?)*>.*?</style>)");
		escape1.append("|");
		escape1.append("(<colgroup([ ]+[^ >]+?)*>.*?</colgroup>)");
		REPLACE_MAP.put(escape1.toString(), "");
		
		StringBuffer escape2 = new StringBuffer();
		escape2.append("(?i)");
		escape2.append("(<div([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<p([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<br([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<iframe([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<html([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<table([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<thead([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<tbody([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<tr([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<hr([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<h1([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<h2([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<h3([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<h4([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<h5([ ]+[^ >]+?)*>)");
		escape2.append("|");
		escape2.append("(<li([ ]+[^ >]+?)*>)");
		REPLACE_MAP.put(escape2.toString(), "\n");
		
		REPLACE_MAP.put("<img(?=[ ]+[^ >]+?)*>", "IMAGE");
		
		StringBuffer escape3 = new StringBuffer();
		escape3.append("(?i)");
		escape3.append("(<a([ ]+[^ >]+?)*>)|(</a>)");
		escape3.append("|");
		escape3.append("(<td([ ]+[^ >]+?)*>)|(</td>)");
		escape3.append("|");
		escape3.append("(<th([ ]+[^ >]+?)*>)|(</th>)");
		escape3.append("|");
		escape3.append("(<blockquote([ ]+[^ >]+?)*>)|(</blockquote>)");
		REPLACE_MAP.put(escape3.toString(), " ");
		
		StringBuffer escape4 = new StringBuffer();
		escape4.append("(?i)");
		escape4.append("(<span([ ]+[^ >]+?)*>)|(</span>)");
		escape4.append("|");
		escape4.append("(<label([ ]+[^ >]+?)*>)|(</label>)");
		escape4.append("|");
		escape4.append("(<ul([ ]+[^ >]+?)*>)|(</ul>)");
		escape4.append("|");
		escape4.append("(<ol([ ]+[^ >]+?)*>)|(</ol>)");
		escape4.append("|");
		escape4.append("(<u([ ]+[^ >]+?)*>)|(</u>)");
		escape4.append("|");
		escape4.append("(<col([ ]+[^ >]+?)*>)|(</col>)");
		escape4.append("|");
		escape4.append("(<b([ ]+[^ >]+?)*>)|(</b>)");
		escape4.append("|");
		escape4.append("(<script([ ]+[^ >]+?)*>)|(</script>)");
		
		escape4.append("|");
		escape4.append("(<video([ ]+[^ >]+?)*>)|(</video>)");
		escape4.append("|");
		escape4.append("(<audio([ ]+[^ >]+?)*>)|(</audio>)");
		escape4.append("|");
		escape4.append("(<style([ ]+[^ >]+?)*>)|(</style>)");
		escape4.append("|");
		escape4.append("(<colgroup([ ]+[^ >]+?)*>)|(</colgroup>)");
		escape4.append("|");
		escape4.append("(</img>)");
		escape4.append("|");
		escape4.append("(</div>)");
		escape4.append("|");
		escape4.append("(</p>)");
		escape4.append("|");
		escape4.append("(</br>)");
		escape4.append("|");
		escape4.append("(</iframe>)");
		escape4.append("|");
		escape4.append("(</html>)");
		escape4.append("|");
		escape4.append("(</table>)");
		escape4.append("|");
		escape4.append("(</thead>)");
		escape4.append("|");
		escape4.append("(</tbody>)");
		escape4.append("|");
		escape4.append("(</tr>)");
		escape4.append("|");
		escape4.append("(</hr>)");
		escape4.append("|");
		escape4.append("(</h1>)");
		escape4.append("|");
		escape4.append("(</h2>)");
		escape4.append("|");
		escape4.append("(</h3>)");
		escape4.append("|");
		escape4.append("(</h4>)");
		escape4.append("|");
		escape4.append("(</h5>)");
		escape4.append("|");
		escape4.append("(</li>)");
		REPLACE_MAP.put(escape4.toString(), "");
		
	}
	
	public static String escapeHtml(String text) {
		Iterator<String> keyItr = REPLACE_MAP.keySet().iterator();
		while (keyItr.hasNext()) {
			String key = keyItr.next();
			text = text.replaceAll(key, REPLACE_MAP.get(key));
		}
		text = text.replace("<", "&lt;").replace(">", "&gt;");
		text = text.replaceAll("\n+", "\n");
		return text;
	}
	
	public static void main(String[] args) {
		String str = "<p>1234</p><p>&lt;p&gt;哈哈哈哈&lt;/p&gt;</p><ol><li>&lt;script&gt;个为而非哇个&lt;/script&gt;<br/><span style=\"font-size: 1.5em;\">gwafwfaherdsfgw<br>hea<span style=\"color: rgb(226, 139, 65);\">彩色文<a href=\"http://www.example.com\" target=\"_blank\">wg</a>字gwe</span></span></li></ol><blockquote><p>gawefg</p></blockquote><p><u>gwafewf&nbsp;<br>hg链接文字个为额发</u></p><p><u><br></u></p><table><colgroup><col width=\"24.92581602373887%\"><col width=\"25.024727992087044%\"><col width=\"25.024727992087044%\"><col width=\"25.222551928783382%\"></colgroup><thead><tr><th>table</th><th>blee</th><th>tabl</th><th>tab</th></tr></thead><tbody><tr><td>1</td><td>1</td><td>1</td><td>1</td></tr><tr><td>2</td><td>2</td><td>2</td><td>2</td></tr><tr><td>3</td><td>3</td><td>3</td><td>3<br><br></td></tr></tbody></table><p style=\"text-align: right;\">gwgweewaef</p><p style=\"margin-left: 40px;\"><img alt=\"Image\"><br><br></p><hr><h2>gwefagwe<b>gfaef</b></h2><ul><li>wwgwe</li><li>gwe</li><li>wegwa</li><li>wg<br></li></ul><ol><li>gwefa</li><li>sf<br><br><br></li></ol><p>gwaefof</p><ol><li>1. gwefa</li><li>2. wgwef</li><li>3. dgwe<br><br></li></ol>";
//		String str = "表单记录:【版本号】V1.0.4.5【测试包存放位置】<dIv>用</div>火<p g=\"gwe\"  m='gwe'>gwef</p>狐测<br>一<span style=\"color: red;\">次</span>【建议测试内容】<bigtext>火狐来测一测\n" + 
//				"火狐来测一测\n" + 
//				"\n" + 
//				"火狐来测一测\n" + 
//				"火狐来测一测\n" + 
//				"火狐来测一测\n" + 
//				"火狐来测一测\n" + 
//				"火狐来测一测\n" + 
//				"火狐来测一测</bigtext>";
		str = escapeHtml(str);
		System.out.println(str);
		
		
		
	}
}
