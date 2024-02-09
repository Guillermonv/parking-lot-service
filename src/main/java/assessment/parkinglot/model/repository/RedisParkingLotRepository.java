package assessment.parkinglot.model.repository;

import assessment.parkinglot.model.ParkingSpot;
import assessment.parkinglot.model.Vehicle;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class RedisParkingLotRepository implements ParkingLotRepository {
    private static final String PARKING_LOT_KEY = "parkingLot";

    private RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, ParkingSpot> hashOperations;

    public RedisParkingLotRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public ParkingSpot getParkingSpotById(String spotId) {
        return hashOperations.get(PARKING_LOT_KEY, spotId);
    }

    @Override
    public void saveParkingSpot(ParkingSpot spot) {
        hashOperations.put(PARKING_LOT_KEY, spot.getId(), spot);
    }

    @Override
    public Optional<Vehicle> findVehicleByLicensePlate(String licensePlate) {
        Map<String, ParkingSpot> parkingData = hashOperations.entries(PARKING_LOT_KEY);

        for (ParkingSpot spot : parkingData.values()) {
            Vehicle occupyingVehicle = spot.getOccupyingVehicle();
            if (occupyingVehicle != null && occupyingVehicle.getLicensePlate().equals(licensePlate)) {
                return Optional.of(occupyingVehicle);
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean isVehicleAlreadyParked(Vehicle vehicle) {
        Map<String, ParkingSpot> parkingData = hashOperations.entries(PARKING_LOT_KEY);

        for (ParkingSpot spot : parkingData.values()) {
            if (Objects.nonNull(spot.getOccupyingVehicle()) && spot.isOccupied() &&
                    spot.getOccupyingVehicle().getLicensePlate().equals(vehicle.getLicensePlate())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Map<String, ParkingSpot> getAllParkingSpots() {
        return hashOperations.entries(PARKING_LOT_KEY);
    }

}
