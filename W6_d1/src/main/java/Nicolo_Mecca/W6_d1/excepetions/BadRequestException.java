package Nicolo_Mecca.W6_d1.excepetions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String msg) {
        super(msg);
    }
}
