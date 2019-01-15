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

import javax.crypto.SecretKey;
import java.net.InetAddress;
import java.security.PrivateKey;
import java.security.PublicKey;

public class NetUser extends TrustedUser {
    
    protected InetAddress inetAddress;
    protected int port;
    
    public NetUser(String username) {
        super(username);
    }
    
    public NetUser(String username, SecretKey secretKey) {
        super(username, secretKey);
    }
    
    public NetUser(String username, PublicKey publicKey) {
        super(username, publicKey);
    }
    
    public NetUser(String username, PublicKey publicKey, PrivateKey privateKey) {
        super(username, publicKey, privateKey);
    }
    
    public NetUser(String username, SecretKey secretKey, PublicKey publicKey) {
        super(username, secretKey, publicKey);
    }
    
    public NetUser(String username, SecretKey secretKey, PublicKey publicKey, PrivateKey privateKey) {
        super(username, secretKey, publicKey, privateKey);
    }
    
    public NetUser(String username, InetAddress inetAddress, int port) {
        super(username);
        this.inetAddress = inetAddress;
        this.port = port;
    }
    
    public NetUser(String username, SecretKey secretKey, InetAddress inetAddress, int port) {
        super(username, secretKey);
        this.inetAddress = inetAddress;
        this.port = port;
    }
    
    public NetUser(String username, PublicKey publicKey, InetAddress inetAddress, int port) {
        super(username, publicKey);
        this.inetAddress = inetAddress;
        this.port = port;
    }
    
    public NetUser(String username, PublicKey publicKey, PrivateKey privateKey, InetAddress inetAddress, int port) {
        super(username, publicKey, privateKey);
        this.inetAddress = inetAddress;
        this.port = port;
    }
    
    public NetUser(String username, SecretKey secretKey, PublicKey publicKey, InetAddress inetAddress, int port) {
        super(username, secretKey, publicKey);
        this.inetAddress = inetAddress;
        this.port = port;
    }
    
    public NetUser(String username, SecretKey secretKey, PublicKey publicKey, PrivateKey privateKey, InetAddress inetAddress, int port) {
        super(username, secretKey, publicKey, privateKey);
        this.inetAddress = inetAddress;
        this.port = port;
    }
    
    public InetAddress getInetAddress() {
        return inetAddress;
    }
    
    public NetUser setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
        return this;
    }
    
    public int getPort() {
        return port;
    }
    
    public NetUser setPort(int port) {
        this.port = port;
        return this;
    }
    
    @Override
    public String toString() {
        return "NetUser{" + "inetAddress=" + inetAddress + ", port=" + port + ", publicKey=" + publicKey + ", verifier=" + verifier + ", aes_mode='" + aes_mode + '\'' + ", timestamp=" + timestamp + ", username='" + username + '\'' + '}';
    }
    
}
