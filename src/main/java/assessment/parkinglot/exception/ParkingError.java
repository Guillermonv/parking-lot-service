package assessment.parkinglot.exception;

import org.springframework.http.HttpStatus;

public enum ParkingError {
    MISSING_VEHICLE(1001, "Vehicle object should be provided", HttpStatus.BAD_REQUEST),

    MISSING_LICENSE_PLATE(1002, "license plate is required", HttpStatus.BAD_REQUEST),

    MISSING_VEHICLE_TYPE(1003, "vehicle type is required", HttpStatus.BAD_REQUEST),
    MISSING_SPOT(1004, "spot for vehicle is required", HttpStatus.BAD_REQUEST),

    VEHICLE_ALREADY_PARKED(1005, "Vehicle is already parked", HttpStatus.BAD_REQUEST),
    SPOT_ALREADY_OCCUPIED(1006, "The spot is already used by other vehicle check available places on GET /parking/remaining", HttpStatus.BAD_REQUEST),
    INVALID_SPOT(1007, "one or more spot id do not exist, please check on  /parking/remaining", HttpStatus.BAD_REQUEST),

    INVALID_SPOT_TYPE(1008, "spot type do is invalid for the vehicle type", HttpStatus.BAD_REQUEST),
    INVALID_VEHICLE(1009, "Vehicle not found or already left the parking spot", HttpStatus.BAD_REQUEST),

    INVALID_VEHICLE_TYPE(1010, "Vehicle type not exist , valid are : MOTORBIKE , CAR , VAN", HttpStatus.BAD_REQUEST),

    INVALID_VAN_PARKING_SPOTS_AMOUNT(1011, "parking spots for a van should be 3 spots", HttpStatus.BAD_REQUEST),

    INVALID_VAN_PARKING_SPOTS_TYPE(1007, "parking spots for a van should be 3 REGULAR spots", HttpStatus.BAD_REQUEST),

    INVALID_CAR_OR_MOTORBIKE_PARKING_SPOTS(1008, "motorbikes and cars should take only one spot", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

    ParkingError(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
