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

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class HTMLMessage<U extends User> extends TextMessage<U> {
    
    protected DateTimeFormatter dateTimeFormatter;
    
    public HTMLMessage(Instant instant, U user, String content, DateTimeFormatter dateTimeFormatter) {
        super(instant, user, content);
        this.dateTimeFormatter = dateTimeFormatter;
    }
    
    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }
    
    public HTMLMessage<U> setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
        return this;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] <strong>%s</strong>: %s", toLocalDateTime().format(dateTimeFormatter), user.toDisplayString(), content);
    }
    
}
