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
import de.codemakers.chat.entities.Message;
import de.codemakers.chat.entities.User;
import de.codemakers.chat.gui.ChatTab;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Chat<U extends User, M extends Message<U>, MS, MR> implements Closeable, Startable, Stoppable {
    
    protected final ChatTab chatTab;
    protected final U selfUser;
    protected final List<U> users = new ArrayList<>();
    protected final List<M> messages = new ArrayList<>();
    
    public Chat(ChatTab chatTab, U selfUser) {
        Objects.requireNonNull(chatTab);
        Objects.requireNonNull(selfUser);
        this.chatTab = chatTab;
        this.selfUser = selfUser;
    }
    
    public ChatTab getChatTab() {
        return chatTab;
    }
    
    public U getSelfUser() {
        return selfUser;
    }
    
    public List<U> getUsers() {
        return users;
    }
    
    public List<M> getMessages() {
        return messages;
    }
    
    public abstract boolean send(MS message, Object... arguments) throws Exception;
    
    public abstract boolean onMessage(MR message, Object... arguments) throws Exception;
    
    protected void scrollEditorPaneToBottom() {
        chatTab.getEditorPane().setCaretPosition(chatTab.getEditorPane().getDocument().getLength());
    }
    
    @Override
    public String toString() {
        return "Chat{" + "chatTab=" + chatTab + ", selfUser=" + selfUser + ", users=" + users + ", messages=" + messages.size() + '}';
    }
    
}
