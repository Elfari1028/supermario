// package com.ui;

// import javax.swing.*;
// import java.awt.*;

// import com.util.Callback;
// import com.util.KeyListener;

// import java.awt.event.*;

// public class Window {
//     public static Window instance = new Window();
//     JFrame mainFrame;
//     GameFrame gameFrame;

//     private Window() {
//         mainFrame = new JFrame("Let's Play Mario");
//         mainFrame.setLayout(new FlowLayout());
//         mainFrame.setSize(800, 450);
//         mainFrame.setResizable(false);
//         mainFrame.setLocationRelativeTo(null);
//         mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//         JPanel panel = new JPanel(new FlowLayout());

//         panel.add(createButton("Level 1", "level1_map.txt",false));
//         panel.add(createButton("Level 2", "level2_map.txt",false));
//         panel.add(createButton("Level 3", "level3_map.txt", false));
//         panel.add(createButton("Tutorial", "tutorial_map.txt", false));
//         panel.add(createButton("Multiplayer", "map.txt", true));
//         mainFrame.add(panel);
//     }

//     private JButton createButton(String title, String mapName, boolean isMultiplayer) {
//         JButton button = new JButton(title);
//         button.addActionListener(new ActionListener() {
//             public void actionPerformed(ActionEvent event) {
//                 onButtonPressed(title, mapName,isMultiplayer);
//             }
//         });
//         return button;
//     }

//     private void onButtonPressed(String buttonTitle, String mapName,boolean isMultiplayer) {
//         if (gameFrame != null)
//             gameFrame.dispose();
//         try {
//             gameFrame = new GameFrame(mapName,isMultiplayer);
//         } catch (Exception e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
//         gameFrame.addKeyListener(new KeyListener(gameFrame, new Callback() {
//             public void exit() {
//                 onEsc();
//             }
//         }));
//         gameFrame.start();
//         mainFrame.setVisible(false);
//     }

//     public void run() {
//         mainFrame.setVisible(true);
//     }

//     public void onEsc() {
//         gameFrame.setVisible(false);
//         gameFrame.dispose();
//         gameFrame = null;
//         mainFrame.setVisible(true);
//     }
// }
