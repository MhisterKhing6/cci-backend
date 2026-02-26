package cci.confferenct.conference.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import cci.confferenct.conference.enums.TransactionTypes;
import lombok.Data;

@Data
@Document(collection = "transaction_history")
public class Transaction {
    
    @Id
    private String id;
    private UserInfo userInfo;
    private BigDecimal  amount;
    private long  transactionDate;
    private String reference;
    private TransactionTypes transactionType;
    private String status;
    private String accessCode;
}
