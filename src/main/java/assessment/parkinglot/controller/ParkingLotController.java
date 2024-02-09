package assessment.parkinglot.controller;

import assessment.parkinglot.controller.request.ParkingLotRequest;
import assessment.parkinglot.controller.response.RemainingSpotsResponse;
import assessment.parkinglot.exception.ParkingException;
import assessment.parkinglot.service.ParkingLotService;
import assessment.parkinglot.service.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static assessment.parkinglot.service.util.ParkingUtil.validateRequest;

@Validated
@RestController
@RequestMapping("/parking")
public class ParkingLotController {

    private static final String CODE = "code";

    private static final String MSG = "message";

    @Autowired
    private ParkingLotService parkingLotService;

    @Autowired
    private ParkingSpotService parkingSpotService;


    @PostMapping("/park")
    public ResponseEntity<Map<String, Object>> parkVehicle(@Valid @RequestBody ParkingLotRequest request) {
        try {
            validateRequest(request);
            parkingLotService.parkVehicle(request.getVehicle());

            return ResponseEntity.ok(Map.of(MSG, "Vehicle parked successfully"));
        } catch (ParkingException e) {

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(CODE, e.getCode());
            errorResponse.put(MSG, e.getMessage());

            return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
        }
    }

    @DeleteMapping("/leave/{licensePlate}")
    public ResponseEntity<Map<String, Object>> leaveParkingLot(@PathVariable String licensePlate) {
        try {

            parkingLotService.leaveParkingLot(licensePlate);
            Map<String, Object> response = new HashMap<>();
            response.put(MSG, "Vehicle left the parking lot successfully");

            return ResponseEntity.ok(response);
        } catch (ParkingException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(CODE, e.getCode());
            errorResponse.put(MSG, e.getMessage());
            return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
        }
    }

    @GetMapping("/remaining")
    public ResponseEntity<?> getRemainingSpots(
            @RequestParam(name = "vehicleType", required = false) Optional<String> optionalVehicleType) {
        try {
            RemainingSpotsResponse response = parkingSpotService.getRemainingSpots(optionalVehicleType);
            return ResponseEntity.ok(response);
        } catch (ParkingException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put(CODE, e.getCode());
            errorResponse.put(MSG, e.getMessage());
            return ResponseEntity.status(e.getHttpStatus()).body(errorResponse);
        }
    }
}