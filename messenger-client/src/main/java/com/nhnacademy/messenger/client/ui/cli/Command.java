package com.nhnacademy.messenger.client.ui.cli;

import java.util.List;

public record Command(
        String action,
        List<String> args
) {
    public boolean is(String expected) {
        return action.equalsIgnoreCase(expected);
    }
}
