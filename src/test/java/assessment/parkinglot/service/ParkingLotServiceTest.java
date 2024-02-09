package assessment.parkinglot.service;

import assessment.parkinglot.enums.EParkingSpotType;
import assessment.parkinglot.exception.ParkingException;
import assessment.parkinglot.model.ParkingSpot;
import assessment.parkinglot.model.Vehicle;
import assessment.parkinglot.model.repository.ParkingLotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingLotServiceTest {


    @Mock
    private ParkingLotRepository parkingLotRepository;

    @InjectMocks
    private ParkingSpotService parkingSpotService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testValidateSpotIdsExistWithValidSpots() {
        Vehicle vehicle = createValidVehicle();

        Map<String, ParkingSpot> allSpots = createValidParkingSpots();
        when(parkingLotRepository.getAllParkingSpots()).thenReturn(allSpots);

        parkingSpotService.validateSpotIdsExist(vehicle);
    }

    @Test
    public void testValidateSpotIdsExistWithInvalidSpot() {
        Vehicle vehicle = createVehicleWithInvalidSpot();

        Map<String, ParkingSpot> allSpots = new HashMap<>();
        when(parkingLotRepository.getAllParkingSpots()).thenReturn(allSpots);

        assertThrows(ParkingException.class, () -> parkingSpotService.validateSpotIdsExist(vehicle));
    }

    private Vehicle createVehicleWithInvalidSpot() {
        Vehicle vehicle = new Vehicle();
        ParkingSpot invalidSpot = new ParkingSpot("M1", EParkingSpotType.MOTORCYCLE, false, null);
        vehicle.setParkingSpotList(Collections.singletonList(invalidSpot));
        return vehicle;
    }

    private Vehicle createValidVehicle() {
        Vehicle vehicle = new Vehicle();
        ParkingSpot validSpot = new ParkingSpot("M2", EParkingSpotType.MOTORCYCLE, false, null);
        vehicle.setParkingSpotList(Collections.singletonList(validSpot));
        return vehicle;
    }

    private Map<String, ParkingSpot> createValidParkingSpots() {
        Map<String, ParkingSpot> parkingSpots = new HashMap<>();
        ParkingSpot validSpot = new ParkingSpot("M2", EParkingSpotType.MOTORCYCLE, false, null);
        parkingSpots.put("M2", validSpot);
        return parkingSpots;
    }
}