package com.iflytek.component;

import com.iflytek.dao.HBaseDao;
import com.iflytek.entity.req.RequestContent;
import com.iflytek.exception.SecurityVerificationException;
import com.iflytek.exception.RateLimiterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 参数验证组件,使用方法级参数验证
 *
 * @author jjliu15@iflytek.com
 * @date 2018/1/23
 */
@Slf4j
@Validated
@Component
public class ParamValid {

    /**
     * 加密向量
     */
    private static final String IVSTR = "7201084316056726";

    @Autowired
    private HBaseDao hBaseDao;

    /**
     * 校验安全参数
     *
     * @param content
     * @throws SecurityException
     */
    public void validArgs(@Valid RequestContent content) {

        String appId = content.getApp_id();
        //aes的秘钥
        String appKey = "iflyrecomment453";//HBase.getAppKey(appid);
        //appId+ request_time
        String src = appId.concat(content.getRequest_time());
        String tar = encrypt(src, appKey);
        if (!tar.equals(content.getSecure_msg())) {
            throw new SecurityVerificationException("SECURITY CHECK FAILED");
        }

    }

    /**
     * 加密方法
     * 使用AES-128-CBC加密模式，key需要为16位
     *
     * @param sSrc
     * @param key
     * @return
     */
    private String encrypt(String sSrc, String key) {
        try {
            byte[] raw = key.getBytes();
            SecretKeySpec keySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(IVSTR.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes());
            return encodeBytes(encrypted);
        } catch (Exception ex) {
            return null;
        }

    }

    private String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }
        return strBuf.toString();
    }

}
