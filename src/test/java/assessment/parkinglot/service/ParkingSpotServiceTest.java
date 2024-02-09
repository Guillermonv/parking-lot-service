package assessment.parkinglot.service;

import assessment.parkinglot.controller.response.RemainingSpotsResponse;
import assessment.parkinglot.enums.EParkingSpotType;
import assessment.parkinglot.model.ParkingSpot;
import assessment.parkinglot.model.repository.ParkingLotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class ParkingSpotServiceTest {

    @Mock
    private ParkingLotRepository parkingLotRepository;

    @InjectMocks
    private ParkingSpotService parkingSpotService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRemainingSpots() {

        ParkingSpot spot1 = new ParkingSpot("M1", EParkingSpotType.MOTORCYCLE, false, null);
        ParkingSpot spot2 = new ParkingSpot("C1", EParkingSpotType.COMPACT, false, null);
        ParkingSpot spot3 = new ParkingSpot("R1", EParkingSpotType.REGULAR, false, null);

        Map<String, ParkingSpot> allSpots = new HashMap<>();
        allSpots.put("M1", spot1);
        allSpots.put("C1", spot2);
        allSpots.put("R1", spot3);

        when(parkingLotRepository.getAllParkingSpots()).thenReturn(allSpots);

        RemainingSpotsResponse response = parkingSpotService.getRemainingSpots(Optional.empty());


        Map<EParkingSpotType, Integer> expectedSpotCounts = new HashMap<>();
        Map<EParkingSpotType, List<String>> expectedSpots = new HashMap<>();

        expectedSpotCounts.put(EParkingSpotType.MOTORCYCLE, 1);
        expectedSpotCounts.put(EParkingSpotType.COMPACT, 1);
        expectedSpotCounts.put(EParkingSpotType.REGULAR, 1);

        expectedSpots.put(EParkingSpotType.MOTORCYCLE, Collections.singletonList("M1"));
        expectedSpots.put(EParkingSpotType.COMPACT, Collections.singletonList("C1"));
        expectedSpots.put(EParkingSpotType.REGULAR, Collections.singletonList("R1"));

        assertEquals(expectedSpotCounts, response.getAvailableSpotCounts());
        assertEquals(expectedSpots, response.getAvailableSpots());
    }

    @Test
    public void testGetRemainingSpotsWithVehicleType() {

        ParkingSpot spot1 = new ParkingSpot("M1", EParkingSpotType.MOTORCYCLE, false, null);
        ParkingSpot spot2 = new ParkingSpot("C1", EParkingSpotType.COMPACT, false, null);
        ParkingSpot spot3 = new ParkingSpot("R1", EParkingSpotType.REGULAR, false, null);

        Map<String, ParkingSpot> allSpots = new HashMap<>();
        allSpots.put("M1", spot1);
        allSpots.put("C1", spot2);
        allSpots.put("R1", spot3);

        when(parkingLotRepository.getAllParkingSpots()).thenReturn(allSpots);

        RemainingSpotsResponse response = parkingSpotService.getRemainingSpots(Optional.of("MOTORCYCLE"));

        Map<EParkingSpotType, Integer> expectedSpotCounts = new HashMap<>();
        Map<EParkingSpotType, List<String>> expectedSpots = new HashMap<>();

        expectedSpotCounts.put(EParkingSpotType.MOTORCYCLE, 1);

        expectedSpots.put(EParkingSpotType.MOTORCYCLE, Collections.singletonList("M1"));

        assertEquals(expectedSpotCounts, response.getAvailableSpotCounts());
        assertEquals(expectedSpots, response.getAvailableSpots());
    }


}
