package algo_arena.submission.entity;

import java.util.Arrays;

public enum Language {
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

    Language(String name) {
        this.name = name;
    }

    public static Language fromName(String name) {
        return Arrays.stream(values())
            .filter(language -> language.name.equals(name))
            .findAny().orElse(null);
    }

    public String getName() {
        return name;
    }
}