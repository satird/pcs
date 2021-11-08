package ru.satird.pcs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.satird.pcs.restcontrollers.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ContextControllerTest {

    @Autowired
    private AdRestController adRestController;
    @Autowired
    private AuthRestController authRestController;
    @Autowired
    private CommentRestController commentRestController;
    @Autowired
    private MessageRestController messageRestController;
    @Autowired
    private UserRestController userRestController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(adRestController).isNotNull();
        assertThat(authRestController).isNotNull();
        assertThat(commentRestController).isNotNull();
        assertThat(messageRestController).isNotNull();
        assertThat(userRestController).isNotNull();
    }
}
