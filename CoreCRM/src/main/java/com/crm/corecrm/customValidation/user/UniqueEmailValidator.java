package com.crm.corecrm.customValidation.user;

import com.crm.corecrm.repository.UsersRepo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UsersRepo repository;

    @Autowired
    public UniqueEmailValidator(UsersRepo repository) {
        this.repository = repository;
    }

    public UniqueEmailValidator(){
        repository = null;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if (repository == null || email == null || email.isEmpty()) {
            return true;
        }
        return !repository.existsByEmail(email);
    }

    public void initialize(UniqueEmail uniqueEmail) {
        ConstraintValidator.super.initialize(uniqueEmail);
    }
}
