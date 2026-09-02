package com.hamropatro.validation;


public interface Validate<T> {
    void validate(T request);

    void validateUpdate(T request);
}
