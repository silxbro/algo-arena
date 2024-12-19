package algo_arena.submission.entity;

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

    public String getName() {
        return name;
    }
}