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

import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;
import de.codemakers.chat.entities.SecureUser;
import de.codemakers.chat.entities.User;
import de.codemakers.chat.gui.ChatTab;
import de.codemakers.security.util.AESCryptUtil;
import de.codemakers.security.util.EasyCryptUtil;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelfChat<U extends User> extends Chat<U, Object, byte[]> {
    
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS"); //TODO Temp only
    
    private final List<Object> messages = new ArrayList<>();
    
    public SelfChat(ChatTab chatTab, U selfUser) {
        super(chatTab, selfUser);
    }
    
    @Override
    public boolean send(Object message, Object... arguments) throws Exception {
        Instant instant = Instant.now();
        if (arguments.length >= 1 && arguments[0] instanceof Instant) {
            instant = (Instant) arguments[0];
        }
        final String temp = String.format("[%s]: %s", LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DATE_TIME_FORMATTER), message);
        if (selfUser instanceof SecureUser) {
            final SecureUser secureUser = (SecureUser) selfUser;
            final byte[] iv = AESCryptUtil.generateRandomIVAESCBC(EasyCryptUtil.getSecurestRandom());
            final byte[] temp_encrypted = secureUser.encrypt(temp.getBytes(), iv);
            final byte[] temp_encrypted_and_iv = new byte[temp_encrypted.length + iv.length];
            System.arraycopy(temp_encrypted, 0, temp_encrypted_and_iv, 0, temp_encrypted.length);
            System.arraycopy(iv, 0, temp_encrypted_and_iv, temp_encrypted.length, iv.length);
            return onMessage(temp_encrypted_and_iv);
        } else {
            return onMessage(temp.getBytes());
        }
    }
    
    @Override
    public boolean onMessage(byte[] message, Object... arguments) throws Exception {
        Logger.log(String.format("onMessage: %s %s", new String(message), Arrays.toString(message)), LogLevel.FINE); //TODO Debug only
        String temp;
        if (selfUser instanceof SecureUser) {
            final SecureUser secureUser = (SecureUser) selfUser;
            final byte[] iv = new byte[AESCryptUtil.IV_BYTES_CBC];
            final byte[] temp_encrypted = new byte[message.length - iv.length];
            System.arraycopy(message, 0, temp_encrypted, 0, temp_encrypted.length);
            System.arraycopy(message, temp_encrypted.length, iv, 0, iv.length);
            temp = new String(secureUser.decrypt(temp_encrypted, iv));
        } else {
            temp = new String(message);
        }
        if (chatTab.getEditorPane().getText().isEmpty()) {
            chatTab.getEditorPane().setText(temp);
        } else {
            chatTab.getEditorPane().setText(chatTab.getEditorPane().getText() + "\n" + temp);
        }
        scrollEditorPaneToBottom();
        return true;
    }
    
    @Override
    public boolean start() throws Exception {
        return true;
    }
    
    @Override
    public boolean stop() throws Exception {
        return true;
    }
    
    @Override
    public void close() throws IOException {
        messages.clear();
    }
    
}
