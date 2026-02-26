package cci.confferenct.conference.model;
import lombok.Data;


@Data
public class VirtualAccount {
    private String userId;
    private double balance;
    private int totalTopUp;
    private double totalSpent;
}
