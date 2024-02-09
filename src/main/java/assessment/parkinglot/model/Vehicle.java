package assessment.parkinglot.model;

import assessment.parkinglot.enums.ValidEVehicleType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Vehicle implements Serializable {

    private String licensePlate;

    @ValidEVehicleType
    private String type;

    @JsonProperty("spots")
    private List<ParkingSpot> parkingSpotList;

}