package assessment.parkinglot.controller;

import assessment.parkinglot.controller.request.ParkingLotRequest;
import assessment.parkinglot.controller.response.RemainingSpotsResponse;
import assessment.parkinglot.enums.EParkingSpotType;
import assessment.parkinglot.exception.ParkingError;
import assessment.parkinglot.exception.ParkingException;
import assessment.parkinglot.model.ParkingSpot;
import assessment.parkinglot.model.Vehicle;
import assessment.parkinglot.service.ParkingLotService;
import assessment.parkinglot.service.ParkingSpotService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingLotControllerTest {

    @InjectMocks
    private ParkingLotController parkingLotController;

    @Mock
    private ParkingLotService parkingLotService;

    @Mock
    private ParkingSpotService parkingSpotService;

    @Test
    public void whenValidParkRequestThenReturnsSuccess() {
        ParkingLotRequest validRequest = createValidRequest();

        ResponseEntity<Map<String, Object>> response = parkingLotController.parkVehicle(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Vehicle parked successfully", response.getBody().get("message"));
    }

    @Test
    public void whenInvalidParkRequestThenReturnsBadRequest() {
        ParkingLotRequest invalidRequest = createInvalidRequest();

        ResponseEntity<Map<String, Object>> response = parkingLotController.parkVehicle(invalidRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Vehicle object should be provided", response.getBody().get("message"));
    }

    @Test
    public void whenLeaveParkingLotThenReturnsSuccess() {
        String licensePlate = "ABC123";
        ResponseEntity<Map<String, Object>> response = parkingLotController.leaveParkingLot(licensePlate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Vehicle left the parking lot successfully", response.getBody().get("message"));
    }

    @Test
    public void whenInvalidLeaveParkingLotThenReturnsBadRequest() {
        String invalidLicensePlate = "INVALID";
        doThrow(new ParkingException(ParkingError.INVALID_VEHICLE)).when(parkingLotService).leaveParkingLot(any());

        ResponseEntity<Map<String, Object>> response = parkingLotController.leaveParkingLot(invalidLicensePlate);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Vehicle not found or already left the parking spot", response.getBody().get("message"));
    }

    @Test
    public void whenGetRemainingSpotsThenReturnsSuccess() {
        RemainingSpotsResponse mockResponse = createMockRemainingSpotsResponse();
        when(parkingSpotService.getRemainingSpots(any())).thenReturn(mockResponse);

        ResponseEntity<?> response = parkingLotController.getRemainingSpots(Optional.empty());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }


    @Test
    public void whenValidateSpotIdsExistAndValidIdsThenMethodCalled() {
        ParkingLotRequest request = createValidRequest();
        doNothing().when(parkingSpotService).validateSpotIdsExist(request.getVehicle());

        parkingSpotService.validateSpotIdsExist(request.getVehicle());

        verify(parkingSpotService).validateSpotIdsExist(request.getVehicle());
    }

    private ParkingLotRequest createValidRequest() {
        ParkingLotRequest validRequest = new ParkingLotRequest();
        Vehicle vehicle = new Vehicle();
        ParkingSpot parkingSpot = new ParkingSpot();
        parkingSpot.setId("M1");
        vehicle.setLicensePlate("ABC123");
        vehicle.setType("MOTORCYCLE");
        vehicle.setParkingSpotList(List.of(parkingSpot));
        validRequest.setVehicle(vehicle);
        return validRequest;
    }

    private ParkingLotRequest createInvalidRequest() {
        return new ParkingLotRequest();
    }

    private RemainingSpotsResponse createMockRemainingSpotsResponse() {
        Map<EParkingSpotType, Integer> availableSpotCounts = Map.of(
                EParkingSpotType.COMPACT, 10,
                EParkingSpotType.MOTORCYCLE, 4,
                EParkingSpotType.REGULAR, 9
        );

        Map<EParkingSpotType, List<String>> availableSpots = Map.of(
                EParkingSpotType.COMPACT, List.of("C8", "C4", "C11", "C6", "C3", "C2", "C5", "C9", "C10", "C7"),
                EParkingSpotType.MOTORCYCLE, List.of("M3", "M5", "M2", "M4"),
                EParkingSpotType.REGULAR, List.of("R3", "R6", "R7", "R1", "R5", "R2", "R9", "R8", "R4")
        );

        return new RemainingSpotsResponse(availableSpotCounts, availableSpots);
    }
}
