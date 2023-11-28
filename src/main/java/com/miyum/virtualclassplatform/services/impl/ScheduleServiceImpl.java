package com.miyum.virtualclassplatform.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.miyum.virtualclassplatform.models.Schedule;
import com.miyum.virtualclassplatform.models.User;
import com.miyum.virtualclassplatform.models.UserProfile;
import com.miyum.virtualclassplatform.models.UserSchedule;
import com.miyum.virtualclassplatform.payload.request.CreateScheduleRequest;
import com.miyum.virtualclassplatform.payload.response.ProfileWithAttendance;
import com.miyum.virtualclassplatform.payload.response.ScheduleResponse;
import com.miyum.virtualclassplatform.repositories.ScheduleRepository;
import com.miyum.virtualclassplatform.repositories.UserProfileRespository;
import com.miyum.virtualclassplatform.repositories.UserRepository;
import com.miyum.virtualclassplatform.repositories.UserScheduleRepository;
import com.miyum.virtualclassplatform.services.ScheduleService;
import com.miyum.virtualclassplatform.utils.Utility;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    UserScheduleRepository userScheduleRepository;
    
    @Autowired
    UserProfileRespository profileRespository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public boolean createSchedule(CreateScheduleRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User teacher = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("Error: Invalid token"));

        Schedule schedule = new Schedule();
        schedule.setName(request.getName());
        schedule.setDescription(request.getDescription());
        schedule.setDate(request.getDate());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setTeacher(teacher);
        schedule.setMeetingId(Utility.getInstance().getUUID());
        schedule.setCreatedAt(Utility.getInstance().getDateTime(Calendar.getInstance().getTime()));
        schedule.setUpdatedAt(Utility.getInstance().getDateTime(Calendar.getInstance().getTime()));

        Schedule savedSchedule = scheduleRepository.save(schedule);

        if (request.getStudentIds() != null && request.getStudentIds().isEmpty()) {
            Iterable<User> studentsIterable = userRepository.findAllById(request.getStudentIds());
            List<UserSchedule> userSchedules = new ArrayList<>();
            studentsIterable.forEach(student -> {
                UserSchedule userSchedule = new UserSchedule();
                userSchedule.setStudent(student);
                userSchedule.setSchedule(savedSchedule);
                userSchedules.add(userSchedule);
            });
            userScheduleRepository.saveAll(userSchedules);
        }
        return true;
    }

    @Override
    public ScheduleResponse getTeacherSchedules() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User teacher = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("Error: Invalid token"));
        List<Schedule> todaySchedules = scheduleRepository.findByTeacherIdAndDate(teacher.getId(), Utility.getInstance().getDate(Calendar.getInstance().getTime()));
        List<Schedule> upcomingSchedules = scheduleRepository.findByTeacherIdAndDateAfter(teacher.getId(), Utility.getInstance().getDate(Calendar.getInstance().getTime()));
        ScheduleResponse scheduleResponse = new ScheduleResponse();
        scheduleResponse.setTodaySchedules(todaySchedules);
        scheduleResponse.setUpcomingSchedules(upcomingSchedules);
        return scheduleResponse;
    }

    @Override
    public ScheduleResponse getStudentSchedules() {

        ScheduleResponse scheduleResponse = new ScheduleResponse();
        List<Schedule> todaySchedules = new ArrayList<>();
        List<Schedule> upComingSchedules = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User student = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("Error: Invalid token"));

        List<String> todayScheduleIds = mongoTemplate.find(
                Query.query(Criteria.where("date").is(Utility.getInstance().getDate(Calendar.getInstance().getTime()))),
                Schedule.class
        ).stream().map(Schedule::getId).collect(Collectors.toList());

        List<UserSchedule> todayUserSchedules = mongoTemplate.find(
                Query.query(
                        Criteria.where("student.id").is(student.getId())
                                .and("schedule").in(todayScheduleIds)
                ),
                UserSchedule.class
        );

        todayUserSchedules.forEach(tus -> {
            todaySchedules.add(tus.getSchedule());
        });

        List<String> upcomingScheduleIds = mongoTemplate.find(
                Query.query(Criteria.where("date").gt(Utility.getInstance().getDate(Calendar.getInstance().getTime()))),
                Schedule.class
        ).stream().map(Schedule::getId).collect(Collectors.toList());

        List<UserSchedule> upComingUserSchedules = mongoTemplate.find(
                Query.query(
                        Criteria.where("student.id").is(student.getId())
                                .and("schedule").in(upcomingScheduleIds)
                ),
                UserSchedule.class
        );

        upComingUserSchedules.forEach(ucs -> {
            upComingSchedules.add(ucs.getSchedule());
        });

        scheduleResponse.setTodaySchedules(todaySchedules);
        scheduleResponse.setUpcomingSchedules(upComingSchedules);

        return scheduleResponse;
    }

    @Override
    public void createAttendance(String scheduleId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User student = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("Error: Invalid token"));
        UserSchedule userSchedule = userScheduleRepository.findByStudentIdAndScheduleId(student.getId(), scheduleId).orElse(null);
        if (userSchedule != null) {
            userSchedule.setAttendanceStatus(true);
            userScheduleRepository.save(userSchedule);
        }
    }

    @Override
    public List<ProfileWithAttendance> getScheduleStudents(String scheduleId) {
        List<ProfileWithAttendance> profileWithAttendances = new ArrayList<>();
        List<UserSchedule> userSchedules = userScheduleRepository.findByScheduleId(scheduleId);
        userSchedules.forEach(pwa -> {
            ProfileWithAttendance profileWithAttendance = new ProfileWithAttendance();
            UserProfile studentProfile = pwa.getStudentProfile();
            profileWithAttendance.setId(studentProfile.getId());
            profileWithAttendance.setFullName(studentProfile.getFullName());
            profileWithAttendance.setAddress(studentProfile.getAddress());
            profileWithAttendance.setGrade(studentProfile.getGrade());
            profileWithAttendance.setPhoneNumber(studentProfile.getPhoneNumber());
            profileWithAttendance.setStudent(true);
            profileWithAttendance.setUserCode(studentProfile.getUserCode());
            profileWithAttendance.setUserId(studentProfile.getUserId());
            profileWithAttendance.setAttendance(pwa.isAttendanceStatus());
            profileWithAttendances.add(profileWithAttendance);
        });
        
        return profileWithAttendances;
    }

    @Override
    public List<UserProfile> getScheduleStudentsOther(String scheduleId) {
        List<UserSchedule> userSchedules = userScheduleRepository.findByScheduleId(scheduleId);
        List<String> ids = new ArrayList<>();
        userSchedules.forEach(us -> {
            User student = us.getStudent();
            ids.add(student.getId());
        });
        
        List<UserProfile> profiles = profileRespository.findByProfilesIn(ids);
        return profiles;
    }

    @Override
    public UserProfile addScheduleStudent(String scheduleId, String studentCode) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElse(null);
        if(schedule == null) {
            return null;
        }
        UserProfile profile = profileRespository.findByUserCode(studentCode).orElse(null);
        if(profile == null) {
            return null;
        }
        User user = userRepository.findById(profile.getUserId()).orElse(null);
        if(user == null) {
            return null;
        }
        UserSchedule userSchedule = new UserSchedule();
        userSchedule.setSchedule(schedule);
        userSchedule.setStudent(user);
        userSchedule.setAttendanceStatus(false);
        userSchedule.setStudentProfile(profile);
        userScheduleRepository.save(userSchedule);
        return profile;
    }

    @Override
    public boolean removeScheduleStudent(String scheduleId, String studentCode) {
        UserProfile profile = profileRespository.findByUserCode(studentCode).orElse(null);
        if(profile == null) {
            return false;
        }
        User user = userRepository.findById(profile.getUserId()).orElse(null);
        if(user == null) {
            return false;
        }

        UserSchedule userSchedule = userScheduleRepository.findByStudentIdAndScheduleId(user.getId(), scheduleId).orElse(null);
        if(userSchedule == null) {
            return false;
        }
        userScheduleRepository.delete(userSchedule);

        return true;
    }

    @Override
    public List<Schedule> getTeacherScheduleHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User teacher = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("Error: Invalid token"));
        List<Schedule> previousSchedules = scheduleRepository.findByTeacherIdAndDateBefore(teacher.getId(), Utility.getInstance().getDate(Calendar.getInstance().getTime()));
        return previousSchedules;
    }

    @Override
    public List<Schedule> getStudentScheduleHistory() {

        List<Schedule> schedules = new ArrayList<>();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User student = userRepository.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("Error: Invalid token"));

        List<String> upcomingScheduleIds = mongoTemplate.find(
                Query.query(Criteria.where("date").lt(Utility.getInstance().getDate(Calendar.getInstance().getTime()))),
                Schedule.class
        ).stream().map(Schedule::getId).collect(Collectors.toList());

        List<UserSchedule> upComingUserSchedules = mongoTemplate.find(
                Query.query(
                        Criteria.where("student.id").is(student.getId())
                                .and("schedule").in(upcomingScheduleIds)
                ),
                UserSchedule.class
        );

        upComingUserSchedules.forEach(ucs -> {
            schedules.add(ucs.getSchedule());
        });

        return schedules;
    }

    @Override
    public boolean deleteSchedule(String scheduleId) {
        scheduleRepository.deleteById(scheduleId);
        userScheduleRepository.deleteByScheduleId(scheduleId);
        return true;
    }

    @Override
    public boolean updateSchedule(CreateScheduleRequest request) {
        Schedule schedule = scheduleRepository.findById(request.getId()).orElse(null);
        if (schedule != null) {
            schedule.setName(request.getName());
            schedule.setDescription(request.getDescription());
            schedule.setDate(request.getDate());
            schedule.setStartTime(request.getStartTime());
            schedule.setEndTime(request.getEndTime());
            schedule.setUpdatedAt(Utility.getInstance().getDateTime(Calendar.getInstance().getTime()));

            Schedule savedSchedule = scheduleRepository.save(schedule);
            if(savedSchedule != null) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
}
