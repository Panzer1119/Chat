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

import de.codemakers.net.entities.NetEndpoint;
import de.codemakers.security.interfaces.Decryptor;
import de.codemakers.security.interfaces.Encryptor;
import de.codemakers.security.interfaces.Signer;
import de.codemakers.security.interfaces.Verifier;

import java.net.InetAddress;

public class NetUser extends TrustedUser {
    
    protected NetEndpoint netEndpoint;
    
    public NetUser(String username) {
        super(username);
    }
    
    public NetUser(String username, Signer signer, Verifier verifier) {
        super(username, signer, verifier);
    }
    
    public NetUser(String username, Encryptor encryptor, Decryptor decryptor) {
        super(username, encryptor, decryptor);
    }
    
    public NetUser(String username, Encryptor encryptor, Decryptor decryptor, Signer signer, Verifier verifier) {
        super(username, encryptor, decryptor, signer, verifier);
    }
    
    public NetUser(String username, NetEndpoint netEndpoint) {
        super(username);
        this.netEndpoint = netEndpoint;
    }
    
    public NetUser(String username, Signer signer, Verifier verifier, NetEndpoint netEndpoint) {
        super(username, signer, verifier);
        this.netEndpoint = netEndpoint;
    }
    
    public NetUser(String username, Encryptor encryptor, Decryptor decryptor, NetEndpoint netEndpoint) {
        super(username, encryptor, decryptor);
        this.netEndpoint = netEndpoint;
    }
    
    public NetUser(String username, Encryptor encryptor, Decryptor decryptor, Signer signer, Verifier verifier, NetEndpoint netEndpoint) {
        super(username, encryptor, decryptor, signer, verifier);
        this.netEndpoint = netEndpoint;
    }
    
    public NetEndpoint getNetEndpoint() {
        return netEndpoint;
    }
    
    public NetUser setNetEndpoint(NetEndpoint netEndpoint) {
        this.netEndpoint = netEndpoint;
        return this;
    }
    
    public InetAddress getInetAddress() {
        if (netEndpoint == null) {
            return null;
        }
        return netEndpoint.getInetAddress();
    }
    
    public int getPort() {
        if (netEndpoint == null) {
            return -1;
        }
        return netEndpoint.getPort();
    }
    
    @Override
    public String toString() {
        return "NetUser{" + "netEndpoint=" + netEndpoint + ", signer=" + signer + ", verifier=" + verifier + ", encryptor=" + encryptor + ", decryptor=" + decryptor + ", timestamp=" + timestamp + ", username='" + username + '\'' + '}';
    }
    
}
