//package com.easygame.easygame.services;
//
//
//import com.easygame.easygame.model.UserModel;
//
//import com.easygame.easygame.security.AuthenticationService;
//import com.easygame.easygame.security.JwtService;
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
//public class TemplateServiceTest {
//    @Mock
//    private TemplatesRepository templateRepository;
//    @Mock
//    private AuthenticationService authenticationService;
//    @Mock
//    private JwtService jwtService;
//
//    @InjectMocks
//    private TemplateService templateService;
//
//    private Template template;
//    @BeforeEach
//    public void init(){
//        template = Template.builder()
//                .templatePhoto(1)
//                .description("gregerg")
//                .title("gregre")
//                .creator("fkkfkf")
//                .build();
//    }
//
//    @Test
//    public void UserService_CreateUser_ReturnSavedUser(){
//        when(templateRepository.save(any())).thenReturn(template);
//        when(templateRepository.existsByCreator(template.getCreator())).thenReturn(false);
//        when(templateRepository.existsByTitle(template.getTitle())).thenReturn(false);
//
//        UserModel newUser = template.create(user);
//        Assertions.assertThat(newUser).isNotNull();
//        Assertions.assertThat(newUser.getId()).isEqualTo(0);
//    }
//}
