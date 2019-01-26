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
import de.codemakers.chat.entities.SecureUser;
import de.codemakers.chat.entities.UserUtil;
import de.codemakers.chat.gui.ChatTab;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.security.util.AESCryptUtil;
import de.codemakers.security.util.EasyCryptUtil;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;

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
                final SecureUser user_panzer1119 = new SecureUser("Panzer1119_FileChat");
                Logger.log(String.format("user_panzer1119 pre  secure=%s", user_panzer1119), LogLevel.FINE);
                final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(256, SecureRandom.getInstanceStrong());
                final SecretKey secretKey = keyGenerator.generateKey();
                Logger.log(String.format("secretKey=%s", secretKey), LogLevel.FINE);
                UserUtil.secureUserAESCBCPKCS5Padding(user_panzer1119, secretKey);
                Logger.log(String.format("user_panzer1119 post secure=%s", user_panzer1119), LogLevel.FINE);
                final byte[] iv = new byte[AESCryptUtil.IV_BYTES_CBC];
                final FileChat<SecureUser> fileChat = new FileChat<SecureUser>(chatTab, user_panzer1119, advancedFile) {
                    @Override
                    protected byte[] preSend(byte[] data) throws Exception {
                        EasyCryptUtil.generateRandomBytes(iv, EasyCryptUtil.getSecurestRandom());
                        final byte[] data_encrypted = selfUser.encrypt(data, iv);
                        final byte[] data_encrypted_and_iv = new byte[data_encrypted.length + iv.length];
                        System.arraycopy(data_encrypted, 0, data_encrypted_and_iv, 0, data_encrypted.length);
                        System.arraycopy(iv, 0, data_encrypted_and_iv, data_encrypted.length, iv.length);
                        return data_encrypted_and_iv;
                    }
                };
                chatTab.setChat(fileChat);
                fileChat.start();
            });
        });
    }
    
}
