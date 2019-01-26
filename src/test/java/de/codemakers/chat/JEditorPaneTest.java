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

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class JEditorPaneTest {
    
    public static final void main(String[] args) throws Exception {
        final JFrame frame = new JFrame(JEditorPaneTest.class.getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(600, 800));
        final JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditable(false);
        final JScrollPane scrollPane = new JScrollPane(editorPane);
        final JTextArea textArea = new JTextArea();
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                editorPane.setText(textArea.getText());
            }
    
            @Override
            public void keyPressed(KeyEvent e) {
                editorPane.setText(textArea.getText());
            }
    
            @Override
            public void keyReleased(KeyEvent e) {
                editorPane.setText(textArea.getText());
            }
        });
        JScrollPane scrollPane1 = new JScrollPane(textArea);
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setLeftComponent(scrollPane);
        splitPane.setRightComponent(scrollPane1);
        frame.add(splitPane, BorderLayout.CENTER);
        frame.pack();
        splitPane.setDividerLocation(0.5);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
}
