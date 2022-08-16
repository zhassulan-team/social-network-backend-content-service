package kata.academy.eurekacontentservice.api;

import lombok.Getter;

@Getter
public final class Error {

    private final String text;

    private Error(String text) {
        this.text = text;
    }

    public static Error of(String text) {
        return new Error(text);
    }
}
