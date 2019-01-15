/*
 *    Copyright 2019 Paul Hagedorn (Panzer1119)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.codemakers.chat.entities;

import de.codemakers.base.logger.Logger;
import de.codemakers.security.interfaces.Decryptor;
import de.codemakers.security.interfaces.Encryptor;
import de.codemakers.security.util.AESCryptUtil;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.util.Objects;

public class SecureUser extends User implements Encryptor, Decryptor {
    
    public static String AES_MODE_STANDARD = AESCryptUtil.MODE_AES_CBC_PKCS5Padding;
    
    //TODO Maybe just have an Encryptor and an Decryptor object, which are accessible with via this wrapper class? So the user can implement his own cryptography
    protected String aes_mode = AES_MODE_STANDARD;
    private SecretKey secretKey = null;
    private transient Cipher cipher_encrypt = null;
    private transient Cipher cipher_decrypt = null;
    
    public SecureUser(String username) {
        super(username);
    }
    
    public SecureUser(String username, SecretKey secretKey) {
        super(username);
        setSecretKey(secretKey);
    }
    
    public String getAESMode() {
        return aes_mode;
    }
    
    public SecureUser setAESMode(String aes_mode) {
        this.aes_mode = aes_mode;
        return this;
    }
    
    public SecretKey getSecretKey() {
        return secretKey;
    }
    
    public SecureUser setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
        resetCiphers();
        return this;
    }
    
    public boolean resetCiphers() {
        try {
            cipher_encrypt = Cipher.getInstance(aes_mode);
            cipher_decrypt = Cipher.getInstance(aes_mode);
            return true;
        } catch (Exception ex) {
            cipher_encrypt = null;
            cipher_decrypt = null;
            Logger.handleError(ex);
            return false;
        }
    }
    
    @Override
    public byte[] encrypt(byte[] data, byte[] iv) throws Exception {
        if (cipher_encrypt == null) {
            return null;
        }
        Objects.requireNonNull(iv);
        cipher_encrypt.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
        return cipher_encrypt.doFinal(data);
    }
    
    @Override
    public byte[] decrypt(byte[] data, byte[] iv) throws Exception {
        if (cipher_decrypt == null) {
            return null;
        }
        Objects.requireNonNull(iv);
        cipher_encrypt.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
        return cipher_decrypt.doFinal(data);
    }
    
    @Override
    public byte[] crypt(byte[] data, byte[] iv) throws Exception {
        throw new NotImplementedException();
    }
    
    @Override
    public byte[] crypt(byte[] data) throws Exception {
        return crypt(data, (byte[]) null);
    }
    
    @Override
    public String toString() {
        return "SecureUser{" + "aes_mode='" + aes_mode + '\'' + ", timestamp=" + timestamp + ", username='" + username + '\'' + '}';
    }
    
}
