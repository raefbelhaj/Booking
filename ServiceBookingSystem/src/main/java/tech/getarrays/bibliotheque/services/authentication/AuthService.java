package tech.getarrays.bibliotheque.services.authentication;

import tech.getarrays.bibliotheque.DTO.SignupRequestDTO;
import tech.getarrays.bibliotheque.DTO.UserDto;

public interface AuthService {
    UserDto signupClient (SignupRequestDTO signupRequestDTO);
    Boolean presentByEmail(String email);
    UserDto signupCompany (SignupRequestDTO signupRequestDTO);

}
