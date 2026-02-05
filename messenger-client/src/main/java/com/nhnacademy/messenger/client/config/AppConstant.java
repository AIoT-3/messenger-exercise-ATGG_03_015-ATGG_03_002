package com.nhnacademy.messenger.client.config;

import lombok.experimental.UtilityClass;

import java.awt.*;

@UtilityClass
public class AppConstant {
    // UI 구성을 위한 색상 값
    public static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
    public static final Color PRIMARY_COLOR = Color.decode("#424549");
    public static final Color SECONDARY_COLOR = Color.decode("#36393e");
    public static final Color TEXT_COLOR = Color.WHITE;

    public static final int SPACING_SMALL = 10;
    public static final int SPACING_MEDIUM = 15;

    public static final Font FONT_TITLE = new Font("Dialog", Font.BOLD, 18);
    public static final Font FONT_SUBTITLE = new Font("Dialog", Font.BOLD, 14);
    public static final Font FONT_MESSAGE = new Font("Dialog", Font.PLAIN, 14);
}
