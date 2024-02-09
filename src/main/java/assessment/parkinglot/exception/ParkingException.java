package assessment.parkinglot.exception;

import org.springframework.http.HttpStatus;

public class ParkingException extends RuntimeException {

    private final ParkingError parkingLotError;

    public ParkingException(ParkingError parkingLotError) {
        super(parkingLotError.getMessage());
        this.parkingLotError = parkingLotError;
    }

    public int getCode() {
        return parkingLotError.getCode();
    }

    public String getMessage() {
        return parkingLotError.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return parkingLotError.getHttpStatus();
    }
}

