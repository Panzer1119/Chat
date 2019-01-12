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

import javax.swing.*;

public class ChatTab {
    
    protected final Chat chat;
    protected int index;
    protected String name;
    protected final JScrollPane scrollPane = new JScrollPane();
    protected final JEditorPane editorPane = new JEditorPane();
    
    public ChatTab(Chat chat, int index, String name) {
        this.chat = chat;
        this.index = index;
        this.name = name;
    }
    
    public Chat getChat() {
        return chat;
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
        chat.renameChatTab(this, name);
        return this;
    }
    
    public JScrollPane getScrollPane() {
        return scrollPane;
    }
    
    public JEditorPane getEditorPane() {
        return editorPane;
    }
    
    @Override
    public String toString() {
        return "ChatTab{" + "chat=" + chat + ", index=" + index + ", name='" + name + '\'' + ", scrollPane=" + scrollPane + ", editorPane=" + editorPane + '}';
    }
    
}
