package assessment.parkinglot.model;

import lombok.Data;

import java.util.Map;

@Data
public class ParkingLot {
    private String id;
    private int capacity;
    private Map<String, ParkingSpot> parkingSpots;


}