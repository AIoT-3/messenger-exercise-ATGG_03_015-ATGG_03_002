package com.nhnacademy.messenger.server.user.repository.impl;

import com.nhnacademy.messenger.server.user.domain.User;
import com.nhnacademy.messenger.server.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
    }

    @Test
    @DisplayName("초기화: 기본 계정 존재 확인")
    void init_DefaultUserExists() {
        Optional<User> marco = userRepository.findById("marco");
        
        assertAll(
                () -> assertTrue(marco.isPresent()),
                () -> assertEquals("마르코", marco.get().getUserName())
        );
    }

    @Test
    @DisplayName("저장: 신규 사용자 저장 및 조회 확인")
    void save_And_Find() {
        // Given
        User newUser = new User("alice", "앨리스", "1234");

        // When
        User savedUser = userRepository.save(newUser);
        Optional<User> foundUser = userRepository.findById("alice");

        // Then
        assertAll(
                () -> assertEquals(newUser, savedUser),
                () -> assertTrue(foundUser.isPresent()),
                () -> assertEquals("앨리스", foundUser.get().getUserName()),
                () -> assertEquals("1234", foundUser.get().getPassword())
        );
    }

    @Test
    @DisplayName("조회: 미존재 사용자 조회 시 빈 Optional 확인")
    void findById_NotFound() {
        Optional<User> user = userRepository.findById("unknown");
        assertTrue(user.isEmpty());
    }

    @Test
    @DisplayName("존재 확인: ID 존재 여부 확인 로직 검증")
    void existsById() {
        // Given
        userRepository.save(new User("check", "체크", "pass"));

        // When & Then
        assertAll(
                () -> assertTrue(userRepository.existsById("check")),
                () -> assertFalse(userRepository.existsById("ghost"))
        );
    }

    @Test
    @DisplayName("삭제: 사용자 삭제 및 조회 불가 확인")
    void deleteById() {
        // Given
        userRepository.save(new User("del", "제삭", "pass"));
        assertTrue(userRepository.existsById("del"));

        // When
        userRepository.deleteById("del");

        // Then
        assertAll(
                () -> assertFalse(userRepository.existsById("del")),
                () -> assertTrue(userRepository.findById("del").isEmpty())
        );
    }
}