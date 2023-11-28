package com.miyum.virtualclassplatform.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.miyum.virtualclassplatform.models.ERole;
import com.miyum.virtualclassplatform.models.User;
import com.miyum.virtualclassplatform.models.UserProfile;
import com.miyum.virtualclassplatform.payload.request.ProfileRequest;
import com.miyum.virtualclassplatform.repositories.UserProfileRespository;
import com.miyum.virtualclassplatform.repositories.UserRepository;
import com.miyum.virtualclassplatform.services.UserProfileService;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    UserProfileRespository profileRespository;

    @Autowired
    UserRepository userRepository;

    boolean isStudent = true;

    @Override
    public boolean createProfile(ProfileRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("Error: Invalid token"));
        UserProfile profileExist = profileRespository.findByUserId(user.getId()).orElse(null);
        // UserProfile lastProfile = profileRespository.findFirstByOrderByIdDesc().orElse(null);

        user.getRoles().forEach(role -> {
            if(role.getName() == ERole.ROLE_TEACHER) {
                isStudent = false;
            }
        });

        UserProfile lastUserProfile = profileRespository.findFirstByIsStudentOrderByIdDesc(isStudent).orElse(null);

        UserProfile profile = new UserProfile();
        if (profileExist != null) {
            profile.setId(profileExist.getId());
        }
        profile.setFullName(request.getName());
        profile.setPhoneNumber(request.getPhoneNum());
        profile.setAddress(request.getAddress());
        profile.setGrade(request.getGrade());
        profile.setUserId(user.getId());
        profile.setStudent(isStudent);

        if(profileExist == null) {
            if(lastUserProfile != null) {
                if(isStudent) {
                    String[] splitUserCode = lastUserProfile.getUserCode().split("U");
                    profile.setUserCode(String.format("STU%06d", Integer.parseInt(splitUserCode[1])+1));
                } else {
                    String[] splitUserCode = lastUserProfile.getUserCode().split("H");
                    profile.setUserCode(String.format("TECH%06d", Integer.parseInt(splitUserCode[1])+1));
                }

            } else {
                if (isStudent) {
                    profile.setUserCode(String.format("STU%06d", 1));
                } else {  
                    profile.setUserCode(String.format("TECH%06d", 1));
                }
            }
        } else {
            profile.setUserCode(profileExist.getUserCode());
        }

        profileRespository.save(profile);
        return true;
    }

    @Override
    public UserProfile getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("Error: Invalid token"));
        UserProfile userProfile = profileRespository.findByUserId(user.getId()).orElse(null);
        return userProfile;
    }

    @Override
    public List<UserProfile> searchStudents(String search) {
        List<UserProfile> searchedStudents = profileRespository.findByFullName(search);
        return searchedStudents;
    }
    
}
