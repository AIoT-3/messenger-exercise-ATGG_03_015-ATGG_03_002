package com.nhnacademy.messenger.client.domain.user.listener;

import com.nhnacademy.messenger.client.domain.user.controller.UserController;
import lombok.AllArgsConstructor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@AllArgsConstructor
public class LoginListener implements ActionListener {
    private final JTextField idField;
    private final JPasswordField passwordField;
    private final UserController userController;

    @Override
    public void actionPerformed(ActionEvent e) {
        String userId = idField.getText();
        String password = passwordField.getText();
        // char[] password = passwordField.getPassword();

        if (userId.isBlank() || password.isBlank()) {
            return;
        }
        userController.login(userId, password);
    }
}
