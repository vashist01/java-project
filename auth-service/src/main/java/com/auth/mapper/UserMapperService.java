package com.auth.mapper;

import com.auth.dto.request.UserRegisterRequestDTO;
import com.auth.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserMapperService {
    public User userRegisterDtoToEntityMapper(UserRegisterRequestDTO userRegisterDTO) {

        return User.builder().email(userRegisterDTO.email()).firstName(userRegisterDTO.firstName()).mobileNumber(userRegisterDTO.phoneNumber())
                .active(false).deleted(false).build();
    }
}
