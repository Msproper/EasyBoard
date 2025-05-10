//package com.easygame.easygame.services;
//
//
//import com.easygame.easygame.enums.Role;
//import com.easygame.easygame.model.UserModel;
//import com.easygame.easygame.repository.UserRepository;
//
//import com.easygame.easygame.security.AuthenticationService;
//import com.easygame.easygame.security.JwtService;
//import com.easygame.easygame.service.UserService;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private AuthenticationService authenticationService;
//    @Mock
//    private JwtService jwtService;
//
//    @InjectMocks
//    private UserService userService;
//
//    private UserModel user;
//    @BeforeEach
//    public void init(){
//        user = UserModel.builder()
//                .groupId(0L)
//                .id(0L)
//                .email("absdefg@gmail.com")
//                .username("Test")
//                .role(Role.ROLE_USER)
//                .password("1122334455")
//                .build();
//    }
//
//    @Test
//    public void UserService_CreateUser_ReturnSavedUser(){
//        when(userRepository.save(any())).thenReturn(user);
//        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
//        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
//
//        UserModel newUser = userService.create(user);
//        Assertions.assertThat(newUser).isNotNull();
//        Assertions.assertThat(newUser.getId()).isEqualTo(0);
//    }
//}
