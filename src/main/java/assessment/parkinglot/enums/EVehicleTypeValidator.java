package assessment.parkinglot.enums;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EVehicleTypeValidator implements ConstraintValidator<ValidEVehicleType, String> {

    @Override
    public void initialize(ValidEVehicleType constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            EVehicleType.valueOf(value.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}