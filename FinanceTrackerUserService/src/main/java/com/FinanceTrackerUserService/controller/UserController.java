package com.FinanceTrackerUserService.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.FinanceTrackerUserService.Entity.UserInfoDto;
import com.FinanceTrackerUserService.Service.UserService;

@RestController
@RequestMapping("/user/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // CREATE or UPDATE user info
    @PostMapping("/create-update")
    public ResponseEntity<UserInfoDto> createOrUpdate(
            @RequestBody UserInfoDto userInfoDto) {

        UserInfoDto user = userService.createOrUpdateUser(userInfoDto);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserInfoDto> getUser(@RequestBody UserInfoDto userInfoDto){
        try{
            UserInfoDto user = userService.getUser(userInfoDto);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
