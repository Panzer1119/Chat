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

package de.codemakers.chat;

import de.codemakers.base.Standard;
import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;
import de.codemakers.chat.connector.FileChat;
import de.codemakers.chat.entities.User;
import de.codemakers.chat.gui.ChatTab;
import de.codemakers.io.file.AdvancedFile;

public class Test {
    
    public static final void main(String[] args) throws Exception {
        Main.main(args);
        Standard.async(() -> {
            Logger.log("Waiting to add ChatTab", LogLevel.FINER);
            Thread.sleep(2000);
            final ChatTab chatTab = Main.CHAT_WINDOW.createChatTab("Test 1");
            Logger.log("Created ChatTab", LogLevel.FINER);
            Standard.async(() -> {
                Logger.log("Waiting to rename ChatTab", LogLevel.FINER);
                Thread.sleep(2000);
                chatTab.setName("Test 2");
                Logger.log("Renamed ChatTab", LogLevel.FINER);
                final AdvancedFile advancedFile = new AdvancedFile("test/chat/test123.txt");
                advancedFile.getParentFile().mkdirs();
                final User user_panzer1119 = new User("Panzer1119_FileChat");
                final FileChat<User> fileChat = new FileChat<User>(chatTab, user_panzer1119, advancedFile);
                chatTab.setChat(fileChat);
                fileChat.start();
            });
        });
    }
    
}
