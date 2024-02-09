package assessment.parkinglot.model;

import assessment.parkinglot.enums.EParkingSpotType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpot implements Serializable {

    private String id;

    private EParkingSpotType type;

    private boolean occupied;

    private Vehicle occupyingVehicle;

}
