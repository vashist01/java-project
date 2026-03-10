package com.auth.service;

import com.auth.dto.request.LoginRequestDTO;
import com.auth.dto.request.UserRegisterRequestDTO;
import com.auth.dto.response.LoginResponse;
import com.auth.dto.response.RegisterUserResponse;
import com.auth.entity.User;
import com.auth.enums.ResponseMessageEnum;
import com.auth.exception.custome.UserAlreadyExistException;
import com.auth.mapper.UserMapperService;
import com.auth.repository.RegisterUserRepository;
import com.auth.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final RegisterUserRepository registerUserRepository;
    private final UserMapperService userMapperService;
    private final JwtUtil jwtUtil;
    public RegisterUserResponse registerUser(UserRegisterRequestDTO userRegisterDTO) throws UserAlreadyExistException {
       log.info("Request -> : AuthService registerUser is start : {}",userRegisterDTO);


            Optional<User> userOptional = registerUserRepository.findByMobileNumber(userRegisterDTO.phoneNumber());
            if(userOptional.isEmpty()){
                User user = userMapperService.userRegisterDtoToEntityMapper(userRegisterDTO);
                 registerUserRepository.save(user);
                return new RegisterUserResponse(userRegisterDTO.firstName(),userRegisterDTO.email());
            }
            throw new UserAlreadyExistException(ResponseMessageEnum.ALREADY_EXIST.getMessage(),HttpStatus.ALREADY_REPORTED);

    }

    public LoginResponse login(LoginRequestDTO loginRequestDTO) {
        log.info("Request -> : AuthService login is start : {}",loginRequestDTO);
         String token = jwtUtil.generateToken(loginRequestDTO.mobileNumber());
        return LoginResponse.builder().token(token).build();
    }
}
