package mobi.eyeline.ips.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxSizeValidator implements ConstraintValidator<MaxSize, String> {

  private int maxSize;

  @Override
  public void initialize(MaxSize constraintAnnotation) {
    maxSize = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return (value == null) || (value.length() <= maxSize);
  }
}
