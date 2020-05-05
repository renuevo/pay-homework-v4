package com.github.renuevo.common;

import com.google.common.hash.Hashing;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * <pre>
 * @className : SecurityUtils
 * @author : Deokhwa.Kim
 * @since : 2020-05-02
 * </pre>
 */
@Component
public class SecurityUtils {

    @Getter
    private final String saltKey;
    private final String password;
    private final TextEncryptor textEncryptor;

    public SecurityUtils(@Value("${security.password}") String password) {
        this.password = password;
        this.saltKey = KeyGenerators.string().generateKey();
        this.textEncryptor = Encryptors.text(this.password, this.saltKey);      //PBKDF2 해싱
    }

    /**
     * <pre>
     *  @methodName : getEncode
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-04 오후 10:16
     *  @summary : PBKDF2 해싱 암호화
     *  @param : [text]
     *  @return : java.lang.String
     * </pre>
     */
    public String getEncode(String text) {
        return textEncryptor.encrypt(text);
    }

    /**
     * <pre>
     *  @methodName : getDecode
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-04 오후 10:17
     *  @summary : PBKDF2 해싱 복호화
     *  @param : [text]
     *  @return : java.lang.String
     * </pre>
     */
    public String getDecode(String text) {
        return textEncryptor.decrypt(text);
    }

    /**
     * <pre>
     *  @methodName : getDecode
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-04 오후 10:17
     *  @summary : PBKDF2 해싱 복호화 - Add salt
     *  @param : [text, salt]
     *  @return : java.lang.String
     * </pre>
     */
    public String getDecode(String text, String salt) {
        return Encryptors.text(this.password, salt).decrypt(text);
    }

    /**
     * <pre>
     *  @methodName : getIdentityHash
     *  @author : Deokhwa.Kim
     *  @since : 2020-05-04 오후 10:18
     *  @summary : 관리번호 생성 해싱키
     *  @param : [text]
     *  @return : java.lang.String
     * </pre>
     */
    public String getIdentityHash(String text){
        return Hashing.murmur3_32().hashString(text, StandardCharsets.UTF_8).toString();
    }

}
