package assessment.parkinglot.model.repository;

import assessment.parkinglot.model.ParkingSpot;
import assessment.parkinglot.model.Vehicle;

import java.util.Map;
import java.util.Optional;

public interface ParkingLotRepository {

    ParkingSpot getParkingSpotById(String spotId);

    void saveParkingSpot(ParkingSpot spot);

    Optional<Vehicle> findVehicleByLicensePlate(String licensePlate);

    boolean isVehicleAlreadyParked(Vehicle vehicle);


    Map<String, ParkingSpot> getAllParkingSpots();

}
