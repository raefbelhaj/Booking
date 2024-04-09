package tech.getarrays.bibliotheque.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tech.getarrays.bibliotheque.DTO.AuthenticationRequest;
import tech.getarrays.bibliotheque.DTO.SignupRequestDTO;
import tech.getarrays.bibliotheque.DTO.UserDto;
import tech.getarrays.bibliotheque.entity.User;
import tech.getarrays.bibliotheque.repository.UserRepository;
import tech.getarrays.bibliotheque.services.authentication.AuthService;
import tech.getarrays.bibliotheque.services.jwt.UserDetailsServiceImpl;
import tech.getarrays.bibliotheque.utils.jwtUtil;

import java.io.IOException;

@RestController
public class AuthenticationController {
    @Autowired
    private AuthService authService ;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private jwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    public static final String TOKEN_PREFIX = "Bearer ";
    public  static final String HEADER_STRING = "Authorization";

    @PostMapping("/client/sign-up")
    public ResponseEntity<?> signupClient(@RequestBody SignupRequestDTO signupRequestDTO){

        if (authService.presentByEmail(signupRequestDTO.getEmail())){
            return  new ResponseEntity<>("client is already exist with this email", HttpStatus.NOT_ACCEPTABLE);

        }

        UserDto createdUser = authService.signupClient(signupRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);


    }
    @PostMapping("/company/sign-up")
    public ResponseEntity<?> signupCompany(@RequestBody SignupRequestDTO signupRequestDTO){

        if (authService.presentByEmail(signupRequestDTO.getEmail())){
            return  new ResponseEntity<>("company is already exist with this email", HttpStatus.NOT_ACCEPTABLE);

        }

        UserDto createdUser = authService.signupCompany(signupRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.OK);


    }

    @PostMapping("/authenticate")
    public void createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest,
                                          HttpServletResponse response) throws IOException, JSONException {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),authenticationRequest.getPassword()
            ));
        } catch (BadCredentialsException e){
            throw new BadCredentialsException("incorrect username or password", e);

        }


        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        User user = userRepository.findFirstByEmail(authenticationRequest.getUsername());

     response.getWriter().write(new JSONObject()
                      .put("userId", user.getId())
                     .put("role", user.getRole())
                     .toString()

                  );

     response.addHeader("access-Control-Expose-Headers", "Authorization");
     response.addHeader("Access-Control-Allow-Headers", "Authorization,"+
             "X-PINGOTHER, origin, X-Requested-With, content-Type, Accept, X-Custom-header");
     response.addHeader(HEADER_STRING, TOKEN_PREFIX+jwt);




    }


}
