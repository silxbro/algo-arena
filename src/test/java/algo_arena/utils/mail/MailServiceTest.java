package algo_arena.utils.mail;

import static org.assertj.core.api.Assertions.assertThat;

import algo_arena.utils.mail.service.MailService;
import jakarta.mail.Address;
import jakarta.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Slf4j
class MailServiceTest {

    @Autowired
    MailService mailService;

    @Autowired
    JavaMailSender mailSender;

    @Test
    @SneakyThrows
    void createEmail() {
        //given
        String recipient = "tester@example.com";
        String title = "test email";
        String content = "This is a test email";

        //when
        MimeMessage email = mailService.createEmail(recipient, title, content);

        List<String> recipients = Arrays.stream(email.getAllRecipients()).map(Address::toString).toList();
        String emailSubject = email.getSubject();

        //then
        assertThat(recipients.size()).isEqualTo(1);
        assertThat(recipients).containsExactly(recipient);
        assertThat(emailSubject).isEqualTo(title);
    }
}