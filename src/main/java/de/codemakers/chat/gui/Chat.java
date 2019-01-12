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

import de.codemakers.base.logger.Logger;
import de.codemakers.chat.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Chat {
    
    public static final Dimension STANDARD_SIZE = new Dimension(1000, 800);
    
    protected final JFrame frame = new JFrame(Main.TITLE);
    protected final JMenuBar menuBar = new JMenuBar();
    protected final JMenu menu_file = new JMenu("File");
    protected final JMenuItem menuItem_file_exit = new JMenuItem("Exit");
    
    public Chat() {
        initFrame();
    }
    
    protected void initFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(STANDARD_SIZE);
        frame.setLayout(new BorderLayout());
        initMenuBar();
        initFrameListeners();
    }
    
    private void initMenuBar() {
        menu_file.add(menuItem_file_exit);
        menuBar.add(menu_file);
        frame.setJMenuBar(menuBar);
    }
    
    private void initFrameListeners() {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }
            
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
            
            @Override
            public void windowClosed(WindowEvent e) {
            }
            
            @Override
            public void windowIconified(WindowEvent e) {
            }
            
            @Override
            public void windowDeiconified(WindowEvent e) {
            }
            
            @Override
            public void windowActivated(WindowEvent e) {
            }
            
            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }
    
    public void showFrame() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public void exit() {
        if (Main.DEBUG) {
            Logger.log("Exiting program");
        }
        System.exit(0);
    }
    
}
