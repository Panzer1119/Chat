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

package de.codemakers.chat.connector;

import de.codemakers.base.util.interfaces.Startable;
import de.codemakers.base.util.interfaces.Stoppable;
import de.codemakers.chat.gui.ChatTab;

import java.io.Closeable;

public abstract class Chat implements Closeable, Startable, Stoppable {
    
    protected final ChatTab chatTab;
    protected String username = null;
    
    public Chat(ChatTab chatTab) {
        this.chatTab = chatTab;
    }
    
    public ChatTab getChatTab() {
        return chatTab;
    }
    
    public String getUsername() {
        return username;
    }
    
    public Chat setUsername(String username) {
        this.username = username;
        return this;
    }
    
    public abstract boolean send(Object message, Object... arguments) throws Exception;
    
    protected void scrollEditorPaneToBottom() {
        chatTab.getEditorPane().setCaretPosition(chatTab.getEditorPane().getText().length());
    }
    
    @Override
    public String toString() {
        return "Chat{" + "chatTab=" + chatTab + ", username='" + username + '\'' + '}';
    }
    
}
