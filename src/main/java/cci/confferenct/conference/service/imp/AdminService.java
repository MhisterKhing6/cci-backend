package cci.confferenct.conference.service.imp;

import java.time.ZoneId;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import cci.confferenct.conference.dto.request.ConferenceDetailsRequest;
import cci.confferenct.conference.dto.request.UpdateConfrenceRequest;
import cci.confferenct.conference.dto.response.UserResponse;
import cci.confferenct.conference.exceptions.EntityNotFound;
import cci.confferenct.conference.model.Confrence;
import cci.confferenct.conference.model.User;
import cci.confferenct.conference.repository.ConfrenceRepository;
import cci.confferenct.conference.repository.TransactionRepository;
import cci.confferenct.conference.repository.UserRepository;
import cci.confferenct.conference.service.AdminServiceInterface;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AdminService implements AdminServiceInterface {
    private final ConfrenceRepository confrenceRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;
        private final TransactionRepository transactionRepository;
    @Override
    public Confrence addConference(ConferenceDetailsRequest conferenceDetails) {
        Confrence confrence = confrenceMapper(conferenceDetails);
        confrenceRepository.save(confrence);
        return confrence;
    }
 
    @Override
    public UserResponse deleteConfrence(String confrenceId) {
        Confrence existing = confrenceRepository.findById(confrenceId)
                .orElseThrow(() -> new EntityNotFound("Conference not found"));
        confrenceRepository.delete(existing);
        return UserResponse.builder().message("Conference deleted successfully").build();
    }
    
    @Override
    public Confrence updateConference(String id, UpdateConfrenceRequest updatedConference) {

        Confrence existing = confrenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFound("Conference not found"));

        if (updatedConference.getName() != null) {
            existing.setName(updatedConference.getName());
        }
        if (updatedConference.getDescription() != null) {
            existing.setDescription(updatedConference.getDescription());
        }
        if (updatedConference.getStartDate() > 0) {
            existing.setStartDate(updatedConference.getStartDate());
        }
        if (updatedConference.getEndDate() > 0) {
            existing.setEndDate(updatedConference.getEndDate());
        }
        if (updatedConference.getLocation() != null) {
            existing.setLocation(updatedConference.getLocation());
        }
        if (updatedConference.getCciPrice() > 0) {
            existing.setCciPrice(updatedConference.getCciPrice());
        }
        if (updatedConference.getNonCciPrice() > 0) {
            existing.setNonCciPrice(updatedConference.getNonCciPrice());
        }
        if (updatedConference.getIsActive() != null) {
            existing.setActive(updatedConference.getIsActive());
        }
        return confrenceRepository.save(existing);
    }

     @Override
    public  Page<User> getAllUsers(String name, String email, String whatsappNumber, Pageable pageable) {
        Query query = new Query();

        boolean hasFilter = false;

        if (name != null && !name.isBlank()) {
            query.addCriteria(Criteria.where("name")
                    .regex(name, "i"));
            hasFilter = true;
        }

        if (email != null && !email.isBlank()) {
            query.addCriteria(Criteria.where("email")
                    .regex(email, "i"));
            hasFilter = true;
        }

        if (whatsappNumber != null && !whatsappNumber.isBlank()) {
            query.addCriteria(Criteria.where("whatsappNumber")
                    .regex(whatsappNumber, "i"));
            hasFilter = true;
        }

        long total;

        if (!hasFilter) {
            total = mongoTemplate.count(new Query(), User.class);
            List<User> users = mongoTemplate.find(
                    new Query().with(pageable),
                    User.class
            );
            return new PageImpl<>(users, pageable, total);
        }

        total = mongoTemplate.count(query, User.class);

        query.with(pageable);

        List<User> users = mongoTemplate.find(query, User.class);

        return new PageImpl<>(users, pageable, total);
        }
    
    @Override
    public UserResponse changeActivateStatus(String userId, boolean activate) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFound("User not found"));

        user.setActivated(activate);

        userRepository.save(user);

        return UserResponse.builder().message("User activated status changed successfully").build();
    }

    @Override
    public UserResponse deleteUser(String userId) {

        if (!userRepository.existsById(userId)) {
            throw new EntityNotFound("User not found");
        }

        userRepository.deleteById(userId);
        return UserResponse.builder().message("User deleted successfully").build();
    }

    @Override
    public Confrence getConferenceById(String id) {
        return  confrenceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFound("Conference not found"));
    }

    @Override
    public UserResponse toggleConferenceStatus(String conferenceId) {
        Confrence conference = confrenceRepository.findById(conferenceId)
                .orElseThrow(() -> new EntityNotFound("Conference not found"));
        
        conference.setActive(!conference.isActive());
        confrenceRepository.save(conference);
        
        return UserResponse.builder()
                .message("Conference status updated to " + (conference.isActive() ? "active" : "inactive"))
                .build();
    }

   

    /**
     * Maps ConferenceDetailsRequest to Confrence entity
     * @param conferenceDetails
     * @return
     */
    
    private Confrence confrenceMapper(ConferenceDetailsRequest conferenceDetails) { 
    Confrence confrence = new Confrence();
    confrence.setName(conferenceDetails.getName());
    confrence.setDescription(conferenceDetails.getDescription());
    confrence.setCciPrice(conferenceDetails.getCciPrice());
    confrence.setNonCciPrice(conferenceDetails.getNonCciPrice());

    long startMillis = conferenceDetails.getStartDate()
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli();

    long endMillis = conferenceDetails.getEndDate()
            .atZone(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli();

    confrence.setStartDate(startMillis);
    confrence.setEndDate(endMillis);
    confrence.setLocation(conferenceDetails.getLocation());
    confrence.setActive(conferenceDetails.getIsActive());
    return confrence;

    }


   


}
