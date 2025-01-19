package com.sparta.onboarding.auth;

import static org.junit.jupiter.api.Assertions.*;

import com.sparta.onboarding.auth.model.User;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUsername() {
        // given
        String username = "testUser";
        User user = User.builder()
            .username(username)
            .password("1234")
            .nickname("닉네임")
            .build();

        userRepository.save(user);
        userRepository.flush();

        // when
        Optional<User> findUser = userRepository.findByUsername(username);

        // then
        assertTrue(findUser.isPresent());
        assertEquals(username, findUser.get().getUsername());
    }

    @Test
    void testFindByUsername_NotFound() {
        // given
        String username = "nonExistingUser";

        // when
        Optional<User> findUser = userRepository.findByUsername(username);

        // then
        assertFalse(findUser.isPresent());
    }
}