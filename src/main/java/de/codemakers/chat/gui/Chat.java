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

import de.codemakers.chat.Main;

import javax.swing.*;
import java.awt.*;

public class Chat {
    
    public static final Dimension STANDARD_SIZE = new Dimension(1000, 800);
    
    protected final JFrame frame = new JFrame(Main.TITLE);
    
    public Chat() {
        initFrame();
    }
    
    protected void initFrame() {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //TODO Implement WindowListener
        frame.setPreferredSize(STANDARD_SIZE);
        frame.setLayout(new BorderLayout());
    }
    
    public void showFrame() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
}
