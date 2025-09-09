package com.quizapp;
import com.quizapp.ui.LoginFrame;
public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
