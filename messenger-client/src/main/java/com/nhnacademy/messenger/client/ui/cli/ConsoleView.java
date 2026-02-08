package com.nhnacademy.messenger.client.ui.cli;

import com.nhnacademy.messenger.client.ui.View;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.PrintStream;
import java.util.Scanner;

@Slf4j
public class ConsoleView implements View {

    private final Scanner scanner;
    private final PrintStream out;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
        this.out = System.out;
    }

    public void println(String message) {
        printWithPrompt(message);
    }

    private void printWithPrompt(String message) {
        out.print("\r" + message + "\n> ");
        out.flush();
    }

    @Override
    public void showSystemMessage(String message) {
        printWithPrompt("[시스템] " + message);
    }

    @Override
    public void showErrorMessage(String message) {
        printWithPrompt("[오류] " + message);
    }

    @Override
    public String readInput() {
        out.print("\r> ");
        out.flush();
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            return StringUtils.trim(line);
        }
        return "";
    }
}
