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

import de.codemakers.base.util.interfaces.Timestamp;

import java.util.Objects;

public class User implements Timestamp {
    
    protected final long timestamp;
    protected String username;
    
    public User(String username) {
        this.timestamp = System.currentTimeMillis();
        this.username = username;
    }
    
    @Override
    public long getTimestamp() {
        return timestamp;
    }
    
    public String getUsername() {
        return username;
    }
    
    public User setUsername(String username) {
        this.username = username;
        return this;
    }
    
    public String toDisplayString() {
        return username;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        final User user = (User) object;
        return Objects.equals(username, user.username);
    }
    
    @Override
    public int hashCode() {
        return username.hashCode();
    }
    
    @Override
    public String toString() {
        return "User{" + "timestamp=" + timestamp + ", username='" + username + '\'' + '}';
    }
    
}
