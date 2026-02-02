package com.nhnacademy.messenger.client.ui.gui.panel;

import com.nhnacademy.messenger.client.config.AppConstant;
import com.nhnacademy.messenger.client.domain.user.controller.UserController;
import com.nhnacademy.messenger.client.domain.user.listener.LoginListener;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JFrame {
    private static final String TITLE_TEXT = "로그인";
    private static final String TITLE_LABEL_TEXT = "로그인";
    private static final String ID_LABEL_TEXT = "아이디";
    private static final String PW_LABEL_TEXT = "비밀번호";
    private static final String LOGIN_BUTTON_TEXT = "로그인";

    private static final float TITLE_FONT_SIZE = 15;
    private static final int LABEL_SIZE_WIDTH = 60;
    private static final int LABEL_SIZE_HEIGHT = 25;
    private static final int FIELD_COLUMNS = 15;
    private static final int SPACING_SMALL = 10;
    private static final int SPACING_MEDIUM = 15;
    private static final int PADDING = 40;

    private JTextField idField;
    private JPasswordField passwordField;
    private final UserController userController;

    public LoginPanel(UserController userController) {
        super(TITLE_TEXT);
        this.userController = userController;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel();
        root.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBackground(AppConstant.PRIMARY_COLOR);

        idField = new JTextField(FIELD_COLUMNS);
        passwordField = new JPasswordField(FIELD_COLUMNS);

        root.add(createTitleLabel());
        root.add(Box.createRigidArea(new Dimension(0, SPACING_MEDIUM)));
        root.add(createInputRow(ID_LABEL_TEXT, idField));
        root.add(Box.createRigidArea(new Dimension(0, SPACING_SMALL)));
        root.add(createInputRow(PW_LABEL_TEXT, passwordField));
        root.add(Box.createRigidArea(new Dimension(0, SPACING_MEDIUM)));
        root.add(createLoginButton());

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
    }

    private JLabel createTitleLabel() {
        JLabel title = new JLabel(TITLE_LABEL_TEXT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        title.setForeground(AppConstant.TEXT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        return title;
    }

    private JPanel createInputRow(String labelText, JComponent field) {
        JPanel row = new JPanel();
        row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));
        row.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setForeground(AppConstant.TEXT_COLOR);
        Dimension labelSize = new Dimension(LABEL_SIZE_WIDTH, LABEL_SIZE_HEIGHT);
        label.setPreferredSize(labelSize);
        label.setMinimumSize(labelSize);
        label.setMaximumSize(labelSize);

        row.add(label);
        row.add(Box.createRigidArea(new Dimension(SPACING_SMALL, 0)));
        row.add(field);
        row.setAlignmentX(Component.CENTER_ALIGNMENT);

        return row;
    }

    private JButton createLoginButton() {
        JButton loginButton = new JButton(LOGIN_BUTTON_TEXT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(new LoginListener(idField, passwordField, userController));
        getRootPane().setDefaultButton(loginButton);
        return loginButton;
    }
}

