package assessment.parkinglot.service.util;

import assessment.parkinglot.controller.request.ParkingLotRequest;
import assessment.parkinglot.enums.EParkingSpotType;
import assessment.parkinglot.enums.EVehicleType;
import assessment.parkinglot.exception.ParkingError;
import assessment.parkinglot.exception.ParkingException;
import assessment.parkinglot.model.ParkingSpot;

import java.util.List;
import java.util.Objects;

public class ParkingUtil {

    public static void validateVanParkingSpots(List<ParkingSpot> parkingSpots, EVehicleType vehicleType) {
        if (EVehicleType.VAN.equals(vehicleType) && parkingSpots.size() != 3) {
            throw new ParkingException(ParkingError.INVALID_VAN_PARKING_SPOTS_AMOUNT);
        }
        if (!EVehicleType.VAN.equals(vehicleType) && parkingSpots.size() != 1) {
            throw new ParkingException(ParkingError.INVALID_CAR_OR_MOTORBIKE_PARKING_SPOTS);
        }
        for (ParkingSpot spot : parkingSpots) {
            if (EVehicleType.VAN.equals(vehicleType) && getSpotType(spot.getId()) != EParkingSpotType.REGULAR) {
                throw new ParkingException(ParkingError.INVALID_VAN_PARKING_SPOTS_TYPE);
            }
        }
    }

    public static EParkingSpotType getSpotType(String spotId) {
        if (spotId.startsWith("M")) {
            return EParkingSpotType.MOTORCYCLE;
        } else if (spotId.startsWith("C")) {
            return EParkingSpotType.COMPACT;
        } else if (spotId.startsWith("R")) {
            return EParkingSpotType.REGULAR;
        } else {
            throw new ParkingException(ParkingError.INVALID_SPOT_TYPE);
        }

    }

    public static void validateRequest(ParkingLotRequest request) {
        if (Objects.isNull(request.getVehicle())) {
            throw new ParkingException(ParkingError.MISSING_VEHICLE);
        }
        if (Objects.isNull(request.getVehicle().getLicensePlate())) {
            throw new ParkingException(ParkingError.MISSING_LICENSE_PLATE);
        }
        if (Objects.isNull(request.getVehicle().getType())) {
            throw new ParkingException(ParkingError.MISSING_VEHICLE_TYPE);
        }
        if (Objects.isNull(request.getVehicle().getParkingSpotList())) {
            throw new ParkingException(ParkingError.MISSING_SPOT);
        }
    }

}
