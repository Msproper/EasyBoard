//package com.easygame.easygame.repositories;
//
//import com.easygame.easygame.enums.Role;
//import com.easygame.easygame.model.UserModel;
//import com.easygame.easygame.repository.UserRepository;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import java.util.Optional;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
//public class UserRepositoryTest {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    private UserModel userModel;
//
//    @BeforeEach
//    public void init(){
//        userModel = UserModel.builder().email("mail@gmail.com").password("qweasd").username("New User").role(Role.ROLE_USER).groupId(0L).build();
//    }
//
//    @Test
//    public void UserRepository_SaveAll_ReturnSavedUser(){
//        UserModel savedUser = userRepository.save(userModel);
//
//        Assertions.assertThat(savedUser).isNotNull();
//        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
//    }
//
//    @Test
//    public void UserRepository_FindUserById_ReturnUser(){
//        UserModel savedUser = userRepository.save(userModel);
//        Optional<UserModel> foundUser = userRepository.findById(savedUser.getId());
//
//        Assertions.assertThat(foundUser.isPresent()).isTrue();
//        Assertions.assertThat(foundUser.get().getUsername()).isEqualTo(savedUser.getUsername());
//    }
//
//    @Test
//    public void UserRepository_FindUserByUsername_ReturnUser(){
//        UserModel savedUser = userRepository.save(userModel);
//        Optional<UserModel> foundUser = userRepository.findByUsername(savedUser.getUsername());
//
//        Assertions.assertThat(foundUser.isPresent()).isTrue();
//        Assertions.assertThat(foundUser.get().getId()).isGreaterThan(0);
//    }
//
//    @Test
//    public void UserRepository_CheckExistOfUserByUsername_ReturnStatus(){
//        UserModel savedUser = userRepository.save(userModel);
//        String fakeUsername = "Fake user";
//
//        Boolean shouldBeExist = userRepository.existsByUsername(savedUser.getUsername());
//        Boolean shouldNotExist = userRepository.existsByUsername(fakeUsername);
//
//        Assertions.assertThat(shouldBeExist).isTrue();
//        Assertions.assertThat(shouldNotExist).isFalse();
//
//    }
//
//    @Test
//    public void UserRepository_CheckExistOfUserByEmail_ReturnStatus(){
//        UserModel savedUser = userRepository.save(userModel);
//        String fakeEmail = "Fake user";
//
//        Boolean shouldBeExist = userRepository.existsByEmail(savedUser.getEmail());
//        Boolean shouldNotExist = userRepository.existsByEmail(fakeEmail);
//
//        Assertions.assertThat(shouldBeExist).isTrue();
//        Assertions.assertThat(shouldNotExist).isFalse();
//
//    }
//}
