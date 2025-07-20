package com.stratumtech.realtyticket.validation.validator;

import com.stratumtech.realtyticket.validation.TelegramTag;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class TelegramTagValidator implements ConstraintValidator<TelegramTag, String> {

    /** pattern: @ + 5–32 [A-Za-z0-9_] */
    private static final Pattern PATTERN = Pattern.compile("^@[A-Za-z0-9_]{5,32}$");

    @Override
    public void initialize(TelegramTag constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        return PATTERN.matcher(value).matches();
    }
}

