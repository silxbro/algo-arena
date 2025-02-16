package algo_arena.submission.enums;

import java.util.Arrays;

public enum CodeLanguage {
    PYTHON("Python"),
    JAVA("Java"),
    C_PP("C++"),
    C_SHARP("C#"),
    C("C"),
    JAVA_SCRIPT("JavaScript"),
    RUBY("Ruby"),
    GO("Go"),
    KOTLIN("Kotlin"),
    SWIFT("Swift"),
    ;


    private final String name;

    CodeLanguage(String name) {
        this.name = name;
    }

    public static CodeLanguage fromName(String name) {
        return Arrays.stream(values())
            .filter(language -> language.name.equals(name))
            .findAny().orElse(null);
    }

    public String getName() {
        return name;
    }
}