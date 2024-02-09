package assessment.parkinglot.controller.response;

import assessment.parkinglot.enums.EParkingSpotType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class RemainingSpotsResponse {

    private Map<EParkingSpotType, Integer> availableSpotCounts;
    private Map<EParkingSpotType, List<String>> availableSpots;
}
