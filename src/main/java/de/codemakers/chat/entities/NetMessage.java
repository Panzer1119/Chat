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

import de.codemakers.base.util.IDTimeUtil;
import de.codemakers.net.entities.NetObject;

import java.net.InetAddress;
import java.time.Instant;

public class NetMessage extends NetObject {
    
    protected final InetAddress source;
    protected final Object content;
    protected final String username;
    
    public NetMessage(InetAddress source, Object content, String username, Instant instant) {
        super(IDTimeUtil.createId(instant.toEpochMilli()));
        this.source = source;
        this.content = content;
        this.username = username;
    }
    
    public InetAddress getSource() {
        return source;
    }
    
    public Object getContent() {
        return content;
    }
    
    public String getUsername() {
        return username;
    }
    
    @Override
    public String toString() {
        return "NetMessage{" + "source=" + source + ", content=" + content + ", username='" + username + '\'' + ", id=" + id + '}';
    }
    
}
