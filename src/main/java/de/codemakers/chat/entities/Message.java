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
import de.codemakers.base.util.interfaces.Snowflake;

import java.time.Instant;

public abstract class Message<U extends User> implements Snowflake {
    
    protected final long id;
    protected final U user;
    
    public Message(Instant instant, U user) {
        this.id = IDTimeUtil.createId(instant.toEpochMilli());
        this.user = user;
    }
    
    @Override
    public long getId() {
        return id;
    }
    
    public U getUser() {
        return user;
    }
    
    public abstract String toString();
    
}
