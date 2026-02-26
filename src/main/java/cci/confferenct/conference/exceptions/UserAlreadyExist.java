package cci.confferenct.conference.exceptions;

public class UserAlreadyExist extends RuntimeException {
    public UserAlreadyExist(String message) {
        super(message);
    }
}
