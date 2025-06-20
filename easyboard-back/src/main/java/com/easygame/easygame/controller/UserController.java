package com.easygame.easygame.controller;


import com.easygame.easygame.DTO.auth.DetailsRequest;
import com.easygame.easygame.model.UsersDetails;
import com.easygame.easygame.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Поиск подробных данных юзера по его юзернейму")
    @GetMapping("/DetailsByUsername")
    private ResponseEntity<?> findUserDetailsByUsername(String username){
        UsersDetails foundUser = userService.getByUsername(username).getUsersDetails();
        return new ResponseEntity<>(foundUser, HttpStatus.OK);
    }

    @GetMapping("/ByKeyword")
    private ResponseEntity<?> findUsersByKeyword(String keyword){
        var foundUsers = userService.getUsersByKeyword(keyword);
        return new ResponseEntity<>(foundUsers, HttpStatus.OK);
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