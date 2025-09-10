package com.crm.corecrm.customValidation.user;

import jakarta.validation.Constraint;

@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {

}
