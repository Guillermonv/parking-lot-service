package assessment.parkinglot.controller.request;

import assessment.parkinglot.model.Vehicle;
import lombok.Data;

@Data
public class ParkingLotRequest {

    private Vehicle vehicle;
}
