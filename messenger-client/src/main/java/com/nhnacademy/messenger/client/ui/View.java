package com.nhnacademy.messenger.client.ui;

public interface View {
    void showSystemMessage(String message);
    void showErrorMessage(String message);
    String readInput();
}
