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

import de.codemakers.base.logger.Logger;
import de.codemakers.base.util.ArrayUtil;
import de.codemakers.chat.gui.Chat;

public class Main {
    
    public static final String NAME = "Chat";
    public static final String VERSION = "0.0";
    public static final String TITLE = NAME + " v" + VERSION;
    
    public static boolean DEBUG = false;
    
    public static final void main(String[] args) throws Exception {
        if (ArrayUtil.arrayContains(args, "debug")) {
            DEBUG = true;
            Logger.log("Program started");
        }
        final Chat chat = new Chat();
        chat.showFrame();
        //TODO
    }
    
}
