package cci.confferenct.conference.service.imp;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import cci.confferenct.conference.service.EmailNotificatonInterface;

@Service
public class EmailNotification implements  EmailNotificatonInterface{

    @Override
    @Async("taskExecutor")
    public void  sendOtp(String otp, String email) {
        return;
    }
    
    @Override
    @Async("taskExecutor")
    public void sendRemainder(String otp) {
        return;
    }
}
