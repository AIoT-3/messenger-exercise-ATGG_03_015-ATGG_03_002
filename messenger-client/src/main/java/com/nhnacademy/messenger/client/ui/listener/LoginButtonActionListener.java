package com.nhnacademy.messenger.client.ui.listener;

import com.nhnacademy.messenger.common.message.data.auth.LoginRequest;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@AllArgsConstructor
public class LoginButtonActionListener implements ActionListener {
    private JTextField idField;
    private JPasswordField passwordField;

    @Override
    public void actionPerformed(ActionEvent e) {
        String userId = idField.getText();
        String password = passwordField.getText();
        // char[] password = passwordField.getPassword();
        LoginRequest loginRequest = new LoginRequest(userId, password);

        // TODO: LoginRequest 전송

    }
}
