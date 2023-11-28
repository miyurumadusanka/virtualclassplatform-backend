package com.miyum.virtualclassplatform.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.miyum.virtualclassplatform.models.Schedule;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    public List<Schedule> findByTeacherId(String teacherId);
    public List<Schedule> findByTeacherIdAndDate(String teacherId, String date);
    public List<Schedule> findByTeacherIdAndDateAfter(String teacherId, String date);
    public List<Schedule> findByTeacherIdAndDateBefore(String teacherId, String date);
}
