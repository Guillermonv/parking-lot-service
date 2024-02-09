package assessment.parkinglot.service;

import assessment.parkinglot.enums.EVehicleType;
import assessment.parkinglot.exception.ParkingError;
import assessment.parkinglot.exception.ParkingException;
import assessment.parkinglot.model.ParkingSpot;
import assessment.parkinglot.model.Vehicle;
import assessment.parkinglot.model.repository.ParkingLotRepository;
import assessment.parkinglot.service.util.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static assessment.parkinglot.service.util.ParkingUtil.getSpotType;
import static assessment.parkinglot.service.util.ParkingUtil.validateVanParkingSpots;

@Service
public class ParkingLotService {
    @Autowired
    private ParkingLotRepository parkingLotRepository;

    @Autowired
    private ParkingSpotService parkingSpotService;

    public void parkVehicle(Vehicle vehicle) {
        parkingSpotService.validateSpotIdsExist(vehicle);
        if (vehicle.getType() == null || !EnumUtils.isValidEnum(EVehicleType.class, vehicle.getType())) {
            throw new ParkingException(ParkingError.INVALID_VEHICLE_TYPE);
        }

        if (parkingLotRepository.isVehicleAlreadyParked(vehicle)) {
            throw new ParkingException(ParkingError.VEHICLE_ALREADY_PARKED);
        }

        if (vehicle.getParkingSpotList() == null || vehicle.getParkingSpotList().isEmpty()) {
            throw new ParkingException(ParkingError.INVALID_SPOT_TYPE);
        }

        validateVanParkingSpots(vehicle.getParkingSpotList(), EVehicleType.valueOf(vehicle.getType()));

        for (ParkingSpot requestedSpot : vehicle.getParkingSpotList()) {
            ParkingSpot spot = parkingLotRepository.getParkingSpotById(requestedSpot.getId());

            if (spot == null || spot.isOccupied() || spot.getType() != getSpotType(requestedSpot.getId())) {
                throw new ParkingException(ParkingError.SPOT_ALREADY_OCCUPIED);
            }

            Vehicle occupyingVehicle = spot.getOccupyingVehicle();
            if (occupyingVehicle != null) {
                throw new ParkingException(ParkingError.VEHICLE_ALREADY_PARKED);
            }

            spot.setOccupyingVehicle(vehicle);
            spot.setOccupied(true);

            parkingLotRepository.saveParkingSpot(spot);
        }

    }

    public void leaveParkingLot(String licensePlate) {
        Optional<Vehicle> vehicle = parkingLotRepository.findVehicleByLicensePlate(licensePlate);

        if (vehicle.isPresent()) {
            for (ParkingSpot requestedSpot : vehicle.get().getParkingSpotList()) {
                ParkingSpot spot = parkingLotRepository.getParkingSpotById(requestedSpot.getId());
                if (spot != null && spot.isOccupied()) {
                    spot.setOccupied(false);
                    spot.setOccupyingVehicle(null);
                    parkingLotRepository.saveParkingSpot(spot);
                }
            }
            vehicle.get().setParkingSpotList(List.of());
        } else {
            throw new ParkingException(ParkingError.INVALID_VEHICLE);
        }
    }

}
