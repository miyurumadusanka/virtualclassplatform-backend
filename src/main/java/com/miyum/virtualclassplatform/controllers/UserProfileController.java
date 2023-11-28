package com.miyum.virtualclassplatform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miyum.virtualclassplatform.models.UserProfile;
import com.miyum.virtualclassplatform.payload.request.ProfileRequest;
import com.miyum.virtualclassplatform.payload.response.HttpBaseResponse;
import com.miyum.virtualclassplatform.payload.response.MessageResponse;
import com.miyum.virtualclassplatform.services.UserProfileService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    UserProfileService profileService;

    @PostMapping("/create")
    public ResponseEntity<?> createProfile(@RequestBody ProfileRequest profileRequest) {
        boolean saveStatus = profileService.createProfile(profileRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new HttpBaseResponse(saveStatus, null));
    }

    @GetMapping("/get-profile")
    public ResponseEntity<?> getProfile() {
        UserProfile profile = profileService.getProfile();
        if (profile != null) {
            return ResponseEntity.status(HttpStatus.OK).body(profile);
        } else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new MessageResponse("Profile not found"));
        }
    }
}
