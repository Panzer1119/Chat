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

import de.codemakers.base.exceptions.NotYetImplementedRuntimeException;
import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.tough.ToughRunnable;
import de.codemakers.chat.Main;
import de.codemakers.chat.entities.NetMessage;
import de.codemakers.chat.entities.NetUser;
import de.codemakers.chat.entities.TextMessage;
import de.codemakers.chat.gui.ChatTab;
import de.codemakers.net.entities.NetCommand;
import de.codemakers.net.entities.NetObject;
import de.codemakers.net.exceptions.NetRuntimeException;
import de.codemakers.net.wrapper.sockets.test2.ProcessingSocket;

import java.io.*;
import java.net.InetAddress;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class TCPChat extends Chat<NetUser, TextMessage<NetUser>, Object, NetMessage> {
    
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS"); //TODO Temp only
    
    protected final AtomicLong ping = new AtomicLong(Long.MIN_VALUE);
    protected final ProcessingSocket<ObjectInputStream, ObjectOutputStream> processingSocket;
    
    public TCPChat(ChatTab chatTab, NetUser selfUser, InetAddress inetAddress, int port) {
        super(chatTab, selfUser);
        Objects.requireNonNull(inetAddress);
        Main.EXIT_HOOKS.add(() -> {
            stop();
            close();
        }); //FIXME Only for testing?
        processingSocket = new ProcessingSocket<ObjectInputStream, ObjectOutputStream>(inetAddress, port) {
            @Override
            protected ObjectOutputStream toInternOutputStream(OutputStream outputStream) throws Exception {
                return new ObjectOutputStream(outputStream);
            }
            
            @Override
            protected ObjectInputStream toInternInputStream(InputStream inputStream) throws Exception {
                return new ObjectInputStream(inputStream);
            }
            
            @Override
            protected ToughRunnable createInputProcessor(ObjectInputStream inputStream, ObjectOutputStream outputStream) {
                return () -> {
                    try {
                        while (!isLocalCloseRequested() && !isStopRequested()) {
                            final Object input = inputStream.readObject();
                            final Instant instant = Instant.now();
                            if (input == null) {
                                break;
                            }
                            final String timestamp_string = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DATE_TIME_FORMATTER);
                            //System.out.println(String.format("[CLIENT][%s] RAW: \"%s\"", timestamp_string, input));
                            try {
                                if (input instanceof NetObject) {
                                    final NetObject netObject = (NetObject) input;
                                    if (netObject instanceof NetCommand) {
                                        final NetCommand netCommand = (NetCommand) input;
                                        switch (netCommand.getCommand()) {
                                            case PING:
                                                //System.out.println(String.format("[CLIENT][%s] got ping from SERVER (%s)", timestamp_string, netCommand.getId(), netCommand.getObject()));
                                                outputStream.writeObject(new NetCommand(NetCommand.Command.PONG, instant.toEpochMilli()));
                                                break;
                                            case PONG:
                                                //System.out.println(String.format("[CLIENT][%s] got pong from SERVER (%s)", timestamp_string, netCommand.getId(), netCommand.getObject()));
                                                final long pong = instant.toEpochMilli();
                                                final long duration = pong - ping.get();
                                                //System.out.println(String.format("[CLIENT][%s] time from ping to pong: %d ms", timestamp_string, duration));
                                                Logger.log(String.format("[CLIENT][%s] time from ping to pong: %d ms", timestamp_string, duration), LogLevel.FINE);
                                                break;
                                            case START:
                                            case STOP:
                                            case CONNECT:
                                            case DISCONNECT:
                                            case CUSTOM:
                                            case UNKNOWN:
                                                //System.out.println(String.format("[CLIENT][%s][%d] %s: \"%s\"", timestamp_string, netCommand.getId(), NetCommand.class.getSimpleName(), netCommand));
                                                Logger.log(String.format("[CLIENT][%s][%d] %s: \"%s\"", timestamp_string, netCommand.getId(), netCommand.getClass().getSimpleName(), netCommand), LogLevel.FINER);
                                                break;
                                        }
                                    } else if (netObject instanceof NetMessage) {
                                        final NetMessage netMessage = (NetMessage) netObject;
                                        Logger.log(String.format("[CLIENT][%s][%d] %s from %s (%s): \"%s\"", timestamp_string, netMessage.getId(), netMessage.getClass().getSimpleName(), netMessage.getUsername(), netMessage.getSource(), netMessage.getContent()), LogLevel.FINE);
                                        onNetMessage(netMessage, instant);
                                    } else {
                                        //System.out.println(String.format("[CLIENT][%s][%d] %s: \"%s\"", timestamp_string, netObject.getId(), NetObject.class.getSimpleName(), netObject));
                                        Logger.log(String.format("[CLIENT][%s][%d] %s: \"%s\"", timestamp_string, netObject.getId(), netObject.getClass().getSimpleName(), netObject), LogLevel.FINER);
                                    }
                                } else {
                                    //System.out.println(String.format("[CLIENT][%s] input: \"%s\"", timestamp_string, input));
                                    Logger.log(String.format("[CLIENT][%s] input: \"%s\"", timestamp_string, input), LogLevel.FINER);
                                }
                            } catch (Exception ex) {
                                //System.err.println("[CLIENT] input error " + ex);
                                Logger.handleError(ex);
                            }
                        }
                    } catch (Exception ex) {
                        outputStream.close(); //TODO Good?
                        throw new NetRuntimeException(ex);
                    }
                    outputStream.close(); //TODO Good?
                };
            }
            
        };
    }
    
    private void onNetMessage(NetMessage netMessage, Instant instant) {
        final String temp = String.format("[%s] %s (%s): %s", ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DATE_TIME_FORMATTER), netMessage.getUsername(), netMessage.getSource(), netMessage.getContent());
        final String text_old = chatTab.getEditorPane().getText();
        chatTab.getEditorPane().setText((text_old.isEmpty() ? "" : text_old + "\n") + temp);
        scrollEditorPaneToBottom();
    }
    
    @Override
    public boolean send(Object message, Object... arguments) throws Exception {
        Instant instant = Instant.now();
        if (arguments.length >= 1 && arguments[0] instanceof Instant) {
            instant = (Instant) arguments[0];
        }
        if (processingSocket == null || !processingSocket.isConnected()) {
            return false;
        }
        //processingSocket.getOutputStream().writeObject(message); //message is most likely a String, but we want to send NetMessage type objects
        processingSocket.getOutputStream().writeObject(new NetMessage(getSelfUser().getNetEndpoint(), null, message, getSelfUser().toDisplayString(), instant));
        return true;
    }
    
    @Override
    public boolean onMessage(NetMessage message, Object... arguments) throws Exception {
        //TODO Implement
        throw new NotYetImplementedRuntimeException();
    }
    
    @Override
    public boolean start() throws Exception {
        if (!processingSocket.connect()) {
            return false;
        }
        //TODO Test only START
        chatTab.getEditorPane().setContentType("text/html");
        //HTMLEditorKit
        //TODO Test only END
        return processingSocket.start();
    }
    
    @Override
    public boolean stop() throws Exception {
        if (processingSocket != null && processingSocket.isConnected()) {
            processingSocket.getOutputStream().writeObject("shutdown");
        }
        if (!processingSocket.disconnect()) {
            return false;
        }
        return processingSocket.stop();
    }
    
    @Override
    public void close() throws IOException {
        processingSocket.close();
    }
    
    @Override
    public String toString() {
        return "TCPChat{" + "ping=" + ping + ", processingSocket=" + processingSocket + ", chatTab=" + chatTab + ", selfUser=" + selfUser + ", users=" + users + '}';
    }
    
}
