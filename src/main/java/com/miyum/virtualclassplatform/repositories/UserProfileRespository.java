package com.miyum.virtualclassplatform.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.miyum.virtualclassplatform.models.UserProfile;

@Repository
public interface UserProfileRespository extends MongoRepository<UserProfile, String> {
    public Optional<UserProfile> findByUserId(String userId);
    public List<UserProfile> findByFullName(String search);
    public Optional<UserProfile> findFirstByOrderByIdDesc();
    public Optional<UserProfile> findFirstByIsStudentOrderByIdDesc(boolean isStudent);
    @Query("{ 'user_id': { $in: ?0 } }")
    List<UserProfile> findByProfilesIn(List<String> ids);
    public Optional<UserProfile> findByUserCode(String userCode);
}
