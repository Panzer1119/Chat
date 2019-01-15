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
import de.codemakers.security.interfaces.Signer;
import de.codemakers.security.interfaces.Verifier;
import de.codemakers.security.util.EasyCryptUtil;

import javax.crypto.SecretKey;
import java.security.PrivateKey;
import java.security.PublicKey;

public class TrustedUser extends SecureUser implements Signer, Verifier {
    
    protected PublicKey publicKey = null;
    private PrivateKey privateKey = null;
    protected transient Verifier verifier = null;
    private transient Signer signer = null;
    
    public TrustedUser(String username) {
        super(username);
    }
    
    public TrustedUser(String username, SecretKey secretKey) {
        super(username, secretKey);
    }
    
    public TrustedUser(String username, PublicKey publicKey) {
        super(username);
        setPublicKey(publicKey);
    }
    
    public TrustedUser(String username, PublicKey publicKey, PrivateKey privateKey) {
        super(username);
        setPublicKey(publicKey);
        setPrivateKey(privateKey);
    }
    
    public TrustedUser(String username, SecretKey secretKey, PublicKey publicKey) {
        super(username, secretKey);
        setPublicKey(publicKey);
    }
    
    public TrustedUser(String username, SecretKey secretKey, PublicKey publicKey, PrivateKey privateKey) {
        super(username, secretKey);
        setPublicKey(publicKey);
        setPrivateKey(privateKey);
    }
    
    public PublicKey getPublicKey() {
        return publicKey;
    }
    
    public TrustedUser setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
        if (publicKey != null) {
            try {
                verifier = EasyCryptUtil.verifierOfSHA256withRSA(publicKey);
            } catch (Exception ex) {
                verifier = null;
                Logger.handleError(ex);
            }
        } else {
            verifier = null;
        }
        return this;
    }
    
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
    
    public TrustedUser setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        if (privateKey != null) {
            try {
                signer = EasyCryptUtil.signerOfSHA256withRSA(privateKey);
            } catch (Exception ex) {
                signer = null;
                Logger.handleError(ex);
            }
        } else {
            signer = null;
        }
        return this;
    }
    
    @Override
    public boolean verify(byte[] data, byte[] data_signature) throws Exception {
        if (verifier == null) {
            return false;
        }
        return verifier.verify(data, data_signature);
    }
    
    @Override
    public byte[] sign(byte[] data) throws Exception {
        if (signer == null) {
            return null;
        }
        return signer.sign(data);
    }
    
    @Override
    public String toString() {
        return "TrustedUser{" + "publicKey=" + publicKey + ", verifier=" + verifier + ", aes_mode='" + aes_mode + '\'' + ", timestamp=" + timestamp + ", username='" + username + '\'' + '}';
    }
    
}
