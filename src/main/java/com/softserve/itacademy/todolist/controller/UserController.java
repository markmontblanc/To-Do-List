package com.softserve.itacademy.todolist.controller;

import com.softserve.itacademy.todolist.dto.UserResponse;
import com.softserve.itacademy.todolist.model.User;
import com.softserve.itacademy.todolist.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController @Slf4j
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/{u_id}")
    UserResponse getUserById(@PathVariable Long u_id){
        log.info("Get mapping for getUserById");
        try{
            return new UserResponse(userService.readById(u_id));
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Failed to get User by Id ", e);
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    UserResponse createUser(@RequestBody @Validated User user){
        log.info("PostMapping for createUser");
        try{
            UserResponse userResponse = new UserResponse(user);
            userService.create(user);
            return userResponse;
        }
        catch (Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Failed to create User ", e);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasAnyRole('ROLE_ADMIN')")
    UserResponse updateUser(@PathVariable("id") Long userId, @RequestBody User userToUpdate){
        User user=userService.readById(userId);
        user.setFirstName(userToUpdate.getFirstName());
        user.setLastName(userToUpdate.getLastName());
        userService.update(user);

        return new UserResponse(userService.update(user));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<User>deleteUser(@PathVariable Long id){
        User existingUser = userService.readById(id);
        if(existingUser != null) {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER') or hasAnyRole('ROLE_ADMIN')")
    List<UserResponse> getAll() {
        log.info("Info about list of users received");
        return userService.getAll().stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());
    }
}
