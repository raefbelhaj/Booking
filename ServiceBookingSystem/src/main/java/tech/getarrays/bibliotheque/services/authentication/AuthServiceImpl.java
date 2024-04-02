package tech.getarrays.bibliotheque.services.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tech.getarrays.bibliotheque.DTO.SignupRequestDTO;
import tech.getarrays.bibliotheque.DTO.UserDto;
import tech.getarrays.bibliotheque.entity.User;
import tech.getarrays.bibliotheque.enums.UserRole;
import tech.getarrays.bibliotheque.repository.UserRepository;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository ;

    public UserDto signupClient (SignupRequestDTO signupRequestDTO){
        User user = new User() ;
        user.setName(signupRequestDTO.getName());
        user.setLastname(signupRequestDTO.getLastname());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPhone(signupRequestDTO.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));

        user.setRole(UserRole.CLIENT);

        return userRepository.save(user).getDto();


    }
    @Override
    public Boolean presentByEmail(String email) {
        return userRepository.findFirstByEmail(email) != null;
    }



    public UserDto signupCompany (SignupRequestDTO signupRequestDTO){
        User user = new User() ;
        user.setName(signupRequestDTO.getName());
        user.setEmail(signupRequestDTO.getEmail());
        user.setPhone(signupRequestDTO.getPhone());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequestDTO.getPassword()));

        user.setRole(UserRole.COMPANY);

        return userRepository.save(user).getDto();


    }
}
