package cci.confferenct.conference.exceptions;

public class WrongCredentials extends RuntimeException {
    public WrongCredentials(String message) {
        super(message);
    }
    
}
