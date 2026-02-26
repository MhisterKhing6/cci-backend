package cci.confferenct.conference.service;

public interface  EmailNotificatonInterface {
    
    void sendOtp(String otp, String email);

    void sendRemainder(String email);
}
