package com.dksys.biz.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.dksys.biz.util.StringUtil;


public class SecurityUtil {
    private static final Logger       logger                             = LoggerFactory.getLogger(SecurityUtil.class);
    private static String             DEFAULT_CHARSET                    = "UTF-8";
    private static String             EUC_KR_CHARSET                     = "euc-kr";
    
    public static Map<String, String> escapeMap                          = new HashMap<String, String>();
    static {
        escapeMap.put("&apos;", "'");
        escapeMap.put("&amp;", "&");
        escapeMap.put("&quot;", "\"");
        escapeMap.put("&lt;", "<");
        escapeMap.put("&gt;", ">");
        escapeMap.put("&nbsp;", " ");
    }
    public static String bytesToHex(byte[] b) {
        char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < b.length; j++) {
            buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
            buf.append(hexDigit[b[j] & 0x0f]);
        }
        return buf.toString();
    }
    /**
     * SSO 연동을 위한 Sha 암호화 key 생성
     *
     * @param value
     * @return
     * @throws Exception
     */
    public static String getSHA256nDate(String value) {
        try {
            return getSHA256(DateUtil.getThisDay(DateUtil.YYYYMMDDHHMM) + value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * SHA256 hash처리
     *
     * @param value
     * @return
     * @throws Exception
     */
    public static String getSHA256(String value) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedValue = value.getBytes();
        md.update(hashedValue);
        return bytesToHex(md.digest());
    }
    /**
     * URL 인코딩
     * 
     * @param plainText
     * @param charsetName
     * @return
     * @throws Exception
     */
    public static String encodeUrl(String plainText, String charsetName) throws Exception {
        return URLEncoder.encode(plainText, charsetName);
    }
    public static String encodeUrl(String plainText) throws Exception {
        return encodeUrl(plainText, DEFAULT_CHARSET);
    }
    /**
     * URL 디코딩
     * 
     * @param encodeText
     * @param charsetName
     * @return
     * @throws Exception
     */
    public static String decodeUrl(String encodeText, String charsetName) throws Exception {
        return URLDecoder.decode(encodeText, charsetName);
    }
    public static String decodeUrl(String encodeText) throws Exception {
        return decodeUrl(encodeText, DEFAULT_CHARSET);
    }
    /**
     * BASE64로 인코딩
     * 
     * @param plainText
     * @return
     */
    public static String encodeBase64(String plainText) {
        if (plainText != null)
            return new String(Base64.encodeBase64(plainText.getBytes()));
        else
            return null;
    }
    /**
     * BASE64 디코딩
     * 
     * @param encodedText
     * @return
     */
    public static String decodeBase64(String encodedText) {
        if (encodedText != null)
            return new String(Base64.decodeBase64(encodedText.getBytes()));
        else
            return null;
    }
    /**
     * SSO 연동 key에 따른 유효성 Check
     *
     * @param value
     * @return
     * @throws Exception
     */
    public static boolean isValidSHA256(String key, String value) throws Exception {
        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar cal = Calendar.getInstance();
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        for (int i = 0; i < 3; i++) {
            cal.setTime(date);
            cal.add(Calendar.MINUTE, i - 1);
            String dataString = sf.format(cal.getTime());
            String hashStringValue = dataString + value;
            byte[] valueHash = hashStringValue.getBytes();
            md.update(valueHash);
            String ssoValue = bytesToHex(md.digest());
            if (key != null && key.equals(ssoValue))
                return true;
        }
        return false;
    }
    /**
     * SSO시, Base64를 이용한 복호화
     * 예) 메신저에서 클릭시
     * 
     * @param credential
     * @return
     * @throws Exception
     */
    public static String[] getBase64DecrytIdPw(String credential) throws Exception {
        return SecurityUtil.decodeBase64(credential).split(":");
    }
    /**
     * SSO시, Base64를 이용한 암호화
     * 
     * @param id
     * @param pw
     * @return
     * @throws Exception
     */
    public static String getBase64EncrytIdPw(String id, String pw) throws Exception {
        return SecurityUtil.encodeBase64(id + ":" + pw);
    }
    public static String getBase64Encryt(String param) throws Exception {
        return SecurityUtil.encodeBase64(param);
    }
    public static String unescape(String str) {
        if (str == null) {
            return null;
        }
        return StringEscapeUtils.unescapeXml(str).replaceAll("&nbsp;", " ");
    }
    public static String escape(String str) {
        if (str == null) {
            return null;
        }
        return StringEscapeUtils.escapeXml(str);
    }

    public static String getEncodeDrm(String socialPerId) {
        try {
            return Encode(socialPerId, "drmportal");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 구 DRM 자동 로그인 처리
    public static String Encode(String oriSTR, String keyCode) throws UnsupportedEncodingException {
        String Number = "";
        int keyASC = 0;
        String encode = "";
        for (int i = 0; i < keyCode.length(); i++) {
            String tmp = String.valueOf(keyCode.charAt(i));
            byte[] encodeBytes = tmp.getBytes("ASCII");
            int rTmp = encodeBytes[0];
            keyASC = keyASC + rTmp;
        }
        for (int i = 0; i <= oriSTR.length() - 1; i++) {
            String tmp = String.valueOf(oriSTR.charAt(i));
            byte[] encodeBytes = tmp.getBytes("ASCII");
            int rTmp = encodeBytes[0] + keyASC;
            if (i == oriSTR.length() - 1) {
                Number += String.valueOf(rTmp);
            } else {
                Number += String.valueOf(rTmp) + "/";
            }
        }
        encode = Number;
        return encode;
    }
    public static int genRandoNumber(int length) {
        String numStr = "1";
        String plusNumStr = "1";
        for (int i = 0; i < length; i++) {
            numStr += "0";
            if (i != length - 1) {
                plusNumStr += "0";
            }
        }
        Random random = new Random();
        int result = random.nextInt(Integer.parseInt(numStr)) + Integer.parseInt(plusNumStr);
        if (result > Integer.parseInt(numStr)) {
            result = result - Integer.parseInt(plusNumStr);
        }
        return result;
    }
    public static void main(String[] args) throws Exception {
    	
    }
}
