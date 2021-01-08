package com.dksys.biz.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtil {
    private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);
    public static String EMPTY_STR = "";
    /**
     * The empty String <code>""</code>.
     * @since 2.0
     */
    public static final String EMPTY = "";

    /**
     * Represents a failed index search.
     * @since 2.1
     */
    public static final int INDEX_NOT_FOUND = -1;

    /**
     * <p>The maximum size to which the padding constant(s) can expand.</p>
     */
    private static final int PAD_LIMIT = 8192;

    /**
     * <p><code>StringUtils</code> instances should NOT be constructed in
     * standard programming. Instead, the class should be used as
     * <code>StringUtils.trim(" foo ");</code>.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public StringUtil() {
        super();
    }
    /**
     * NullChange
     * 
     * @return String null값을 ""로 변환해서 리턴한다.
     */
    public static String nvl(String val) {
        return nvl(val, "");
    }
    public static String nvl(String val, String rep_val) {
        if (isEmpty(val)) {
            if (isEmpty(rep_val)) {
                val = "";
            } else {
                val = rep_val;
            }
        }
        return val;
    }
    public static String nvlObj(Object val, String rep_val) {
        return nvl((val == null ? "" : (String) val), rep_val);
    }
    public static String nvlObj(Object val) {
        return nvl((val == null ? "" : (String) val), "");
    }
    /**
     * NullTrimChange
     * 
     * @return String null값을 ""로, 혹은 공백문자 포함된 스트링을 공백문자 없애서 리턴한다.
     */
    public static String nvl_trim(String val) {
        return nvl(val).trim();
    }
    /**
     * NullChange
     * 
     * @return String null값을 rep_val 로, 혹은 공백문자 포함된 스트링을 공백문자 없애서 리턴한다.
     */
    public static String nvl_trim(String val, String rep_val) {
        return nvl(val, rep_val).trim();
    }
    /**
     * String의 Null Check
     * 
     * @param tmpString
     * @return null일 경우 true, null이 아닐경우 false
     */
    public static boolean isNull(String tmpString) {
        return ObjectUtil.isNull(tmpString);
    }
    /**
     * String[]의 Null Check
     * 
     * @param tmpString
     *            String 배열
     * @return null일 경우 true, null이 아닐경우 false
     */
    public static boolean isNull(String[] tmpString) {
        return ObjectUtil.isArrayNull(tmpString);
    }
    /**
     * String의 Blank여부 체크
     * 
     * @param tmpString
     * @return null, "" 일 경우 true, null이 아니며, Blank String일 경우 false
     */
    public static boolean isBlank(String tmpString) {
        if (isNull(tmpString)) {
            return true;
        }
        if ("".equals(tmpString)) {
            return true;
        }
        return false;
    }
    /**
     * String[]의 Blank여부 체크
     * 
     * @param tmpString
     *            Stirng 배열
     * @return null, "" 일 경우 true, null이 아니며, Blank String일 경우 false
     */
    public static boolean isBlank(String[] tmpString) {
        if (isNull(tmpString)) {
            return true;
        }
        if (tmpString.length > 0) {
            return false;
        }
        return true;
    }
    /**
     * Object가 null일 경우 Blank String으로 반환한다.
     * 
     * @param obj
     * @return String 입력 Object가 null일 경우 "" 반환, 이외의 경우에는 입력 Object의 toString()
     *         반환
     * @throws UnsupportedEncodingException
     */
    public static String null2Blank(Object obj) {
        if (!ObjectUtil.isNull(obj)) {
            String returnValue = obj.toString();
            return returnValue;
        }
        return "";
    }
    /**
     * 특정문자를 변환한다.
     * 
     * @param source
     *            source 문자열
     * @param pattern
     *            바꿀 문자패턴
     * @param replace
     *            적용할 문자패턴
     * @return 변환된 문자
     */
    public static String replace(String source, String pattern, String replace) {
        if (source != null) {
            final int len = pattern.length();
            StringBuffer sb = new StringBuffer();
            int found = -1;
            int start = 0;
            while ((found = source.indexOf(pattern, start)) != -1) {
                sb.append(source.substring(start, found));
                sb.append(replace);
                start = found + len;
            }
            sb.append(source.substring(start));
            return sb.toString();
        }
        return "";
    }
    /**
     * 스트링을 우측을 기준으로 size크기의 String을 반환한다.
     * 
     * @param str
     *            입력String
     * @param size
     *            획득코저하는 사이즈
     * @return 우측을 기준으로 size 만큼의 String
     */
    public static String getRight(String str, int size) {
        int tmpStringLength = str.length();
        if (size >= tmpStringLength) {
            return str;
        }
        return str.substring(tmpStringLength - size, str.length());
    }
    /**
     * String을 좌측을 기준으로 size크기의 String을 반환한다.
     * 
     * @param str
     *            입력String
     * @param size
     *            획득코저하는 사이즈
     * @return 좌측을 기준으로 size 만큼의 String
     */
    public static String getLeft(String str, int size) {
        int tmpStringLength = str.length();
        if (size >= tmpStringLength) {
            return str;
        }
        return str.substring(0, size);
    }
    /**
     * String을 XML로 변환하기 위해 특수문자를 XML 형식으로 변환
     * 
     * @param strString
     *            변환할 문자열
     * @return 변화된 문자열
     */
    public static String str2XML(String strString) {
        String result = convert2APOS(convert2QUOT(convert2GT(convert2LT(convert2AMP(strString)))));
        return result;
    }
    /**
     * XML형식으로 변환되어 있는 특수문자를 원본으로 변환
     * 
     * @param strString
     *            변환할 문자열
     * @return 변화된 문자열
     */
    public static String xml2Str(String strString) {
        String result = reverse2APOS(reverse2QUOT(reverse2GT(reverse2LT(reverse2AMP(strString)))));
        return result;
    }
    /**
     * 특수문자를 XML에 맞도록 변환해주는 메소드
     * 
     * @param srcText
     *            변환할 문자열
     * @return 변화된 문자열
     */
    public static String str2Html(String srcText) {
        if (isBlank(srcText)) {
            return "";
        }
        String strip = "";
        strip = "&";
        srcText = replace(srcText, strip, "&amp;");
        strip = "<";
        srcText = replace(srcText, strip, "&lt;");
        strip = ">";
        srcText = replace(srcText, strip, "&gt;");
        strip = "\\n";
        srcText = replace(srcText, strip, "<br>");
        strip = "\"";
        srcText = replace(srcText, strip, "&quot;");
        strip = "'";
        srcText = replace(srcText, strip, "&apos;");
        strip = " ";
        srcText = replace(srcText, strip, "&nbsp;");
        return srcText;
    }
    /**
     * '<' 를 &lt; 로 변환
     * 
     * @param strString
     *            String
     * @return String
     */
    public static String convert2LT(String strString) {
        if (isBlank(strString)) {
            return "";
        }
        return replace(strString, "<", "&lt;");
    }
    /**
     * '&lt'를 '<'로 변환
     * 
     * @param strString
     *            변환할 문자열
     * @return 변화된 문자열
     */
    public static String reverse2LT(String strString) {
        if (isBlank(strString)) {
            return "";
        }
        return replace(strString, "&lt;", "<");
    }
    /**
     * >를 &gt;로 변환
     * 
     * @param strString
     * @return String
     */
    public static String convert2GT(String strString) {
        if (isBlank(strString)) {
            return "";
        }
        return replace(strString, ">", "&gt;");
    }
    /**
     * '&gt;'를 '>'로 변환
     * 
     * @param strString
     *            변환할 문자열
     * @return 변화된 문자열
     */
    public static String reverse2GT(String strString) {
        if (isBlank(strString)) {
            return "";
        }
        return replace(strString, "&gt;", ">");
    }
    /**
     * & 를 HTML &amp; 로 변환
     * 
     * @param strString
     *            변환할 문자열
     * @return String 변화된 문자열
     */
    public static String convert2AMP(String strString) {
        if (isBlank(strString)) {
            return "";
        }
        return replace(strString, "&", "&amp;");
    }
    /**
     * '&amp;'를 '&'로 변환
     * 
     * @param strString
     *            변환할 문자열
     * @return 변화된 문자열
     */
    public static String reverse2AMP(String strString) {
        if (isBlank(strString)) {
            return "";
        }
        return replace(strString, "&amp;", "&");
    }
    /**
     * '\r'을 <br>
     * 로 변환해주는 메소드
     * 
     * @param strString
     *            String
     * @return String
     */
    public static String convert2BR(String strString) {
        if (isBlank(strString)) {
            return "";
        }
        return replace(strString, "\\n", "<br>");
    }
    /**
     * ' 를 &apos; 로 변환
     * 
     * @param strString
     * @return String
     */
    public static String convert2APOS(String strString) {
        if (isBlank(strString)) {
            return "";
        }
        return replace(strString, "'", "&apos;");
    }
    /**
     * '&apos;'를 '''로 변환
     * 
     * @param strString
     *            변환할 문자열
     * @return 변화된 문자열
     */
    public static String reverse2APOS(String strString) {
        if (isBlank(strString)) {
            return "";
        }
        return replace(strString, "&apos;", "'");
    }
    /**
     * '"' 를 &quot; 로 변환
     * 
     * @param strString
     * @return String
     */
    public static String convert2QUOT(String strString) {
        if (isBlank(strString)) {
            return "";
        }
        return replace(strString, "\"", "&quot;");
    }
    /**
     * '&quot;'를 '\"'로 변환
     * 
     * @param strString
     *            변환할 문자열
     * @return 변화된 문자열
     */
    public static String reverse2QUOT(String strString) {
        if (isBlank(strString)) {
            return "";
        }
        return replace(strString, "&quot;", "\"");
    }
    /**
     * 스트링을 특정 문자를 기준으로 나누어 Vector형태로 반환한다.
     * 
     * @param strString
     *            : input string
     * @param strDelimeter
     *            : delimeter character
     * @return Vector : result string
     */
    public static Vector getSplitVector(String strString, String strDelimeter) {
        Vector vResult = new Vector();
        int nCount = 0, nLastIndex = 0;
        int indexValue = 0;
        try {
            nLastIndex = strString.indexOf(strDelimeter);
            if (nLastIndex == -1) {
                vResult.add(0, strString);
            } else {
                indexValue = strString.indexOf(strDelimeter);
                while (indexValue > -1) {
                    nLastIndex = strString.indexOf(strDelimeter);
                    vResult.add(nCount, strString.substring(0, nLastIndex));
                    strString = strString.substring(nLastIndex + strDelimeter.length(), strString.length());
                    nCount++;
                    indexValue = strString.indexOf(strDelimeter);
                }
                vResult.add(nCount, strString);
            }
        } catch (Exception e) {
            return null;
        }
        return vResult;
    }
    /**
     * String을 특정 문자를 기준으로 나누어 배열형태로 반환한다.
     * 
     * @param strString
     *            : input string
     * @param strDelimeter
     *            : delimeter character
     * @return String[]
     */
    public static String[] getSplitArray(String strString, String strDelimeter) {
        return (String[]) (getSplitVector(strString, strDelimeter).toArray(new String[0]));
    }
    /**
     * 입력 String에 있는 특정문자를 삭제해준다.
     * 
     * @param strString
     *            input String
     * @param strChar
     *            special character
     * @return String 특정문자를 제거한 문자
     */
    public static String deleteChar(String strString, char strChar) {
        if (isBlank(strString)) {
            return "";
        }
        strString = strString.trim();
        byte[] source = strString.getBytes();
        byte[] result = new byte[source.length];
        int j = 0;
        for (int i = 0; i < source.length; i++) {
            if (source[i] == (byte) strChar) {
                i++;
            }
            result[j++] = source[i];
        }
        return new String(result).trim();
    }
    /**
     * 바꾸고자 하는 스트링의 인덱스 모음을 구한다.
     * 
     * @param str
     *            String
     * @param word
     *            String
     * @return Vector tempindexArray
     */
    public static Vector getSelectedTextIndex(String str, String word) {
        int index = 0;
        int fromIndex = 0;
        Vector tempIndexArray = new Vector();
        do {
            index = str.indexOf(word, fromIndex);
            if (index != -1) {
                tempIndexArray.add(new Integer(index));
                fromIndex = index + word.length();
            }
        } while (index != -1);
        return tempIndexArray;
    }
    /**
     * 왼쪽(Left)에 문자열을 끼어 넣는다. width는 문자열의 전체 길이를 나타내며 chPad는 끼어 넣을 char
     * 
     * @param str
     *            적용할 문자열
     * @param width
     *            전체 문자열 크기
     * @param chPad
     *            pad 적용할 char
     * @return leftpad된 String
     */
    public static String setLeftPad(String str, int width, char chPad) {
        StringBuffer paddedValue = new StringBuffer();
        for (int i = str.length(); i < width; i++) {
            paddedValue.append(chPad);
        }
        paddedValue.append(str);
        String returnValue = paddedValue.toString();
        returnValue = returnValue.substring(0, width);
        return returnValue;
    }
    /**
     * 오른쪽(right)에 문자열을 끼어 넣는다. width는 문자열의 전체 길이를 나타내며, chPad는 끼어 넣을 char
     * 
     * @param str
     *            적용할 문자열
     * @param width
     *            전체 길이
     * @param chPad
     *            삽입할 char
     * @return String
     */
    public static String setRightPad(String str, int width, char chPad) {
        if (str.length() >= width) {
            return str.substring(0, width);
        }
        StringBuffer paddingValue = new StringBuffer();
        for (int i = str.length(); i < width; i++) {
            paddingValue.append(chPad);
        }
        return str + paddingValue.toString();
    }
    /**
     * 한글이 포함되어 있는지 여부를 확인하여, 한글 포함 갯수를 반환한다.
     * 
     * @param str
     *            한글 포함여부를 확인하고자 하는 String
     * @return 포함 한글 갯수
     */
    public static int checkHangul(String str) {
        int cnt = 0;
        if (isBlank(str)) {
            return 0;
        }
        int index = 0;
        while (index < str.length()) {
            if (str.charAt(index++) >= 256) {
                cnt++;
            }
        }
        return cnt;
    }
    /**
     * 한글이 포함되어 있는 String의 경우, byte 단위로 SubString을 행한다.
     * 
     * @param src
     *            한글이 포함되어 있는 String
     * @param beginIndex
     *            시작 byte index
     * @param endIndex
     *            끝 byte index
     * @return String beginIndex부터 endIndex까지의 String
     */
    public static String byteSubString(String src, int beginIndex, int endIndex) {
        if (StringUtil.isBlank(src)) {
            return "";
        }
        byte[] value = src.getBytes();
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }
        if (endIndex > value.length) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }
        if (beginIndex > endIndex) {
            throw new StringIndexOutOfBoundsException(endIndex - beginIndex);
        }
        byte[] tmpByte = new byte[endIndex - beginIndex];
        System.arraycopy(value, beginIndex, tmpByte, 0, tmpByte.length);
        return new String(tmpByte);
    }
    /**
     * sData 값이 sPattern으로 구성되었는지 체크한다.
     * 
     * @param sData
     *            확인하고자 하는 문자열
     * @param sPattern
     *            패턴 (예, 숫자 = "0123456789")
     * @return true/false
     */
    public static boolean isPattern(String sData, String sPattern) {
        for (int i = 0; i < sData.length(); i++) {
            if (sPattern.indexOf(sData.charAt(i)) < 0) {
                return false;
            }
        }
        return true;
    }
    public static byte[] hexFromString(String hex) {
        int len = hex.length();
        int bufLen = (len + 1) / 2;
        byte[] buf = new byte[bufLen];
        int i = 0, j = 0;
        i = len % 2;
        if (i == 1) {
            buf[j++] = (byte) fromDigit(hex.charAt(i++));
        }
        while (i < len) {
            buf[j++] = (byte) ((fromDigit(hex.charAt(i++)) << 4) | fromDigit(hex.charAt(i++)));
        }
        return buf;
    }
    public static int fromDigit(char ch) {
        if (ch >= '0' && ch <= '9') {
            return ch - '0';
        }
        if (ch >= 'A' && ch <= 'F') {
            return ch - 'A' + 10;
        }
        if (ch >= 'a' && ch <= 'f') {
            return ch - 'a' + 10;
        }
        throw new IllegalArgumentException("invalid hex digit '" + ch + "'");
    }
    /**
     * Reads the contents of the given URL and returns it as a string.
     * 
     * @param url
     * @return
     */
    public static String urlToString(URL url) throws IOException {
        StringBuffer sb = new StringBuffer("");
        InputStream is = url.openStream();
        int n = 0;
        do {
            n = is.read();
            if (n >= 0) {
                sb.append((char) n);
            }
        } while (n >= 0);
        is.close();
        return sb.toString();
    }
    public static String makeRandomDigit() {
        String rtn = null;
        try {
            char[] retChar = new char[5];
            for (int i = 0; i < retChar.length; i++) {
                double randomDigit = Math.random() * (double) 25.0 % 25.0;
                retChar[i] = (char) (randomDigit + 65);
                if ((char) retChar[i] < 'A' || (char) retChar[i] > 'Z') {
                    throw new Exception("Unexpected text : " + retChar[i]);
                }
            }
            rtn = new String(retChar);
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return "";
        }
        return rtn;
    }
    /**
     * 
     * @param authValue
     * @param param1
     * @param param2
     * @return
     * 
     * @author taeyong1.kim@dongkuk.com
     * 
     */
    public static HashMap<String, String> split(String authValue, String param1, String param2) {
        HashMap<String, String> hm = new HashMap<String, String>();
        String[] ary = authValue.split(param1);
        for (String s : ary) {
            String[] arytmp = s.split(param2);
            hm.put(arytmp[0], arytmp[1]);
        }
        return hm;
    }
    // /**
    // *
    // * @param value
    // * @return
    // * @author taeyong1.kim@dongkuk.com
    // *
    // */
    // public static String nvl(String value) {
    // String s = "";
    // try {
    // if (value != null && !"".equals(value) && value.length() > 0) {
    // s = value.trim();
    // }
    // } catch (Exception ex) {
    // // throw ex;
    // }
    // return s;
    // }
    /**
     * 
     * @param msg
     *            : javascript alert으로 띄워줄 메시지내용
     * @return
     * @author taeyong1.kim@dongkuk.com
     * 
     */
    public static String getJavascriptAlert(String msg) {
        return getJavascriptSrc("alert('" + msg + "');");
    }
    /**
     * 
     * @param javascriptSrc
     *            : javascript 소스를 넣고싶을 경우, 해당 소스
     * @return
     * @author taeyong1.kim@dongkuk.com
     */
    public static String getJavascriptSrc(String javascriptSrc) {
        StringBuilder sb = new StringBuilder("<script type='text/javascript'>");
        sb.append(javascriptSrc);
        sb.append("</script>");
        return sb.toString();
    }
    public static String getString(Object obj, String defaultStr) {
        return (obj == null ? defaultStr : obj.toString());
    }
    /**
     * OS의 경로구분자인 "/" or "\"----> URL에서 사용하는 경로구분자 "/" 으로 변환
     * 
     * @param str
     * @return
     * @author taeyong1.kim@dongkuk.com
     */
    public static String transUrlSeperator(String str) {
        return str.replaceAll(Matcher.quoteReplacement(File.separator), "/");
    }
    /**
     * URL에서 사용하는 경로구분자 "/" ----> OS의 경로구분자인 "/" or "\" 으로 변환
     * 
     * @param str
     * @return
     * @author taeyong1.kim@dongkuk.com
     */
    public static String transOsSeperator(String str) {
        return str.replaceAll("/", Matcher.quoteReplacement(File.separator));
    }
    /**
     * FIELD 값에 해당하는 두개의 값을 비교하여 두개의 값이 같으면 null return 아니면 field 사용
     * 
     * @param field
     * @param src
     * @param tgt
     * @return
     * @author taeyong1.kim@dongkuk.com
     */
    public static String compareStr(String field, String src, String tgt) {
        if (isEmpty(src) == isEmpty(tgt))
            return null;
        if (isNotEmpty(src) && isNotEmpty(tgt)) {
            return src.equals(tgt) ? null : field;
        } else {
            return tgt;
        }
    }
    /**
     * 
     * @param s
     * @return String
     */
    public static String trimDuplecateSpace(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (i < s.length() - 1) {
                if (c == ' ' && s.charAt(i + 1) == ' ') {
                    continue;
                }
            }
            sb.append(c);
        }
        return sb.toString().trim();
    }
    public static boolean isEmptyObj(Object object) {
        return isEmpty(nvlObj(object,""));
    }
    public static boolean isNotEmptyObj(Object object) {
        return isNotEmpty(nvlObj(object,""));
    }
    
    public static void main(String[] args) {
        Object val = "Test1"; 
        String rep_val = "Test2";
        logger.error(nvlObj(val, rep_val));
        
        val = ""; 
        rep_val = "Test3";
        logger.error(nvlObj(val, rep_val));
        
        val = null; 
        rep_val = "Test4";
        logger.error(nvlObj(val, rep_val));
        
        val = null; 
        rep_val = "";
        logger.error(nvlObj(val, rep_val));
        
        val = null; 
        rep_val = null;
        logger.error(nvlObj(val, rep_val));
    }
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
    public static boolean isNotEmpty(String str) {
        return !StringUtil.isEmpty(str);
    }
}
