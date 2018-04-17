package com.iflytek.util;import com.iflytek.entity.req.RequestContent;import com.iflytek.exception.exceptionhandle.SecurityVerificationException;import sun.misc.BASE64Encoder;import javax.crypto.Cipher;import javax.crypto.spec.IvParameterSpec;import javax.crypto.spec.SecretKeySpec;import javax.validation.Valid;import java.io.IOException;import java.io.UnsupportedEncodingException;import java.security.MessageDigest;import java.security.NoSuchAlgorithmException;/** * @author: demopoo * @Date: Created in 下午6:03 2018/4/16 * @Des: 验证签名 * @Modifyed By: */public class VerificationSignature {    /**     * 加密向量     */    private static final String IVSTR = "7201084316056726";    /**     * 校验安全参数     * @param content     * @throws IOException     */    public static void validArgs(@Valid RequestContent content) throws IOException {        String appid = content.getApp_id();        String appKey = "demopoosecurityk";//HBase.getAppKey(appid);        String src = appid.concat(content.getUids()).concat(content.getRequest_time());        String tar = encrypt(src,appKey);        if (!tar.equals(content.getSecure_msg())){            throw new SecurityVerificationException("SECURITY CHECK FAILED");        }    }    /**     * 加密方法     * 使用AES-128-CBC加密模式，key需要为16位     * @param sSrc     * @param key     * @return     */    public static String encrypt(String sSrc, String key){        try{            byte[] raw = key.getBytes();            SecretKeySpec skeySpec = new SecretKeySpec( raw, "AES" );            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");            IvParameterSpec iv = new IvParameterSpec( IVSTR.getBytes() );            cipher.init( Cipher.ENCRYPT_MODE, skeySpec, iv );            byte[] encrypted = cipher.doFinal( sSrc.getBytes() );            return encodeBytes( encrypted );        }catch(Exception ex){            return null;        }    }    private static String encodeBytes(byte[] bytes) {        StringBuffer strBuf = new StringBuffer();        for (int i = 0; i < bytes.length; i++) {            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));        }        return strBuf.toString();    }}