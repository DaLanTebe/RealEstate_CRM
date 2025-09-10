package com.crm.corecrm.customValidation.user;

import com.crm.corecrm.repository.UsersRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueUsernameValidator implements ConstraintValidator<UniqueEmail, String> {


    private final UsersRepo repository;

    @Autowired
    public UniqueUsernameValidator(UsersRepo repository) {
        this.repository = repository;
    }

    public UniqueUsernameValidator() {
        repository = null;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        if (repository == null || username == null || username.isEmpty()) {
            return true;
        }
        return !repository.existsByUsername(username);
    }
}
