package org.example.expert.domain.user.controller;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.user.dto.request.UserChangePasswordRequest;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/users")
    public void changePassword(@AuthenticationPrincipal AuthUser authUser, @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser.getUserId(), userChangePasswordRequest);
    }

    /**
     * 쿼리 메서드 사용해서 유저 닉네임 검색
     */
    @GetMapping("/users/search")
    public ResponseEntity<UserResponse> getUserByNickName(@RequestParam String nickName) {
        return ResponseEntity.ok(userService.getUserByNickName(nickName));
    }

    @PostMapping(value = "/users/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> saveProfileImages(@AuthenticationPrincipal AuthUser authUser, @RequestParam(value = "image") MultipartFile image) throws IOException {
        userService.saveProfileImages(image, authUser);
        String res = "프로필 이미지 등록 완료";
        return ResponseEntity.ok(res);
    }

}
