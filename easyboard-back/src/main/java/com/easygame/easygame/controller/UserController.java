package com.easygame.easygame.controller;


import com.easygame.easygame.DTO.auth.DetailsRequest;
import com.easygame.easygame.model.UsersDetails;
import com.easygame.easygame.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Поиск подробных данных юзера по его юзернейму")
    @GetMapping("/getDetailsByUsername")
    private ResponseEntity<?> findUserDetailsByUsername(String username){
        UsersDetails findedUser = userService.getByUsername(username).getUsersDetails();
        return new ResponseEntity<>(findedUser, HttpStatus.OK);
    }

    @GetMapping("/getByKeyword")
    private ResponseEntity<?> findUsersByKeyword(String keyword){
        var findedUsers = userService.getUsersByKeyword(keyword);
        return new ResponseEntity<>(findedUsers, HttpStatus.OK);
    }

    @Operation(summary = "Поиск подробных данных самого юзера")
    @GetMapping("/")
    private ResponseEntity<?> getMyDetails(){
        var userDetails = userService.getCurrentUser().getUsersDetails();
        return new ResponseEntity<>(userDetails, HttpStatus.OK);
    }

    @PostMapping("/")
    private ResponseEntity<?> setMyDetails(@RequestBody DetailsRequest detailsRequest){
        userService.setDetails(detailsRequest);
        return new ResponseEntity<>( HttpStatus.OK);
    }
}