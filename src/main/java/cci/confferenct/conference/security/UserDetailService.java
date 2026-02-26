package cci.confferenct.conference.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cci.confferenct.conference.model.User;
import cci.confferenct.conference.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * Custom UserDetailsService implementation for Spring Security authentication.
 * Loads user details from the database using phone number as the username.
 * 
 * @author Shortly Team
 * @version 1.0
 * @since 1.0
 */
@Service
@AllArgsConstructor
@Getter
public class UserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads user details by phone number for Spring Security authentication.
     * 
     * @param phoneNumber the phone number used as username (must be in +233XXXXXXXXX format)
     * @return UserDetails object containing user authentication information
     * @throws UsernameNotFoundException if no user is found with the given phone number
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email  ));
            return new CustomUserDetails(user);
    } 
}

