# Park Vehicle API Documentation

## Summary

---
This is a parking lot service that contain fixed spots types, by default has

motorcycleSpots = 5 
compactSpots = 11
regularSpots = 9

if you want to edit the quantities of spot types you can achieve that on :

```
assessment.parkinglot.service.ParkingSpotService.initializeSpots
```


the only external dependency outside gradle is the redis DB .
to achieve this you can run : 
```
brew install redis 
brew services start redis
```

## Endpoints

---

* Park Car  

Endpoint ```http POST http://localhost:8080/parking/park```
 
To park a vehicle, you must include the following fields in the request body:

```
{
    "vehicle": {
        "licensePlate": "128",
        "type": "CAR", // MOTORCYCLE, CAR, VAN
        "spots": [
            {
                "id": "C1" // M spots have type MOTORKIKE, R spots have type REGULAR, C spots have type COMPACT
            }
        ]
    }
}
```
Response
Upon successful parking, the system will respond with the following message:

```
{
    "message": "Vehicle parked successfully"
}
```
Some common errors
```
{
    "code": 1005,
    "message": "Vehicle is already parked"
}
```

```
{
    "code": 1006,
    "message": "The spot is already used by another vehicle. Check available places on GET /parking/remaining"
}
```
```
{
    "code": 1007,
    "message": "One or more spot IDs do not exist. Please check on GET /parking/remaining"
}
```
```
{
    "code": 1008,
    "message": "Motorbikes and cars should take only one spot"
}
```
---
* Leave Car


Endpoint  ```http DEL http://localhost:8080/parking/leave/{licensePlate}```
 
To leave a car its necessary to send the licensePlate otherwise you wil get and error saying that is required

Response
Upon successful park left , the system will respond with the following message:

```
{
    "message": "Vehicle left the parking lot successfully"
}
```

And some common Errors
```
{
"code": 1009,
"message": "Vehicle not found or already left the parking spot"
}
```

---

* Find how many spots are remaining 

Retrieves a list with all available spots :

Endpoint ```http GET http://localhost:8080/parking/remaining```

will return the ones that are not parked by any vehicle
```
{
    "availableSpotCounts": {
        "COMPACT": 10,
        "MOTORCYCLE": 4,
        "REGULAR": 9
    },
    "availableSpots": {
        "COMPACT": [
            "C8",
            "C4",
            "C11",
            "C6",
            "C3",
            "C2",
            "C5",
            "C9",
            "C10",
            "C7"
        ],
        "MOTORCYCLE": [
            "M3",
            "M5",
            "M2",
            "M4"
        ],
        "REGULAR": [
            "R3",
            "R6",
            "R7",
            "R1",
            "R5",
            "R2",
            "R9",
            "R8",
            "R4"
        ]
    }
}
```

---

* Check if all parking spots are taken for a given vehicle type

Endpoint to get the list of all available spots  for a given vehicle tye

its the same as previous one but its necessary to send the optional request param 

```http GET http://localhost:8080/parking/remaining?vehicleType=VAN```

```
{
    "availableSpotCounts": {
        "REGULAR": 9
    },
    "availableSpots": {
        "REGULAR": [
            "R3",
            "R6",
            "R7",
            "R1",
            "R5",
            "R2",
            "R9",
            "R8",
            "R4"
        ]
    }
}
```

if you sent a vehicle type that is invalid you will get 


```
{
    "code": 1010,
    "message": "Vehicle type not exist , valid are : MOTORBIKE , CAR , VAN"
}
```
