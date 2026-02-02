package com.nhnacademy.messenger.client.ui.cli;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandParser {

    public Command parse(String input) {

        // 0. 입력이 비어있는 경우 처리
        if (StringUtils.isBlank(input)) {
            return new Command("", Collections.emptyList());
        }

        // 1. 공백 기준으로 문자열 분리
        String[] parts = StringUtils.split(input);
        if (parts.length == 0) {
            return new Command("", Collections.emptyList());
        }

        // 2. 명령어 설정
        String action = parts[0];

        // 3. 인자 설정
        List<String> args = (parts.length > 1) 
                ? Arrays.asList(Arrays.copyOfRange(parts, 1, parts.length))
                : Collections.emptyList();

        return new Command(action, args);
    }
}
