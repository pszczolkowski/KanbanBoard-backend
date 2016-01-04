package pl.pszczolkowski.kanban.shared.annotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TaskSizeValidator implements ConstraintValidator<TaskSize, Float> {

	private static final float[] TASK_SIZES = new float[] { 0.25F, 0.5F, 1, 2, 3, 4, 5 };
	
	@Override
	public void initialize(TaskSize a) {}

	@Override
	public boolean isValid(Float size, ConstraintValidatorContext cvc) {
		for (float taskSize : TASK_SIZES) {
			if (taskSize == size) {
				return true;
			}
		}
		
		return false;
	}

}
