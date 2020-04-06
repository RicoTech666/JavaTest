package exception;

public class ParkingLotFullException extends RuntimeException {
    public ParkingLotFullException(String message) {
        super(message);
    }

    public ParkingLotFullException(String message, Throwable cause) {
        super(message, cause);
    }
}
