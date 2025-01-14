package algo_arena.utils.auth.service;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class CodeGenerator {

    private static final String CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateAuthCode(int length) {

        StringBuilder authCode = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            authCode.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }

        return authCode.toString();
    }
}