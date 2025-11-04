package com.user.project.domain.usecase;

import com.user.project.domain.model.User;
import com.user.project.domain.model.UserProjects;
import com.user.project.domain.repository.UserProjectGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserProjectRetrievalUseCaseTest.TestConfig.class)
public class UserProjectRetrievalUseCaseTest {

    @Configuration
    static class TestConfig {
        @Bean
        public UserProjectGateway userProjectGateway() {
            return Mockito.mock(UserProjectGateway.class);
        }

        @Bean
        public UserProjectRetrievalUseCase userProjectRetrievalUseCase(UserProjectGateway userProjectGateway) {
            return new UserProjectRetrievalUseCase(userProjectGateway);
        }
    }

    @Autowired
    private UserProjectRetrievalUseCase useCase;

    @Autowired
    private UserProjectGateway userProjectGateway;

    @Test
    void shouldReturnPageOfUserProjects() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        User user1 = Mockito.mock(User.class);
        when(user1.getId()).thenReturn(1L);
        User user2 = Mockito.mock(User.class);
        when(user2.getId()).thenReturn(2L);

        UserProjects up1 = Mockito.mock(UserProjects.class);
        when(up1.getUser()).thenReturn(user1);
        UserProjects up2 = Mockito.mock(UserProjects.class);
        when(up2.getUser()).thenReturn(user2);

        List<UserProjects> content = Arrays.asList(up1, up2);
        Page<UserProjects> page = new PageImpl<>(content, pageable, content.size());

        when(userProjectGateway.findAllByUserId(userId, pageable)).thenReturn(page);

        Page<UserProjects> result = useCase.retrieveProjectsByUserId(userId, pageable);

        assertNotNull(result);
        assertEquals(content.size(), result.getContent().size(), "returned page size should match provided content size");

        for (int i = 0; i < content.size(); i++) {
            UserProjects expected = content.get(i);
            UserProjects actual = result.getContent().get(i);

            assertNotNull(actual, "item should not be null");
            assertNotNull(actual.getUser(), "item.user should not be null");

            Long expectedUserId = expected.getUser().getId();
            Long actualUserId = actual.getUser().getId();
            assertNotNull(expectedUserId);
            assertNotNull(actualUserId);
            assertEquals(expectedUserId, actualUserId, "user id should match the expected value");
        }

        verify(userProjectGateway).findAllByUserId(userId, pageable);
    }

}
