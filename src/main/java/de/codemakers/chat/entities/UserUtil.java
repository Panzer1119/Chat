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
import de.codemakers.security.util.AESCryptUtil;
import de.codemakers.security.util.EasyCryptUtil;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

public class UserUtil {
    
    public static <U extends SecureUser> U secureUserAESCBCPKCS5Padding(U secureUser, SecretKey secretKey) {
        secureUser.setEncryptor(AESCryptUtil.createEncryptorAESCBCPKCS5Padding(secretKey));
        secureUser.setDecryptor(AESCryptUtil.createDecryptorAESCBCPKCS5Padding(secretKey));
        return secureUser;
    }
    
    public static <U extends TrustedUser> U trustUserSHA256withRSA(U trustedUser, PublicKey publicKey, PrivateKey privateKey) {
        trustUserSHA256withRSA(trustedUser, publicKey);
        trustUserSHA256withRSA(trustedUser, privateKey);
        return trustedUser;
    }
    
    public static <U extends TrustedUser> U trustUserSHA256withRSA(U trustedUser, PublicKey publicKey) {
        try {
            trustedUser.setVerifier(EasyCryptUtil.verifierOfSHA256withRSA(publicKey));
        } catch (Exception ex) {
            Logger.handleError(ex);
        }
        return trustedUser;
    }
    
    public static <U extends TrustedUser> U trustUserSHA256withRSA(U trustedUser, PrivateKey privateKey) {
        try {
            trustedUser.setSigner(EasyCryptUtil.signerOfSHA256withRSA(privateKey));
        } catch (Exception ex) {
            Logger.handleError(ex);
        }
        return trustedUser;
    }
    
}
