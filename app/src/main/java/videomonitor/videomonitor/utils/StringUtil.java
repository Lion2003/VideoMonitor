package videomonitor.videomonitor.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Title: StringUtil.java
 * @Package com.flygift.framework.util
 * @Description: 字符串工具类
 * @author cjl
 * @date 2014年12月25日 下午5:13:35
 * @version V1.0
 */

public class StringUtil {

	/**
	 * @Title: replaceString
	 * @Description: 替换字符串
	 * @param source
	 * @return String
	 */
	public static String replaceString(String source) {
		if (StringUtil.isEmpty(source))
			return "";
		source = source.replace('\'', '\"');
		source = source.replace("\\[", "\\【");
		source = source.replace("\\]", "\\】");
		source = source.replace("\\<", "\\《");
		source = source.replace("\\>", "\\》");
		return source;
	}

	/**
	 * is null or its length is 0
	 * 
	 * @param str
	 * @return if string is null or its size is 0, return true, else return
	 *         false.
	 */
	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	// *************************************************************************
	/**
	 * 【】(是否只包含数字和字母)
	 * 
	 * @param str
	 * @return
	 */
	// *************************************************************************
	public static boolean isNumberLetters(String str) {
		if (str == null || str.length() == 0)
			return true;

		return !str.matches("[a-zA-Z0-9]+");
	}

	// *************************************************************************
	/**
	 * 【】(是否只包含数字字母和中文)
	 * 
	 * @param str
	 * @return
	 */
	// *************************************************************************
	public static boolean isNumberLettersCharacter(String str) {
		if (str == null || str.length() == 0)
			return true;

		return !str.matches("[0-9a-zA-Z\\u4e00-\\u9fa5]+");
	}

	/**
	 * compare two string
	 * 
	 * @param actual
	 * @param expected
	 * @return
	 * @see ObjectUtil#isEquals(Object, Object)
	 */
//	public static boolean isEquals(String actual, String expected) {
//		return ObjectUtil.isEquals(actual, expected);
//	}

	/**
	 * null string to empty string
	 * 
	 * <pre>
	 * nullStrToEmpty(null) = &quot;&quot;;
	 * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
	 * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static String nullStrToEmpty(String str) {
		return (str == null ? "" : str);
	}
	
	/**
	 * 计算字符串的字节数
	 * @param str
	 * @return
	 */
	public static int getBytesCount(String str) {
		byte[] buff=str.getBytes();
		int f=buff.length;
		return f;
	}

	/**
	 * capitalize first letter
	 * 
	 * <pre>
	 * capitalizeFirstLetter(null)     =   null;
	 * capitalizeFirstLetter("")       =   "";
	 * capitalizeFirstLetter("2ab")    =   "2ab"
	 * capitalizeFirstLetter("a")      =   "A"
	 * capitalizeFirstLetter("ab")     =   "Ab"
	 * capitalizeFirstLetter("Abc")    =   "Abc"
	 * </pre>
	 * 
	 * @param str
	 * @return
	 */
	public static String capitalizeFirstLetter(String str) {
		if (isEmpty(str)) {
			return str;
		}

		char c = str.charAt(0);
		return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length()).append(Character.toUpperCase(c)).append(str.substring(1)).toString();
	}

	/**
	 * encoded in utf-8
	 * 
	 * <pre>
	 * utf8Encode(null)        =   null
	 * utf8Encode("")          =   "";
	 * utf8Encode("aa")        =   "aa";
	 * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
	 * </pre>
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 *             if an error occurs
	 */
	public static String utf8Encode(String str) {
		if (!isEmpty(str) && str.getBytes().length != str.length()) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
			}
		}
		return str;
	}

	/**
	 * encoded in utf-8, if exception, return defultReturn
	 * 
	 * @param str
	 * @param defultReturn
	 * @return
	 */
	public static String utf8Encode(String str, String defultReturn) {
		if (!isEmpty(str) && str.getBytes().length != str.length()) {
			try {
				return URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				return defultReturn;
			}
		}
		return str;
	}

	/**
	 * get innerHtml from href
	 * 
	 * <pre>
	 * getHrefInnerHtml(null)                                  = ""
	 * getHrefInnerHtml("")                                    = ""
	 * getHrefInnerHtml("mp3")                                 = "mp3";
	 * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
	 * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
	 * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
	 * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
	 * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
	 * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
	 * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
	 * </pre>
	 * 
	 * @param href
	 * @return <ul>
	 *         <li>if href is null, return ""</li>
	 *         <li>if not match regx, return source</li>
	 *         <li>return the last string that match regx</li>
	 *         </ul>
	 */
	public static String getHrefInnerHtml(String href) {
		if (isEmpty(href)) {
			return "";
		}

		String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
		Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
		Matcher hrefMatcher = hrefPattern.matcher(href);
		if (hrefMatcher.matches()) {
			return hrefMatcher.group(1);
		}
		return href;
	}

/**
     * process special char in html
     * 
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     * 
     * @param source
     * @return
     */
	public static String htmlEscapeCharsToString(String source) {
		return StringUtil.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
	}

	/**
	 * transform half width char to full width char
	 * 
	 * <pre>
	 * fullWidthToHalfWidth(null) = null;
	 * fullWidthToHalfWidth("") = "";
	 * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
	 * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static String fullWidthToHalfWidth(String s) {
		if (isEmpty(s)) {
			return s;
		}

		char[] source = s.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (source[i] == 12288) {
				source[i] = ' ';
				// } else if (source[i] == 12290) {
				// source[i] = '.';
			} else if (source[i] >= 65281 && source[i] <= 65374) {
				source[i] = (char) (source[i] - 65248);
			} else {
				source[i] = source[i];
			}
		}
		return new String(source);
	}

	/**
	 * transform full width char to half width char
	 * 
	 * <pre>
	 * halfWidthToFullWidth(null) = null;
	 * halfWidthToFullWidth("") = "";
	 * halfWidthToFullWidth(" ") = new String(new char[] {12288});
	 * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
	 * </pre>
	 * 
	 * @param s
	 * @return
	 */
	public static String halfWidthToFullWidth(String s) {
		if (isEmpty(s)) {
			return s;
		}

		char[] source = s.toCharArray();
		for (int i = 0; i < source.length; i++) {
			if (source[i] == ' ') {
				source[i] = (char) 12288;
				// } else if (source[i] == '.') {
				// source[i] = (char)12290;
			} else if (source[i] >= 33 && source[i] <= 126) {
				source[i] = (char) (source[i] + 65248);
			} else {
				source[i] = source[i];
			}
		}
		return new String(source);
	}

	public static String getRandomString(int length) {
		StringBuffer buffer = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
		StringBuffer sb = new StringBuffer();
		Random r = new Random();
		int range = buffer.length();
		for (int i = 0; i < length; i++) {
			sb.append(buffer.charAt(r.nextInt(range)));
		}
		return sb.toString();
	}

	public static String getReverseStr(String str) {
		String strReverse = new StringBuffer(str).reverse().toString();
		return strReverse;
	}

	/**
	 * @author：liaoxy
	 * @Description: 防乱码截取字符
	 * @param str
	 * @param len
	 * @return String
	 */
	public static String getLimitLengthString(String str, int len) {
		try {
			int counterOfDoubleByte = 0;
			byte[] b = str.getBytes("gb2312");
			if (b.length <= len)
				return str;
			for (int i = 0; i < len; i++) {
				if (b[i] < 0) {
					counterOfDoubleByte++;
				}
			}
			if (counterOfDoubleByte % 2 == 0)
				return new String(b, 0, len, "gb2312");
			else
				return new String(b, 0, len - 1, "gb2312");
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * @author：cjy
	 * @Description: 向上取整100
	 * @param value
	 * @return Double
	 */
	public final static Double ceil(double value) {
		long tidy = (long) Math.ceil(value);
		if (tidy % 100 > 0) {
			return new BigDecimal((tidy / 100 + 1) * 100).doubleValue();
		}
		return new BigDecimal(tidy).doubleValue();
	}
	
	/**
	 * 按字节获得字符串长度
	 * @param s
	 * @return
	 */
    public static int getWordCount(String s) {
        int length = 0;
        for(int i = 0; i < s.length(); i++)
        {
            int ascii = Character.codePointAt(s, i);
            if(ascii >= 0 && ascii <=255)
                length++;
            else
                length += 2;
                
        }
        return length;
        
    }

	/**
	 * 得到网页中图片的地址
	 */
	public static List<String> getImgSrcList(String htmlStr) {
		List<String> pics = new ArrayList<String>();

		String regEx_img = "<img.*?src=\"http://(.*?).jpg\""; // 图片链接地址
		Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
		Matcher m_image = p_image.matcher(htmlStr);
		while (m_image.find()) {
			String src = m_image.group(1);
			if (src.length() < 100) {
				pics.add("http://" + src + ".jpg");
			}
		}
		return pics;
	}

	/**
	 * @author：liaoxy
	 * @Description: 将float转为正常字串避免科学计数法显示
	 * @param value
	 * @return String
	 */
	public static String floatToStr(Float value, String fomat) {
		BigDecimal num = new BigDecimal(value + "");
		DecimalFormat df = new DecimalFormat(fomat);
		return df.format(num);
	}
	
	public static boolean checkTelephone(String telephone) {
		Pattern p = Pattern.compile("/^0{0,1}(13[0-9]|15[7-9]|153|156|18[7-9])[0-9]{8}$/");
		Matcher m = p.matcher(telephone);
		return m.matches();
	}

	/**
	 * 保留小数点后两位
	 * @param data
	 * @return
     */
	public static String decimal(double data) {
		String decimal = new DecimalFormat("#.00").format(data);
		// #.00 表示两位小数 #.0000四位小数 以此类推…
		if (decimal.equals(".00")) {
			decimal = "0.00";
		}
		return decimal;
	}

	/**
	 * 验证身份证
	 * @param idNum
	 * @return
     */
	public static boolean isIDCard(String idNum) {
		Pattern pattern = Pattern.compile("((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65)[0-9]{4})" + "(([1|2][0-9]{3}[0|1][0-9][0-3][0-9][0-9]{3}" +
				"[Xx0-9])|([0-9]{2}[0|1][0-9][0-3][0-9][0-9]{3}))");
		Matcher matcher = pattern.matcher(idNum);
		return matcher.matches();
	}

	/**
	 * 验证邮箱
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email){
		boolean flag = false;
		try{
			String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}

	/**
	 * 验证邮编
	 * @param post
	 * @return
	 */
	public static boolean checkPost(String post){
		boolean flag = false;
		try{
			String check = "[1-9]\\d{5}";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(post);
			flag = matcher.matches();
		}catch(Exception e){
			flag = false;
		}
		return flag;
	}
}
