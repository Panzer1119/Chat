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

import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;
import de.codemakers.chat.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;

public class ChatWindow {
    
    public static final Dimension STANDARD_SIZE = new Dimension(1000, 800);
    
    // GUI Start
    // JFrame
    protected final JFrame frame = new JFrame(Main.TITLE);
    // JMenuBar
    protected final JMenuBar menuBar = new JMenuBar();
    protected final JMenu menu_file = new JMenu(Main.LOCALIZER.localize("menu_file", "File"));
    protected final JMenuItem menuItem_file_exit = new JMenuItem(Main.LOCALIZER.localize("menuItem_file_exit", "Exit"));
    // OutputArea
    protected final JTabbedPane tabbedPane_output = new JTabbedPane();
    protected final List<ChatTab> chatTabs = new ArrayList<>();
    // InputArea
    protected final JPanel panel_input = new JPanel();
    protected final JTextField textField_input = new JTextField();
    protected final JButton button_send = new JButton(Main.LOCALIZER.localize("button_input_send", "Send"));
    // GUI End
    
    public ChatWindow() {
        initFrame();
    }
    
    protected void initFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(STANDARD_SIZE);
        frame.setLayout(new BorderLayout());
        initMenuBar();
        initFrameListeners();
        frame.add(tabbedPane_output, BorderLayout.CENTER);
        panel_input.setLayout(new BorderLayout());
        panel_input.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel_input.add(textField_input, BorderLayout.CENTER);
        panel_input.add(button_send, BorderLayout.EAST);
        frame.add(panel_input, BorderLayout.SOUTH);
    }
    
    private void initMenuBar() {
        menuItem_file_exit.addActionListener((event) -> exit());
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
    
    public void refreshOutputArea() {
        tabbedPane_output.invalidate();
        tabbedPane_output.repaint();
    }
    
    public void refreshInputArea() {
        panel_input.invalidate();
        panel_input.repaint();
    }
    
    public void exit() {
        if (Main.DEBUG) {
            Logger.log("Exiting program", LogLevel.FINE);
        }
        System.exit(0);
    }
    
    public void renameChatTab(ChatTab chatTab, String title) {
        tabbedPane_output.setTitleAt(chatTab.getIndex(), title);
        chatTab.name = title;
    }
    
    public ChatTab createChatTab(String name) {
        final ChatTab chatTab = new ChatTab(this, tabbedPane_output.getTabCount(), name);
        chatTabs.add(chatTab);
        tabbedPane_output.addTab(name, chatTab.getScrollPane());
        return chatTab;
    }
    
}