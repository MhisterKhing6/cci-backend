package cci.confferenct.conference.service.imp;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cci.confferenct.conference.dto.request.RegisterConferencRequest;
import cci.confferenct.conference.enums.RegistrationType;
import cci.confferenct.conference.enums.TransactionTypes;
import cci.confferenct.conference.exceptions.EntityNotFound;
import cci.confferenct.conference.exceptions.OperationNotPermitted;
import cci.confferenct.conference.model.Confrence;
import cci.confferenct.conference.model.ConfrenceInfo;
import cci.confferenct.conference.model.ConfrenceRegistration;
import cci.confferenct.conference.model.Transaction;
import cci.confferenct.conference.model.User;
import cci.confferenct.conference.model.UserInfo;
import cci.confferenct.conference.repository.ConfrenceRegistrationRepository;
import cci.confferenct.conference.repository.ConfrenceRepository;
import cci.confferenct.conference.repository.TransactionRepository;
import cci.confferenct.conference.repository.UserRepository;
import cci.confferenct.conference.service.TransactionServiceInterface;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TransactionService implements TransactionServiceInterface {
    private final TransactionRepository transactionRepository;
    private final ConfrenceRepository conferenceRepository;
    private final UserRepository userRepository;
    private final ConfrenceRegistrationRepository confRegRepository;
    private final MongoTemplate mongoTemplate;
    
    @Override
    public Page<Transaction> getAllTransactions(Pageable pageable) { 
        return transactionRepository.findAll(pageable);
    }

    @Override
    public Page<Transaction> getUserTransactions(String email, Pageable pageable) { 
        return transactionRepository.findAllByUserInfoEmail(email, pageable);
    }
    
    @Override
    public ConfrenceRegistration registerConference(RegisterConferencRequest registerConferencRequest, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new EntityNotFound("User not found"));
        Confrence conference = conferenceRepository.findById(registerConferencRequest.getConferenceId())
                .orElseThrow(() -> new EntityNotFound("Conference not found"));
    
        double costOfConfrence = user.isCCIMember() ? conference.getCciPrice() : conference.getNonCciPrice();
        RegistrationType regType = user.isCCIMember() ? RegistrationType.CCIMEMBER : RegistrationType.NONCCIMEMBER;
        //check if today is greater than start conference dat throw runtime error throw and expetion
        long currentTimeMillis = System.currentTimeMillis();
        //check time
        if(conference.getStartDate() < currentTimeMillis) {
            throw new OperationNotPermitted("Conference has not started yet");
        }
        //check if the amount is greater than conference cost throw opertion not permitted excetion
        if(user.getBalance().compareTo(BigDecimal.valueOf(costOfConfrence)) < 0) {
            throw new OperationNotPermitted("Insufficient balance");
        }
        //form user info
        UserInfo userInfo = UserInfo.builder()
                .name(user.getName())
                .email(user.getEmail())
                .userId(user.getId())
                .whatsappNumber(user.getWhatsappNumber())
                .build();
        ConfrenceInfo confrenceInfo = ConfrenceInfo.builder()
                .confrenceId(conference.getId())
                .confrenceName(conference.getName())
                .location(conference.getLocation())
                .startDate(Long.toString(conference.getStartDate()))
                .endDate(Long.toString(conference.getEndDate()))
                .build();
        ConfrenceRegistration registerConfrenceRegistration = new ConfrenceRegistration();
        registerConfrenceRegistration.setUserInfo(userInfo);
        registerConfrenceRegistration.setConfrenceInfo(confrenceInfo);
        registerConfrenceRegistration.setConfrenceId(conference.getId());
        registerConfrenceRegistration.setUserEmail(user.getEmail());
        registerConfrenceRegistration.setCost(costOfConfrence);
        registerConfrenceRegistration.setRegistrationDate(System.currentTimeMillis());
        registerConfrenceRegistration.setRegistrationType(regType);
        confRegRepository.save(registerConfrenceRegistration);        
        user.setBalance(user.getBalance().subtract(BigDecimal.valueOf(costOfConfrence)));
        userRepository.save(user);

        Transaction transaction = new Transaction();

        transaction.setAccessCode("DEBIT");
        transaction.setAmount(BigDecimal.valueOf(costOfConfrence));
        transaction.setUserInfo(userInfo);
        transaction.setStatus("success");
        transaction.setReference("DEBIT");
        transaction.setTransactionDate(currentTimeMillis);
        transaction.setTransactionType(TransactionTypes.DEBIT);
        transactionRepository.save(transaction);
        return registerConfrenceRegistration;
    }

    @Override
    
    public Page<ConfrenceRegistration> adminConfrenceSearch(String conferenceId, String userEmail, RegistrationType registrationType,Pageable pageable) {
        Query query = new Query();

        if (conferenceId != null && !conferenceId.isBlank()) {
            query.addCriteria(
                Criteria.where("confrenceId").is(conferenceId)
            );
        }

        if (userEmail != null && !userEmail.isBlank()) {
            query.addCriteria(
                Criteria.where("userEmail").regex(userEmail, "i")
            );
        }

        if (registrationType != null) {
            query.addCriteria(
                Criteria.where("registrationType").is(registrationType)
            );
        }

        long total = mongoTemplate.count(query, ConfrenceRegistration.class);

        query.with(pageable);

        List<ConfrenceRegistration> results =
                mongoTemplate.find(query, ConfrenceRegistration.class);

        return new PageImpl<>(results, pageable, total);
    }
    
    @Override
    public Page<ConfrenceRegistration> getUserRegistrations( String userEmail, Pageable pageable) {
        return confRegRepository.findAllByUserEmail(userEmail, pageable);
    }
}
