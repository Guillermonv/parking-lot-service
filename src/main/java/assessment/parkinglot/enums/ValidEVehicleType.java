package assessment.parkinglot.enums;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EVehicleTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEVehicleType {
    String message() default "Invalid vehicle type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
