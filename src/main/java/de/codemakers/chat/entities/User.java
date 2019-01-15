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

import de.codemakers.base.util.interfaces.Snowflake;

import java.util.Objects;

public class User implements Snowflake {
    
    private final long id;
    protected String username;
    //protected final Map<Long, String> displayNames = new ConcurrentHashMap<>();
    
    public User(long id, String username) {
        this.id = id;
        this.username = username;
    }
    
    @Override
    public long getId() {
        return id;
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
    
    /*
    public Map<Long, String> getDisplayNames() {
        return displayNames;
    }
    
    public String addDisplayName(long id, String displayName) {
        return displayNames.put(id, displayName);
    }
    
    public String removeDisplayName(long id) {
        return displayNames.remove(id);
    }
    
    public String getDisplayName(long id) {
        return displayNames.get(id);
    }
    
    public User clearDisplayNames() {
        displayNames.clear();
        return this;
    }
    */
    
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        final User user = (User) object;
        return id == user.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + '}';
    }
    
}
