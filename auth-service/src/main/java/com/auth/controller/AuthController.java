package com.auth.controller;

import com.auth.enums.ApiVersionEnum;
import com.auth.dto.request.UserRegisterRequestDTO;
import com.auth.dto.request.LoginRequestDTO;
import com.auth.dto.response.LoginResponse;
import com.auth.dto.response.RegisterUserResponse;
import com.auth.dto.response.ResponseBO;
import com.auth.exception.GlobeExceptionHandler;
import com.auth.exception.custome.UserAlreadyExistException;
import com.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiVersionEnum.PREFIX_API_URL)
@RequiredArgsConstructor
@Slf4j
@Validated
public class AuthController extends GlobeExceptionHandler {

    private final AuthService authService;

    @PostMapping(value = ApiVersionEnum.REGISTER_USER,
            headers = "X-API-VERSION=1")
    public ResponseEntity<ResponseBO>  registerUser(@RequestBody @Valid UserRegisterRequestDTO userRegisterDTO,
                                                    HttpServletRequest httpServletRequest) throws UserAlreadyExistException {
        log.info("Request : --> Controller registerUser method is start : ");
        RegisterUserResponse response = authService.registerUser(userRegisterDTO);
        return successResponse(response,httpServletRequest);
    }

    @PostMapping(value = ApiVersionEnum.LOGIN,headers = "X-API-VERSION=1")
    public ResponseEntity<ResponseBO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO, HttpServletRequest httpServletRequest) {
       log.info("Request : --> Controller login method is start : ");
       LoginResponse loginResponse = authService.login(loginRequestDTO);
       return successResponse(loginResponse,httpServletRequest);
    }
}
