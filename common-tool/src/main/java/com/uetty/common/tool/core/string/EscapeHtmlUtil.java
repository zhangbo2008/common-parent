package com.uetty.common.tool.core.string;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class EscapeHtmlUtil {

	static Map<String, String> REPLACE_MAP = new LinkedHashMap<>();
	static {
		// 标签包含包裹内容一起被删掉的标签名
		String[] fullEscapeTag = new String[]{
				"script", "video", "audio", "style", "colgroup", "select", "img", "option", "optgroup"
				};
		// 头标签被替换成换行符的标签名
		String[] escapeByLFTag = new String[]{
				"div", "p", "br", "iframe", "html", "table", "thead", "tr", "hr",
				"h1", "h2", "h3", "h4", "h5", "li"
		};
		// 头标签与尾标签被替换成空格的标签名
		String[] escapeByBlankTag = new String[]{
				"a", "td", "th", "blockquote", "form", "nav", "code"
		};
		// 其他头标签与尾标签替换成空字符的标签名
		String[] otherTag = new String[] {
				"span", "label", "ul", "ol", "u", "col", "b", "input", "button", "i"
		};
		
		REPLACE_MAP.put("(?s)<!--.*?-->", "");
		
		// 标签包含包裹内容一起被删掉的匹配规则
		StringBuffer escape1 = new StringBuffer();
		escape1.append("(?i)(?s)"); // 大小写不敏感，.匹配行终止符
		for (String tag : fullEscapeTag) {
			escape1.append("(<" + tag + "([ ]+[^ >]+?)*[ ]*>.*?</" + tag + ">)");
			escape1.append("|");
		}
		escape1.delete(escape1.length() - 1, escape1.length());
		REPLACE_MAP.put(escape1.toString(), "");
		
		// 替换成换行符的匹配规则
		StringBuffer escape2 = new StringBuffer();
		escape2.append("(?i)(?s)");
		for (String tag : escapeByLFTag) {
			escape2.append("(<" + tag + "([ ]+[^ >]+?)*[ ]*[/]?>)");
			escape2.append("|");
		}
		escape2.delete(escape2.length() - 1, escape2.length());
		REPLACE_MAP.put(escape2.toString(), "\n");

		// 替换成空格的匹配规则
		StringBuffer escape3 = new StringBuffer();
		escape3.append("(?i)(?s)");
		for (String tag : escapeByBlankTag) {
			escape3.append("(<" + tag + "([ ]+[^ >]+?)*[ ]*[/]?>)|(</" + tag + ">)");
			escape3.append("|");
		}
		escape3.delete(escape3.length() - 1, escape3.length());
		REPLACE_MAP.put(escape3.toString(), "  ");

		// 替换成空字符的匹配规则
		StringBuffer escape4 = new StringBuffer();
		escape4.append("(?i)(?s)");
		for (String tag : otherTag) {
			escape4.append("(<" + tag + "([ ]+[^ >]+?)*[ ]*[/]?>)|(</" + tag + ">)");
			escape4.append("|");
		}
		for (String tag : fullEscapeTag) {// 防止遗漏的只有标签头/尾 的标签
			escape4.append("(<" + tag + "([ ]+[^ >]+?)*[ ]*[/]?>)|(</" + tag + ">)");
			escape4.append("|");
		}
		for (String tag : escapeByLFTag) {
			escape4.append("(</" + tag + ">)");
			escape4.append("|");
		}
		escape4.delete(escape4.length() - 1, escape4.length());
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
//		String str = "<p>1234</p><p>&lt;p&gt;哈哈哈哈&lt;/p&gt;</p><ol><li>&lt;script&gt;个为而非哇个&lt;/script&gt;<br/><span style=\"font-size: 1.5em;\">gwafwfaherdsfgw<br>hea<span style=\"color: rgb(226, 139, 65);\">彩色文<a href=\"http://www.example.com\" target=\"_blank\">wg</a>字gwe</span></span></li></ol><blockquote><p>gawefg</p></blockquote><p><u>gwafewf&nbsp;<br>hg链接文字个为额发</u></p><p><u><br></u></p><table><colgroup><col width=\"24.92581602373887%\"><col width=\"25.024727992087044%\"><col width=\"25.024727992087044%\"><col width=\"25.222551928783382%\"></colgroup><thead><tr><th>table</th><th>blee</th><th>tabl</th><th>tab</th></tr></thead><tbody><tr><td>1</td><td>1</td><td>1</td><td>1</td></tr><tr><td>2</td><td>2</td><td>2</td><td>2</td></tr><tr><td>3</td><td>3</td><td>3</td><td>3<br><br></td></tr></tbody></table><p style=\"text-align: right;\">gwgweewaef</p><p style=\"margin-left: 40px;\"><img alt=\"Image\"><br><br></p><hr><h2>gwefagwe<b>gfaef</b></h2><ul><li>wwgwe</li><li>gwe</li><li>wegwa</li><li>wg<br></li></ul><ol><li>gwefa</li><li>sf<br><br><br></li></ol><p>gwaefof</p><ol><li>1. gwefa</li><li>2. wgwef</li><li>3. dgwe<br><br></li></ol>";
//		String str = "表单记录:【版本号】V1.0.4.5【测试包存放位置】<dIv>用</div>火<p g=\"gwe\"  m='gwe'>gwef</p>狐测<br>一<span style=\"color: red;\">次</span>【建议测试内容】<bigtext>火狐来测一测\n" + 
//				"火狐来测一测\n" + 
//				"\n" + 
//				"火狐来测一测\n" + 
//				"火狐来测一测\n" + 
//				"火狐来测一测\n" + 
//				"火狐来测一测\n" + 
//				"火狐来测一测\n" + 
//				"火狐来测一测</bigtext>";
		String str = "<div class=\"container\">\n" + 
				"  <form class=\"form-horizontal\" role=\"form\">\n" + 
				"    <div class=\"form-group\">\n" + 
				"      <label for=\"basic\" class=\"col-lg-2 control-label\">\n" + 
				"        Large Select (liveSearch enabled, container: 'body')\n" + 
				"      </label>\n" + 
				"      <div class=\"col-lg-5\">\n" + 
				"        <label>\n" + 
				"          Standard\n" + 
				"        </label>\n" + 
				"        <select class=\"selectpicker form-control\" id=\"number\" data-container=\"body\" data-live-search=\"true\" title=\"Select a number\" data-hide-disabled=\"true\"/>\n" + 
				"      </div>\n" + 
				"      <div class=\"col-lg-5\">\n" + 
				"        <label>\n" + 
				"          Multiple (no virtualScroll)\n" + 
				"        </label>\n" + 
				"        <select multiple=\"\" class=\"selectpicker form-control\" id=\"number-multiple\" data-container=\"body\" data-live-search=\"true\" title=\"Select a number\" data-hide-disabled=\"true\" data-actions-box=\"true\" data-virtual-scroll=\"false\"/>\n" + 
				"      </div>\n" + 
				"    </div>\n" + 
				"  </form>\n" + 
				"  <form class=\"form-horizontal\" role=\"form\">\n" + 
				"    <div class=\"form-group\">\n" + 
				"      <label for=\"basic\" class=\"col-lg-2 control-label\">\n" + 
				"        Large Select (liveSearch disabled)\n" + 
				"      </label>\n" + 
				"      <div class=\"col-lg-5\">\n" + 
				"        <label>\n" + 
				"          Standard\n" + 
				"        </label>\n" + 
				"        <select class=\"selectpicker form-control\" id=\"number2\" data-live-search=\"true\" title=\"Select a number\" data-hide-disabled=\"true\"/>\n" + 
				"      </div>\n" + 
				"      <div class=\"col-lg-5\">\n" + 
				"        <label>\n" + 
				"          Multiple\n" + 
				"        </label>\n" + 
				"        <select class=\"selectpicker form-control\" id=\"number2-multiple\" data-live-search=\"true\" title=\"Select a number\" data-hide-disabled=\"false\" data-actions-box=\"true\" multiple=\"\"/>\n" + 
				"      </div>\n" + 
				"    </div>\n" + 
				"  </form>\n" + 
				"  <hr/>\n" + 
				"  <form class=\"form-horizontal\" role=\"form\">\n" + 
				"    <div class=\"form-group\">\n" + 
				"      <label for=\"basic\" class=\"col-lg-2 control-label\">\n" + 
				"        \"Basic\" (liveSearch disabled)\n" + 
				"      </label>\n" + 
				"      <div class=\"col-lg-10\">\n" + 
				"        <select id=\"basic\" class=\"selectpicker show-tick form-control\">\n" + 
				"          <option>\n" + 
				"            cow\n" + 
				"          </option>\n" + 
				"          <option data-subtext=\"option subtext\">\n" + 
				"            bull\n" + 
				"          </option>\n" + 
				"          <option data-divider=\"true\"/>\n" + 
				"          <option class=\"get-class\" disabled=\"\">\n" + 
				"            ox\n" + 
				"          </option>\n" + 
				"          <optgroup label=\"test\" data-subtext=\"optgroup subtext\">\n" + 
				"            <option>\n" + 
				"              ASD\n" + 
				"            </option>\n" + 
				"            <option selected=\"\">\n" + 
				"              Bla\n" + 
				"            </option>\n" + 
				"            <option>\n" + 
				"              Ble\n" + 
				"            </option>\n" + 
				"          </optgroup>\n" + 
				"        </select>\n" + 
				"      </div>\n" + 
				"    </div>\n" + 
				"  </form>\n" + 
				"  <hr/>\n" + 
				"  <form class=\"form-horizontal\" role=\"form\">\n" + 
				"    <div class=\"form-group\">\n" + 
				"      <label for=\"basic\" class=\"col-lg-2 control-label\">\n" + 
				"        \"Basic\" (liveSearch enabled)\n" + 
				"      </label>\n" + 
				"      <div class=\"col-lg-10\">\n" + 
				"        <select id=\"basic\" class=\"selectpicker show-tick form-control\" data-live-search=\"true\">\n" + 
				"          <option>\n" + 
				"            cow\n" + 
				"          </option>\n" + 
				"          <option data-subtext=\"option subtext\">\n" + 
				"            bull\n" + 
				"          </option>\n" + 
				"          <option class=\"get-class\" disabled=\"\">\n" + 
				"            ox\n" + 
				"          </option>\n" + 
				"          <optgroup label=\"test\" data-subtext=\"optgroup subtext\">\n" + 
				"            <option>\n" + 
				"              ASD\n" + 
				"            </option>\n" + 
				"            <option selected=\"\">\n" + 
				"              Bla\n" + 
				"            </option>\n" + 
				"            <option>\n" + 
				"              Ble\n" + 
				"            </option>\n" + 
				"          </optgroup>\n" + 
				"        </select>\n" + 
				"      </div>\n" + 
				"    </div>\n" + 
				"  </form>\n" + 
				"  <hr/>\n" + 
				"  <form class=\"form-horizontal\" role=\"form\">\n" + 
				"    <div class=\"form-group\">\n" + 
				"      <label for=\"basic2\" class=\"col-lg-2 control-label\">\n" + 
				"        \"Basic\" (multiple, maxOptions=1)\n" + 
				"      </label>\n" + 
				"      <div class=\"col-lg-10\">\n" + 
				"        <select id=\"basic2\" class=\"show-tick form-control\" multiple=\"\">\n" + 
				"          <option>\n" + 
				"            cow\n" + 
				"          </option>\n" + 
				"          <option>\n" + 
				"            bull\n" + 
				"          </option>\n" + 
				"          <option class=\"get-class\" disabled=\"\">\n" + 
				"            ox\n" + 
				"          </option>\n" + 
				"          <optgroup label=\"test\" data-subtext=\"another test\">\n" + 
				"            <option>\n" + 
				"              ASD\n" + 
				"            </option>\n" + 
				"            <option selected=\"\">\n" + 
				"              Bla\n" + 
				"            </option>\n" + 
				"            <option>\n" + 
				"              Ble\n" + 
				"            </option>\n" + 
				"          </optgroup>\n" + 
				"        </select>\n" + 
				"      </div>\n" + 
				"    </div>\n" + 
				"  </form>\n" + 
				"  <hr/>\n" + 
				"  <form class=\"form-horizontal\" role=\"form\">\n" + 
				"    <div class=\"form-group\">\n" + 
				"      <label for=\"maxOption2\" class=\"col-lg-2 control-label\">\n" + 
				"        multiple, show-menu-arrow, maxOptions=2\n" + 
				"      </label>\n" + 
				"      <div class=\"col-lg-10\">\n" + 
				"        <select id=\"maxOption2\" class=\"selectpicker show-menu-arrow form-control\" multiple=\"\" data-max-options=\"2\">\n" + 
				"          <option>\n" + 
				"            chicken\n" + 
				"          </option>\n" + 
				"          <option>\n" + 
				"            turkey\n" + 
				"          </option>\n" + 
				"          <option disabled=\"\">\n" + 
				"            duck\n" + 
				"          </option>\n" + 
				"          <option>\n" + 
				"            goose\n" + 
				"          </option>\n" + 
				"        </select>\n" + 
				"      </div>\n" + 
				"    </div>\n" + 
				"  </form>\n" + 
				"  <hr/>\n" + 
				"  <form class=\"form-horizontal\" role=\"form\">\n" + 
				"    <div class=\"form-group form-group-lg\">\n" + 
				"      <label for=\"error\" class=\"col-lg-2 control-label\">\n" + 
				"        error\n" + 
				"      </label>\n" + 
				"      <div class=\"col-lg-10 error\">\n" + 
				"        <select id=\"error\" class=\"selectpicker show-tick form-control\">\n" + 
				"          <option>\n" + 
				"            pen\n" + 
				"          </option>\n" + 
				"          <option>\n" + 
				"            pencil\n" + 
				"          </option>\n" + 
				"          <option selected=\"\">\n" + 
				"            brush\n" + 
				"          </option>\n" + 
				"        </select>\n" + 
				"      </div>\n" + 
				"    </div>\n" + 
				"  </form>\n" + 
				"  <hr/>\n" + 
				"  <form class=\"form-horizontal\" role=\"form\">\n" + 
				"    <div class=\"form-group has-error form-group-lg\">\n" + 
				"      <label class=\"control-label col-lg-2\" for=\"country\">\n" + 
				"        error type 2\n" + 
				"      </label>\n" + 
				"      <div class=\"col-lg-10\">\n" + 
				"        <select id=\"country\" name=\"country\" class=\"form-control selectpicker\">\n" + 
				"          <option selected=\"selected\">\n" + 
				"            Argentina\n" + 
				"          </option>\n" + 
				"          <option>\n" + 
				"            United State\n" + 
				"          </option>\n" + 
				"          <option>\n" + 
				"            Mexico\n" + 
				"          </option>\n" + 
				"        </select>\n" + 
				"        <p class=\"help-block\">\n" + 
				"          No service available in the selected country\n" + 
				"        </p>\n" + 
				"      </div>\n" + 
				"    </div>\n" + 
				"  </form>\n" + 
				"  <hr/>\n" + 
				"  <nav class=\"navbar navbar-default\" role=\"navigation\">\n" + 
				"    <div class=\"container-fluid\">\n" + 
				"      <div class=\"navbar-header\">\n" + 
				"        <a class=\"navbar-brand\" href=\"#\">\n" + 
				"          Navbar\n" + 
				"        </a>\n" + 
				"      </div>\n" + 
				"      <form class=\"navbar-form navbar-left\" role=\"search\">\n" + 
				"        <div class=\"form-group\">\n" + 
				"          <select class=\"selectpicker\" multiple=\"\" data-live-search=\"true\" data-live-search-placeholder=\"Search\" data-actions-box=\"true\">\n" + 
				"            <optgroup label=\"filter1\">\n" + 
				"              <option>\n" + 
				"                option1\n" + 
				"              </option>\n" + 
				"              <option>\n" + 
				"                option2\n" + 
				"              </option>\n" + 
				"              <option>\n" + 
				"                option3\n" + 
				"              </option>\n" + 
				"              <option>\n" + 
				"                option4\n" + 
				"              </option>\n" + 
				"            </optgroup>\n" + 
				"            <optgroup label=\"filter2\">\n" + 
				"              <option>\n" + 
				"                option1\n" + 
				"              </option>\n" + 
				"              <option>\n" + 
				"                option2\n" + 
				"              </option>\n" + 
				"              <option>\n" + 
				"                option3\n" + 
				"              </option>\n" + 
				"              <option>\n" + 
				"                option4\n" + 
				"              </option>\n" + 
				"            </optgroup>\n" + 
				"            <optgroup label=\"filter3\">\n" + 
				"              <option>\n" + 
				"                option1\n" + 
				"              </option>\n" + 
				"              <option>\n" + 
				"                option2\n" + 
				"              </option>\n" + 
				"              <option>\n" + 
				"                option3\n" + 
				"              </option>\n" + 
				"              <option>\n" + 
				"                option4\n" + 
				"              </option>\n" + 
				"            </optgroup>\n" + 
				"          </select>\n" + 
				"        </div>\n" + 
				"        <div class=\"input-group\">\n" + 
				"          <input type=\"text\" class=\"form-control\" placeholder=\"Search\" name=\"q\"/>\n" + 
				"          <div class=\"input-group-btn\">\n" + 
				"            <button class=\"btn btn-default\" type=\"submit\">\n" + 
				"              <i class=\"glyphicon glyphicon-search\">\n" + 
				"              </i>\n" + 
				"            </button>\n" + 
				"          </div>\n" + 
				"        </div>\n" + 
				"        <button type=\"submit\" class=\"btn btn-default\">\n" + 
				"          Search\n" + 
				"        </button>\n" + 
				"      </form>\n" + 
				"    </div>\n" + 
				"    <!-- .container-fluid -->  </nav>\n" + 
				"  <hr/>\n" + 
				"  <select id=\"first-disabled\" class=\"selectpicker\" data-hide-disabled=\"true\" data-live-search=\"true\">\n" + 
				"    <optgroup disabled=\"disabled\" label=\"disabled\">\n" + 
				"      <option selected=\"selected\">\n" + 
				"        Hidden\n" + 
				"      </option>\n" + 
				"    </optgroup>\n" + 
				"    <optgroup label=\"Fruit\">\n" + 
				"      <option>\n" + 
				"        Apple\n" + 
				"      </option>\n" + 
				"      <option>\n" + 
				"        Orange\n" + 
				"      </option>\n" + 
				"    </optgroup>\n" + 
				"    <optgroup label=\"Vegetable\">\n" + 
				"      <option>\n" + 
				"        Corn\n" + 
				"      </option>\n" + 
				"      <option>\n" + 
				"        Carrot\n" + 
				"      </option>\n" + 
				"    </optgroup>\n" + 
				"  </select>\n" + 
				"  <hr/>\n" + 
				"  <select id=\"first-disabled2\" class=\"selectpicker\" multiple=\"\" data-hide-disabled=\"true\" data-size=\"5\">\n" + 
				"    <option>\n" + 
				"      Apple\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Banana\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Orange\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Pineapple\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Apple2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Banana2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Orange2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Pineapple2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Apple2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Banana2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Orange2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Pineapple2\n" + 
				"    </option>\n" + 
				"  </select>\n" + 
				"  <button id=\"special\" class=\"btn btn-default\">\n" + 
				"    Hide selected by disabling\n" + 
				"  </button>\n" + 
				"  <button id=\"special2\" class=\"btn btn-default\">\n" + 
				"    Reset\n" + 
				"  </button>\n" + 
				"  <p>\n" + 
				"    Just select 1st element, click button and check list again\n" + 
				"  </p>\n" + 
				"  <hr/>\n" + 
				"  <div class=\"input-group\">\n" + 
				"    <span class=\"input-group-addon\">\n" + 
				"      @\n" + 
				"    </span>\n" + 
				"    <select class=\"form-control selectpicker\">\n" + 
				"      <option selected=\"selected\">\n" + 
				"        One\n" + 
				"      </option>\n" + 
				"      <option>\n" + 
				"        Two\n" + 
				"      </option>\n" + 
				"      <option>\n" + 
				"        Three\n" + 
				"      </option>\n" + 
				"    </select>\n" + 
				"  </div>\n" + 
				"  <hr/>\n" + 
				"  <div class=\"input-group\">\n" + 
				"    <span class=\"input-group-addon\">\n" + 
				"      @\n" + 
				"    </span>\n" + 
				"    <select class=\"form-control selectpicker\" data-mobile=\"true\">\n" + 
				"      <option selected=\"selected\">\n" + 
				"        One\n" + 
				"      </option>\n" + 
				"      <option>\n" + 
				"        Two\n" + 
				"      </option>\n" + 
				"      <option>\n" + 
				"        Three\n" + 
				"      </option>\n" + 
				"    </select>\n" + 
				"  </div>\n" + 
				"  <p>\n" + 
				"    With \n" + 
				"    <code>\n" + 
				"      data-mobile=\"true\"\n" + 
				"    </code>\n" + 
				"     option.\n" + 
				"  </p>\n" + 
				"  <hr/>\n" + 
				"  <select id=\"done\" class=\"selectpicker\" multiple=\"\" data-done-button=\"true\">\n" + 
				"    <option>\n" + 
				"      Apple\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Banana\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Orange\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Pineapple\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Apple2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Banana2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Orange2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Pineapple2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Apple2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Banana2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Orange2\n" + 
				"    </option>\n" + 
				"    <option>\n" + 
				"      Pineapple2\n" + 
				"    </option>\n" + 
				"  </select>\n" + 
				"  <hr/>\n" + 
				"  <div class=\"form-group\">\n" + 
				"    <label for=\"tokens\">\n" + 
				"      Key words (data-tokens)\n" + 
				"    </label>\n" + 
				"    <select id=\"tokens\" class=\"selectpicker form-control\" multiple=\"\" data-live-search=\"true\">\n" + 
				"      <option data-tokens=\"first\">\n" + 
				"        I actually am called \"one\"\n" + 
				"      </option>\n" + 
				"      <option data-tokens=\"second\">\n" + 
				"        And me \"two\"\n" + 
				"      </option>\n" + 
				"      <option data-tokens=\"last\">\n" + 
				"        I am \"three\"\n" + 
				"      </option>\n" + 
				"    </select>\n" + 
				"  </div>\n" + 
				"  <hr/>\n" + 
				"  <form class=\"form-inline\">\n" + 
				"    <div class=\"form-group\">\n" + 
				"      <label class=\"col-md-1 control-label\" for=\"lunchBegins\">\n" + 
				"        Lunch (Begins search):\n" + 
				"      </label>\n" + 
				"    </div>\n" + 
				"    <div class=\"form-group\">\n" + 
				"      <select id=\"lunchBegins\" class=\"selectpicker\" data-live-search=\"true\" data-live-search-style=\"begins\" title=\"Please select a lunch ...\">\n" + 
				"        <option selected=\"selected\">\n" + 
				"          Hot Dog, Fries and a Soda\n" + 
				"        </option>\n" + 
				"        <option>\n" + 
				"          Burger, Shake and a Smile\n" + 
				"        </option>\n" + 
				"        <option>\n" + 
				"          Sugar, Spice and all things nice\n" + 
				"        </option>\n" + 
				"        <option>\n" + 
				"          Baby Back Ribs\n" + 
				"        </option>\n" + 
				"        <option>\n" + 
				"          A really really long option made to illustrate an issue with the live search in an inline form\n" + 
				"        </option>\n" + 
				"      </select>\n" + 
				"      <yss>\n" + 
				"        this is yss\n" + 
				"      </yss>\n" + 
				"      <img>        " +
				"      <yhs>\n" + 
				"        this is yss\n" + 
				"    \n" + 
				"      </yhs>\n" +
				"      </img>" + 
				"    </div>\n" + 
				"  </form>\n" + 
				"</div>";
		str = escapeHtml(str.replace("\n", " "));
		str = str.replaceAll("[\\n][\\s]*[\\n]", "\n").replaceAll("[ ]{3,}", "  ");
//		str = str.replaceAll("[\\s]", "");
		System.out.println(str);
	}
}
