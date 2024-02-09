package assessment.parkinglot.service;

import assessment.parkinglot.controller.response.RemainingSpotsResponse;
import assessment.parkinglot.enums.EParkingSpotType;
import assessment.parkinglot.enums.EVehicleType;
import assessment.parkinglot.exception.ParkingError;
import assessment.parkinglot.exception.ParkingException;
import assessment.parkinglot.model.ParkingSpot;
import assessment.parkinglot.model.Vehicle;
import assessment.parkinglot.model.repository.ParkingLotRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ParkingSpotService {
    @Autowired
    private ParkingLotRepository parkingLotRepository;


    @PostConstruct
    public void initializeSpot() {
        try {
            initializeSpots();
            log.info("ParkingLotService initialized successfully.");
        } catch (Exception e) {
            log.error("Error during ParkingLotService initialization: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public RemainingSpotsResponse getRemainingSpots(Optional<String> optionalVehicleType) {
        Map<EParkingSpotType, Integer> availableSpotCounts = new HashMap<>();
        Map<EParkingSpotType, List<String>> availableSpots = new HashMap<>();
        Map<String, ParkingSpot> allSpots = parkingLotRepository.getAllParkingSpots();

        for (ParkingSpot spot : allSpots.values()) {
            if (!spot.isOccupied()) {
                availableSpotCounts.merge(spot.getType(), 1, Integer::sum);
                availableSpots.computeIfAbsent(spot.getType(), k -> new ArrayList<>()).add(spot.getId());
            }
        }
        Optional<EVehicleType> vehicleType = optionalVehicleType
                .map(type -> {
                    try {
                        return EVehicleType.valueOf(type.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new ParkingException(ParkingError.INVALID_VEHICLE_TYPE);
                    }
                });
        if (optionalVehicleType.isPresent()) {
            filterSpotsForVehicleType(vehicleType.get(), availableSpotCounts, availableSpots);
        }

        return new RemainingSpotsResponse(availableSpotCounts, availableSpots);
    }

    private void filterSpotsForVehicleType(EVehicleType type, Map<EParkingSpotType, Integer> availableSpotCounts, Map<EParkingSpotType, List<String>> availableSpots) {
        switch (type) {
            case MOTORCYCLE:
                filterSpotsForType(availableSpotCounts, availableSpots, EParkingSpotType.MOTORCYCLE);
                break;
            case CAR:
                filterSpotsForType(availableSpotCounts, availableSpots, EParkingSpotType.COMPACT, EParkingSpotType.REGULAR);
                break;
            case VAN:
                filterSpotsForType(availableSpotCounts, availableSpots, EParkingSpotType.REGULAR);
                break;
            default:
                throw new IllegalArgumentException("Invalid vehicle type");
        }
    }


    private void filterSpotsForType(Map<EParkingSpotType, Integer> availableSpotCounts, Map<EParkingSpotType, List<String>> availableSpots, EParkingSpotType... types) {
        availableSpotCounts.keySet().retainAll(Arrays.asList(types));
        availableSpots.keySet().retainAll(Arrays.asList(types));

        for (EParkingSpotType type : types) {
            if (availableSpots.containsKey(type)) {
                availableSpotCounts.put(type, availableSpots.get(type).size());
            }
        }
    }

    private void initializeSpots() {
        int motorcycleSpots = 5, compactSpots = 11, regularSpots = 9;

        for (int i = 1; i <= motorcycleSpots; i++) {
            parkingLotRepository.saveParkingSpot(new ParkingSpot("M" + i, EParkingSpotType.MOTORCYCLE, false, null));
        }

        for (int i = 1; i <= compactSpots; i++) {
            parkingLotRepository.saveParkingSpot(new ParkingSpot("C" + i, EParkingSpotType.COMPACT, false, null));
        }

        for (int i = 1; i <= regularSpots; i++) {
            parkingLotRepository.saveParkingSpot(new ParkingSpot("R" + i, EParkingSpotType.REGULAR, false, null));
        }
    }

    public void validateSpotIdsExist(Vehicle vehicle) {
        Map<String, ParkingSpot> allSpots = parkingLotRepository.getAllParkingSpots();

        for (ParkingSpot requestedSpot : vehicle.getParkingSpotList()) {
            ParkingSpot spot = allSpots.get(requestedSpot.getId());
            if (spot == null) {
                throw new ParkingException(ParkingError.INVALID_SPOT);
            }
        }
    }
}
