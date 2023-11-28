package com.miyum.virtualclassplatform.services;

import java.util.List;

import com.miyum.virtualclassplatform.models.UserProfile;
import com.miyum.virtualclassplatform.payload.request.ProfileRequest;

public interface UserProfileService {
    public boolean createProfile(ProfileRequest request);
    public UserProfile getProfile();
    public List<UserProfile> searchStudents(String search);
}
