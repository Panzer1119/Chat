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

import de.codemakers.security.interfaces.Decryptor;
import de.codemakers.security.interfaces.Encryptor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Objects;

public class SecureUser extends User implements Encryptor, Decryptor {
    
    protected transient Encryptor encryptor = null;
    protected transient Decryptor decryptor = null;
    
    public SecureUser(String username) {
        super(username);
    }
    
    public SecureUser(String username, Encryptor encryptor, Decryptor decryptor) {
        super(username);
        this.encryptor = encryptor;
        this.decryptor = decryptor;
    }
    
    public Encryptor getEncryptor() {
        return encryptor;
    }
    
    public SecureUser setEncryptor(Encryptor encryptor) {
        this.encryptor = encryptor;
        return this;
    }
    
    public Decryptor getDecryptor() {
        return decryptor;
    }
    
    public SecureUser setDecryptor(Decryptor decryptor) {
        this.decryptor = decryptor;
        return this;
    }
    
    @Override
    public byte[] encrypt(byte[] data, byte[] iv) throws Exception {
        if (encryptor == null) {
            return null;
        }
        Objects.requireNonNull(iv);
        return encryptor.encrypt(data, iv);
    }
    
    @Override
    public byte[] decrypt(byte[] data, byte[] iv) throws Exception {
        if (decryptor == null) {
            return null;
        }
        Objects.requireNonNull(iv);
        return decryptor.decrypt(data, iv);
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
        return "SecureUser{" + "encryptor=" + encryptor + ", decryptor=" + decryptor + ", timestamp=" + timestamp + ", username='" + username + '\'' + '}';
    }
    
}
