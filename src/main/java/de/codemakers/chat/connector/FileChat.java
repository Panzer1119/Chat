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

import de.codemakers.base.exceptions.CJPException;
import de.codemakers.base.exceptions.NotImplementedRuntimeException;
import de.codemakers.base.logger.Logger;
import de.codemakers.chat.Main;
import de.codemakers.chat.entities.TextMessage;
import de.codemakers.chat.entities.User;
import de.codemakers.chat.gui.ChatTab;
import de.codemakers.io.file.AdvancedFile;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class FileChat<U extends User> extends Chat<U, TextMessage<U>, Object, Object> {
    
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"); //TODO Temp only
    
    protected final AdvancedFile advancedFile;
    protected boolean started = false;
    protected BufferedWriter bufferedWriter = null;
    protected Timer timer = null;
    
    public FileChat(ChatTab chatTab, U selfUser, AdvancedFile advancedFile) {
        super(chatTab, selfUser);
        Objects.requireNonNull(advancedFile);
        this.advancedFile = advancedFile;
        Main.EXIT_HOOKS.add(() -> {
            stop();
            close();
        }); //FIXME Only for testing?
    }
    
    public boolean isStarted() {
        return started;
    }
    
    public AdvancedFile getAdvancedFile() {
        return advancedFile;
    }
    
    protected void update() throws Exception {
        final String text_old = chatTab.getEditorPane().getText();
        final String text_new = new String(advancedFile.readBytes());
        if (!Objects.equals(text_old, text_new)) {
            chatTab.getEditorPane().setText(text_new);
            scrollEditorPaneToBottom();
        }
    }
    
    protected byte[] preSend(byte[] data) throws Exception {
        return data;
    }
    
    @Override
    public boolean send(Object message, Object... arguments) throws Exception {
        if (!started) {
            throw new CJPException("Not started");
        }
        Instant instant = Instant.now();
        if (arguments.length >= 1 && arguments[0] instanceof Instant) {
            instant = (Instant) arguments[0];
        }
        final String temp = String.format("[%s] %s: %s", LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DATE_TIME_FORMATTER), getSelfUser().toDisplayString(), message);
        bufferedWriter.write(new String(preSend(temp.getBytes())));
        bufferedWriter.newLine();
        bufferedWriter.flush();
        return true;
    }
    
    @Override
    public boolean onMessage(Object message, Object... arguments) throws Exception {
        throw new NotImplementedRuntimeException();
    }
    
    @Override
    public boolean start() throws Exception {
        if (started) {
            return false;
        }
        if (!advancedFile.exists()) {
            if (!advancedFile.createNewFile()) {
                throw new FileNotFoundException(advancedFile.getAbsolutePath() + " could not be created");
            }
        }
        bufferedWriter = advancedFile.createBufferedWriter(true); //TODO Maybe do not append?
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    update();
                } catch (Exception ex) {
                    Logger.handleError(ex);
                }
            }
        }, 0, 100);
        started = true;
        return bufferedWriter != null;
    }
    
    @Override
    public boolean stop() throws Exception {
        if (!started) {
            return false;
        }
        bufferedWriter = null;
        timer.cancel();
        timer = null;
        started = false;
        return bufferedWriter == null;
    }
    
    @Override
    public void close() throws IOException {
        if (bufferedWriter != null) {
            bufferedWriter.close();
        }
    }
    
    @Override
    public String toString() {
        return "FileChat{" + "advancedFile=" + advancedFile + ", started=" + started + ", bufferedWriter=" + bufferedWriter + ", timer=" + timer + ", chatTab=" + chatTab + ", selfUser=" + selfUser + ", users=" + users + '}';
    }
    
}
