package algo_arena.utils.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.properties.mail.smtp.address}")
    private String address;
    @Value("${spring.mail.properties.mail.smtp.personal}")
    private String personal;

    //이메일 전송
    public void sendEmail(String toEmail, String title, String content)
        throws MessagingException, UnsupportedEncodingException {
        MimeMessage email = createEmail(toEmail, title, content);
        try {
            emailSender.send(email);
        } catch (RuntimeException e) {
            throw new RuntimeException("Unable to send email: " + toEmail);
        }
    }

    //이메일 생성
    public MimeMessage createEmail(String toEmail, String title, String content)
        throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(address, personal);
        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(content);

        return message;
    }
}