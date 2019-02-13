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
import de.codemakers.chat.entities.HTMLMessage;
import de.codemakers.chat.entities.Message;
import de.codemakers.chat.entities.SecureUser;
import de.codemakers.chat.entities.User;
import de.codemakers.chat.gui.ChatTab;
import de.codemakers.security.util.AESCryptUtil;
import de.codemakers.security.util.EasyCryptUtil;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SelfChat<U extends User> extends Chat<U, HTMLMessage<U>, Object, byte[]> {
    
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS"); //TODO Temp only
    
    public SelfChat(ChatTab chatTab, U selfUser) {
        super(chatTab, selfUser);
    }
    
    @Override
    public boolean send(Object message, Object... arguments) throws Exception {
        Instant instant = Instant.now();
        if (arguments.length >= 1 && arguments[0] instanceof Instant) {
            instant = (Instant) arguments[0];
        }
        //final String temp = String.format("[%s]: %s", LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DATE_TIME_FORMATTER), message);
        final String temp = "" + message;
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
        final HTMLMessage<U> htmlMessage = new HTMLMessage<>(Instant.now(), getSelfUser(), temp, DATE_TIME_FORMATTER);
        messages.add(htmlMessage);
        chatTab.getEditorPane().setText(messages.stream().map(Message::toString).collect(Collectors.joining("<br>")));
        scrollEditorPaneToBottom();
        return true;
    }
    
    @Override
    public boolean start() throws Exception {
        //TODO Test only START
        chatTab.getEditorPane().setContentType("text/html");
        chatTab.getEditorPane().setEditable(false);
        chatTab.getEditorPane().addHyperlinkListener((event) -> {
            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED && Desktop.isDesktopSupported()) {
                try {
                    final URI uri = event.getURL().toURI();
                    final int result = JOptionPane.showConfirmDialog(chatTab.getChatWindow().getFrame(), String.format("This link will open \"%s\" in your web browser. Do you want to continue?", event.getURL()), "External Link Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        Desktop.getDesktop().browse(uri);
                    }
                } catch (Exception ex) {
                    Logger.handleError(ex);
                }
            } else if (!Desktop.isDesktopSupported()) {
                Logger.log("Desktop is not supported!", LogLevel.WARNING);
            }
        });
        //HTMLEditorKit
        //TODO Test only END
        return true;
    }
    
    @Override
    public boolean stop() throws Exception {
        for (HyperlinkListener hyperlinkListener : chatTab.getEditorPane().getHyperlinkListeners()) {
            chatTab.getEditorPane().removeHyperlinkListener(hyperlinkListener);
        }
        chatTab.getEditorPane().setText("");
        return true;
    }
    
    @Override
    public void close() throws IOException {
        messages.clear();
    }
    
}
