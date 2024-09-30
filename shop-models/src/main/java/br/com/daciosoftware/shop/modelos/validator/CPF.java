package br.com.daciosoftware.shop.modelos.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = { CpfValidator.class })
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface CPF {
	
	  String message() default "CPF inválido";

	  Class<?>[] groups() default {};

	  Class<? extends Payload>[] payload() default {};
}
