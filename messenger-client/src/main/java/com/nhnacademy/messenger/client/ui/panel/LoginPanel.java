package com.nhnacademy.messenger.client.ui.panel;

import com.nhnacademy.messenger.client.config.ClientConstant;
import com.nhnacademy.messenger.client.ui.listener.LoginButtonActionListener;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JFrame {
    private static final float TITLE_FONT_SIZE = 15;
    private static final int LABEL_SIZE_WIDTH = 60;
    private static final int LABEL_SIZE_HEIGHT = 25;

    private JTextField idField;
    private JPasswordField passwordField;

    public LoginPanel() {
        super("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // 사이즈 변경 불가

        createUIComponent();
    }

    private void createUIComponent() {
        // ===== Root Panel (Y_AXIS) =====
        JPanel root = new JPanel();
        root.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));

        // Title
        JLabel title = new JLabel("로그인");
        title.setFont(title.getFont().deriveFont(Font.BOLD, TITLE_FONT_SIZE));
        title.setForeground(ClientConstant.TEXT_COLOR);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        root.add(title);
        root.add(Box.createRigidArea(new Dimension(0, 15)));

        // ===== ID Row (X_AXIS) =====
        JPanel idRow = new JPanel();
        idRow.setLayout(new BoxLayout(idRow, BoxLayout.X_AXIS));
        idRow.setOpaque(false);

        JLabel idLabel = new JLabel("아이디");
        idLabel.setForeground(ClientConstant.TEXT_COLOR);
        idField = new JTextField(15);

        Dimension idLabelSize = new Dimension(LABEL_SIZE_WIDTH, LABEL_SIZE_HEIGHT);
        idLabel.setPreferredSize(idLabelSize);
        idLabel.setMinimumSize(idLabelSize);
        idLabel.setMaximumSize(idLabelSize);

        idRow.add(idLabel);
        idRow.add(Box.createRigidArea(new Dimension(10, 0)));
        idRow.add(idField);
        idRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        root.add(idRow);
        root.add(Box.createRigidArea(new Dimension(0, 10)));

        // ===== PW Row (X_AXIS) =====
        JPanel pwRow = new JPanel();
        pwRow.setLayout(new BoxLayout(pwRow, BoxLayout.X_AXIS));
        pwRow.setOpaque(false);

        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setForeground(ClientConstant.TEXT_COLOR);
        passwordField = new JPasswordField(15);

        Dimension pwLabelSize = new Dimension(LABEL_SIZE_WIDTH, LABEL_SIZE_HEIGHT);
        pwLabel.setPreferredSize(pwLabelSize);
        pwLabel.setMinimumSize(pwLabelSize);
        pwLabel.setMaximumSize(pwLabelSize);

        pwRow.add(pwLabel);
        pwRow.add(Box.createRigidArea(new Dimension(10, 0)));
        pwRow.add(passwordField);
        pwRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        root.add(pwRow);
        root.add(Box.createRigidArea(new Dimension(0, 15)));

        // ===== Button Row =====
        JButton loginButton = new JButton("로그인");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // 로그인 버튼 이벤트
        loginButton.addActionListener(new LoginButtonActionListener(idField, passwordField));
        root.add(loginButton);
        // Optional: 엔터키로 로그인 버튼 동작시키기
        getRootPane().setDefaultButton(loginButton);

        root.setBackground(ClientConstant.PRIMARY_COLOR);
        setContentPane(root);
        pack();
        setLocationRelativeTo(null); // 화면 중앙
    }
}

