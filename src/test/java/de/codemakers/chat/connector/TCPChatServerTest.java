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

import de.codemakers.base.logger.Logger;
import de.codemakers.chat.entities.NetMessage;
import de.codemakers.net.entities.NetCommand;
import de.codemakers.net.entities.NetObject;
import de.codemakers.net.wrapper.sockets.test2.AdvancedSocket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class TCPChatServerTest {
    
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS");
    public static final AtomicLong PING = new AtomicLong(Long.MIN_VALUE);
    public static final int PORT = 3453;
    
    static {
        Logger.DEFAULT_ADVANCED_LEVELED_LOGGER.setDateTimeFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS"));
    }
    
    public static final void main(String[] args) throws Exception {
        final ServerSocket serverSocket = new ServerSocket(PORT);
        Logger.log(String.format("[SERVER] serverSocket=%s", serverSocket));
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    serverSocket.close();
                    Thread.sleep(1000);
                    Logger.log("[SERVER] EXITING");
                    System.exit(0);
                } catch (Exception ex) {
                    Logger.handleError(ex);
                }
            }
        }, 20000);
        boolean shutdownRequested = false;
        Socket socket = null;
        outer:
        while ((socket = serverSocket.accept()) != null) {
            Logger.log(String.format("[SERVER] accepted Socket: %s", socket));
            final AdvancedSocket advancedSocket = new AdvancedSocket(socket);
            Logger.log(String.format("[SERVER] advancedSocket 1=%s", advancedSocket));
            advancedSocket.processOutputStream(ObjectOutputStream::new);
            Logger.log(String.format("[SERVER] advancedSocket 2=%s", advancedSocket));
            advancedSocket.processInputStream(ObjectInputStream::new);
            Logger.log(String.format("[SERVER] advancedSocket 3=%s", advancedSocket));
            Object input = null;
            while ((input = advancedSocket.getInputStream(ObjectInputStream.class).readObject()) != null) {
                final Instant instant = Instant.now();
                if (input == null) {
                    break;
                }
                final String timestamp_string = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()).format(DATE_TIME_FORMATTER);
                //System.out.println(String.format("[SERVER][%s] RAW: \"%s\"", timestamp_string, input));
                if (input instanceof NetObject) {
                    final NetObject netObject = (NetObject) input;
                    if (netObject instanceof NetCommand) {
                        final NetCommand netCommand = (NetCommand) input;
                        switch (netCommand.getCommand()) {
                            case PING:
                                //System.out.println(String.format("[SERVER][%s] got ping from CLIENT (%s)", timestamp_string, netCommand.getId(), netCommand.getObject()));
                                advancedSocket.getOutputStream(ObjectOutputStream.class).writeObject(new NetCommand(NetCommand.Command.PONG, instant.toEpochMilli()));
                                break;
                            case PONG:
                                //System.out.println(String.format("[SERVER][%s] got pong from CLIENT (%s)", timestamp_string, netCommand.getId(), netCommand.getObject()));
                                final long pong = instant.toEpochMilli();
                                final long duration = pong - PING.get();
                                //System.out.println(String.format("[SERVER][%s] time from ping to pong: %d ms", timestamp_string, duration));
                                Logger.log(String.format("[SERVER][%s] time from ping to pong: %d ms", timestamp_string, duration));
                                break;
                            case START:
                            case STOP:
                            case CONNECT:
                            case DISCONNECT:
                            case CUSTOM:
                            case UNKNOWN:
                                //System.out.println(String.format("[SERVER][%s][%d] %s: \"%s\"", timestamp_string, netCommand.getId(), NetCommand.class.getSimpleName(), netCommand));
                                Logger.log(String.format("[SERVER][%s][%d] %s: \"%s\"", timestamp_string, netCommand.getId(), netCommand.getClass().getSimpleName(), netCommand));
                                break;
                        }
                    } else if (netObject instanceof NetMessage) {
                        final NetMessage netMessage = (NetMessage) netObject;
                        Logger.log(String.format("[SERVER][%s][%d] echoing %s back to %s (%s): \"%s\"", timestamp_string, netMessage.getId(), netMessage.getClass().getSimpleName(), netMessage.getUsername(), netMessage.getSource(), netMessage));
                        advancedSocket.getOutputStream(ObjectOutputStream.class).writeObject(netMessage);
                    } else {
                        //System.out.println(String.format("[SERVER][%s][%d] %s: \"%s\"", timestamp_string, netObject.getId(), NetObject.class.getSimpleName(), netObject));
                        Logger.log(String.format("[SERVER][%s][%d] %s: \"%s\"", timestamp_string, netObject.getId(), netObject.getClass().getSimpleName(), netObject));
                    }
                } else if (input instanceof String) {
                    String string = (String) input;
                    if (string.equals("shutdown")) {
                        shutdownRequested = true;
                        break outer;
                    } else if (string.startsWith("echo")) {
                        string = string.substring("echo".length()).trim();
                        advancedSocket.getOutputStream(ObjectOutputStream.class).writeObject(string);
                    } else {
                        Logger.log(String.format("[SERVER][%s] input: \"%s\"", timestamp_string, input));
                    }
                }
            }
        }
        Logger.log(String.format("[SERVER] shutdownRequested=%b", shutdownRequested));
    }
    
}
