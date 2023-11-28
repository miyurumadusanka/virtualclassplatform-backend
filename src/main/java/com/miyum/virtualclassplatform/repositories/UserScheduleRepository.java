package com.miyum.virtualclassplatform.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.miyum.virtualclassplatform.models.UserSchedule;

@Repository
public interface UserScheduleRepository extends MongoRepository<UserSchedule, String> {
    public List<UserSchedule> findByStudentId(String studentId);
    public Optional<UserSchedule> findByStudentIdAndScheduleId(String studentId, String scheduleId);
    public List<UserSchedule> findByScheduleId(String scheduleId);
    public void deleteByScheduleId(String scheduleId);

    /**This method show error
        public List<UserSchedule> findByScheduleIdAndScheduleDateAfter(String scheduleId, String date);
    **/
}
