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

import de.codemakers.security.entities.TrustedSecureData;
import de.codemakers.security.interfaces.Decryptor;
import de.codemakers.security.interfaces.Encryptor;
import de.codemakers.security.interfaces.Signer;
import de.codemakers.security.interfaces.Verifier;

public class TrustedUser extends SecureUser implements Signer, Verifier {
    
    protected Signer signer = null;
    protected Verifier verifier = null;
    
    public TrustedUser(String username) {
        super(username);
    }
    
    public TrustedUser(String username, Signer signer, Verifier verifier) {
        super(username);
        this.signer = signer;
        this.verifier = verifier;
    }
    
    public TrustedUser(String username, Encryptor encryptor, Decryptor decryptor) {
        super(username, encryptor, decryptor);
    }
    
    public TrustedUser(String username, Encryptor encryptor, Decryptor decryptor, Signer signer, Verifier verifier) {
        super(username, encryptor, decryptor);
        this.signer = signer;
        this.verifier = verifier;
    }
    
    public Signer getSigner() {
        return signer;
    }
    
    public TrustedUser setSigner(Signer signer) {
        this.signer = signer;
        return this;
    }
    
    public Verifier getVerifier() {
        return verifier;
    }
    
    public TrustedUser setVerifier(Verifier verifier) {
        this.verifier = verifier;
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
    
    public TrustedSecureData toTrustedData(byte[] data) {
        if (signer == null) {
            return null;
        }
        return new TrustedSecureData(data, signer);
    }
    
    public TrustedSecureData toTrustedSecureData(byte[] data) {
        if (encryptor == null || signer == null) {
            return null;
        }
        return new TrustedSecureData(data, encryptor, signer);
    }
    
    public boolean isValid(TrustedSecureData trustedSecureData) {
        if (trustedSecureData == null || verifier == null) {
            return false;
        }
        return trustedSecureData.verifyWithoutException(verifier);
    }
    
    public byte[] fromTrustedDataWithoutValidation(TrustedSecureData trustedSecureData) {
        if (trustedSecureData == null) {
            return null;
        }
        return trustedSecureData.getData();
    }
    
    public byte[] fromTrustedData(TrustedSecureData trustedSecureData) {
        if (trustedSecureData == null || verifier == null) {
            return null;
        }
        if (!isValid(trustedSecureData)) {
            throw new SecurityException(TrustedSecureData.class.getSimpleName() + " could not be validated");
        }
        return trustedSecureData.getData();
    }
    
    public byte[] fromTrustedSecureDataWithoutValidation(TrustedSecureData trustedSecureData) {
        if (trustedSecureData == null || decryptor == null) {
            return null;
        }
        return trustedSecureData.decryptWithoutException(decryptor);
    }
    
    public byte[] fromTrustedSecureData(TrustedSecureData trustedSecureData) {
        if (trustedSecureData == null || verifier == null || decryptor == null) {
            return null;
        }
        if (!isValid(trustedSecureData)) {
            throw new SecurityException(TrustedSecureData.class.getSimpleName() + " could not be validated");
        }
        return trustedSecureData.decryptWithoutException(decryptor);
    }
    
    @Override
    public String toString() {
        return "TrustedUser{" + "signer=" + signer + ", verifier=" + verifier + ", encryptor=" + encryptor + ", decryptor=" + decryptor + ", timestamp=" + timestamp + ", username='" + username + '\'' + '}';
    }
    
}
