package pl.pszczolkowski.kanban.web.restapi.commonvalidation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public abstract class AbstractValidator extends LocalValidatorFactoryBean implements Validator {

	public abstract void customValidation(Object target, Errors errors);

	@Override
	public final void validate(Object target, Errors errors) {
		super.validate(target, errors);
		if (errors.hasErrors()) {
			return;
		}
		customValidation(target, errors);
	}

	@Override
	public final void validate(Object target, Errors errors, Object... validationHints) {
		super.validate(target, errors, validationHints);
		if (errors.hasErrors()) {
			return;
		}
		customValidation(target, errors);
	}
}
