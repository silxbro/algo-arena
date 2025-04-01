package algo_arena.utils.mail.service;

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
public class EmailService {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.properties.mail.smtp.address}")
    private String address;
    @Value("${spring.mail.properties.mail.smtp.personal}")
    private String personal;

    //이메일 전송
    public void sendEmail(String toEmail, String title, String content)
        throws MessagingException, UnsupportedEncodingException {
        MimeMessage email = createEmail(toEmail, title, content);

        emailSender.send(email);
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