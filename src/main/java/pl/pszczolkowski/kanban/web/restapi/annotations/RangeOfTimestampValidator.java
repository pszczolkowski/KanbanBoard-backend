package pl.pszczolkowski.kanban.web.restapi.annotations;

import java.time.LocalDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RangeOfTimestampValidator implements ConstraintValidator<RangeOfTimestamp, LocalDateTime> {

	private static final LocalDateTime MIN_SUPPORTED_VALUE = LocalDateTime.parse("1970-01-01T00:00:00");
	private static final LocalDateTime MAX_SUPPORTED_VALUE = LocalDateTime.parse("+292278994-08-17T07:12:55");

	@Override
	public void initialize(RangeOfTimestamp a) {}

	@Override
	public boolean isValid(LocalDateTime dateTime, ConstraintValidatorContext cvc) {
		if (dateTime == null) {
			return true;
		}

		return !(dateTime.isAfter(MAX_SUPPORTED_VALUE) || dateTime.isBefore(MIN_SUPPORTED_VALUE));
	}

}
