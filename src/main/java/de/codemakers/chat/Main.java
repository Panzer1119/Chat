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

import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.ArrayUtil;
import de.codemakers.chat.gui.Chat;
import de.codemakers.io.file.AdvancedFile;
import de.codemakers.lang.Localizer;
import de.codemakers.lang.PropertiesLocalizer;

import java.time.format.DateTimeFormatter;

public class Main {
    
    public static final String NAME = "Chat";
    public static final String VERSION = "0.0";
    public static final String TITLE = NAME + " v" + VERSION;
    
    public static boolean DEBUG = false;
    
    public static Localizer LOCALIZER = new PropertiesLocalizer();
    public static final AdvancedFile LANGUAGE_FILE_EN = new AdvancedFile("intern:/de/codemakers/chat/lang/lang_EN.txt");
    public static final Chat CHAT;
    
    static {
        Logger.DEFAULT_ADVANCED_LEVELED_LOGGER.setDateTimeFormatter(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSS"));
        ((PropertiesLocalizer) LOCALIZER).loadFromFile(LANGUAGE_FILE_EN);
        CHAT = new Chat();
    }
    
    public static final void main(String[] args) throws Exception {
        if (ArrayUtil.arrayContains(args, "debug")) {
            DEBUG = true;
            AdvancedFile.DEBUG = true;
            AdvancedFile.DEBUG_TO_STRING = true;
            Logger.log("Program started", LogLevel.FINE);
        }
        if (DEBUG) {
            Logger.log("LOCALIZER=" + LOCALIZER, LogLevel.FINER);
        }
        CHAT.showFrame();
        //TODO
    }
    
}
