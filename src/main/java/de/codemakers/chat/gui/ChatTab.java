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

package de.codemakers.chat.gui;

import de.codemakers.base.util.interfaces.Startable;
import de.codemakers.base.util.interfaces.Stoppable;
import de.codemakers.chat.connector.Chat;

import javax.swing.*;
import java.io.Closeable;
import java.io.IOException;

public class ChatTab implements Closeable, Startable, Stoppable {
    
    protected final ChatWindow chatWindow;
    protected final JScrollPane scrollPane = new JScrollPane();
    protected final JEditorPane editorPane = new JEditorPane();
    protected int index;
    protected String name;
    protected Chat chat;
    
    public ChatTab(ChatWindow chatWindow, int index, String name) {
        this.chatWindow = chatWindow;
        this.index = index;
        this.name = name;
        init();
    }
    
    protected void init() {
        scrollPane.add(editorPane);
    }
    
    public ChatWindow getChatWindow() {
        return chatWindow;
    }
    
    public int getIndex() {
        return index;
    }
    
    public ChatTab setIndex(int index) {
        this.index = index;
        return this;
    }
    
    public String getName() {
        return name;
    }
    
    public ChatTab setName(String name) {
        chatWindow.renameChatTab(this, name);
        return this;
    }
    
    public JScrollPane getScrollPane() {
        return scrollPane;
    }
    
    public JEditorPane getEditorPane() {
        return editorPane;
    }
    
    public Chat getChat() {
        return chat;
    }
    
    public ChatTab setChat(Chat chat) {
        this.chat = chat;
        return this;
    }
    
    @Override
    public String toString() {
        return "ChatTab{" + "chatWindow=" + chatWindow + ", scrollPane=" + scrollPane + ", editorPane=" + editorPane + ", index=" + index + ", name='" + name + '\'' + ", chat=" + chat + '}';
    }
    
    @Override
    public boolean start() throws Exception {
        if (chat == null) {
            return false;
        }
        return chat.start();
    }
    
    @Override
    public boolean stop() throws Exception {
        if (chat == null) {
            return false;
        }
        return chat.stop();
    }
    
    @Override
    public void close() throws IOException {
        if (chat != null) {
            chat.close();
        }
    }
    
}
