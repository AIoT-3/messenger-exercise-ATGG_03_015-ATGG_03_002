package com.nhnacademy.messenger.server.user.service.impl;

import com.nhnacademy.messenger.common.exception.MessengerException;
import com.nhnacademy.messenger.common.message.data.error.ErrorCode;
import com.nhnacademy.messenger.server.user.domain.User;
import com.nhnacademy.messenger.server.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("회원가입: 신규 가입 성공 확인")
    void registerUser_Success() {
        // Given
        User newUser = new User("newbie", "뉴비", "pass");
        given(userRepository.existsById("newbie")).willReturn(false);
        given(userRepository.save(any(User.class))).willReturn(newUser);

        // When
        User result = userService.registerUser(newUser);

        // Then
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals("newbie", result.getUserId())
        );
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("회원가입: 중복 ID 가입 실패 확인")
    void registerUser_Fail_AlreadyExists() {
        // Given
        User newUser = new User("duplicate", "중복", "pass");
        given(userRepository.existsById("duplicate")).willReturn(true);

        // When & Then
        MessengerException e = assertThrows(MessengerException.class, () -> 
            userService.registerUser(newUser)
        );
        assertEquals(ErrorCode.USER_ALREADY_EXISTS, e.getErrorCode());
    }

    @Test
    @DisplayName("조회: ID 기반 사용자 조회 성공 확인")
    void getUserById_Success() {
        // Given
        User user = new User("test", "테스트", "pass");
        given(userRepository.findById("test")).willReturn(Optional.of(user));

        // When
        User result = userService.getUserById("test");

        // Then
        assertEquals(user, result);
    }

    @Test
    @DisplayName("조회: 미존재 ID 조회 실패 확인")
    void getUserById_Fail_NotFound() {
        // Given
        given(userRepository.findById("unknown")).willReturn(Optional.empty());

        // When & Then
        MessengerException e = assertThrows(MessengerException.class, () ->
            userService.getUserById("unknown")
        );
        assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("로그인: 인증 성공 확인")
    void doLogin_Success() {
        // Given
        User user = new User("marco", "마르코", "nhnacademy123");
        given(userRepository.findById("marco")).willReturn(Optional.of(user));

        // When
        User result = userService.doLogin("marco", "nhnacademy123");

        // Then
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals("marco", result.getUserId())
        );
    }

    @Test
    @DisplayName("로그인: 미존재 ID 인증 실패 확인")
    void doLogin_Fail_IdNotFound() {
        // Given
        given(userRepository.findById("unknown")).willReturn(Optional.empty());

        // When & Then
        MessengerException e = assertThrows(MessengerException.class, () ->
            userService.doLogin("unknown", "pass")
        );
        assertEquals(ErrorCode.AUTH_INVALID_CREDENTIALS, e.getErrorCode());
    }

    @Test
    @DisplayName("로그인: 비밀번호 불일치 인증 실패 확인")
    void doLogin_Fail_PasswordMismatch() {
        // Given
        User user = new User("marco", "마르코", "nhnacademy123");
        given(userRepository.findById("marco")).willReturn(Optional.of(user));

        // When & Then
        MessengerException e = assertThrows(MessengerException.class, () ->
            userService.doLogin("marco", "wrong_password")
        );
        assertEquals(ErrorCode.AUTH_INVALID_CREDENTIALS, e.getErrorCode());
    }
}