package com.miyum.virtualclassplatform.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.miyum.virtualclassplatform.models.ERole;
import com.miyum.virtualclassplatform.models.Role;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
