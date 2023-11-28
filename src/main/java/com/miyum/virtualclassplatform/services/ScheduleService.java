package com.miyum.virtualclassplatform.services;

import java.util.List;

import com.miyum.virtualclassplatform.models.Schedule;
import com.miyum.virtualclassplatform.models.UserProfile;
import com.miyum.virtualclassplatform.payload.request.CreateScheduleRequest;
import com.miyum.virtualclassplatform.payload.response.ProfileWithAttendance;
import com.miyum.virtualclassplatform.payload.response.ScheduleResponse;

public interface ScheduleService {
    public boolean createSchedule(CreateScheduleRequest request);
    public boolean updateSchedule(CreateScheduleRequest request);
    public ScheduleResponse getTeacherSchedules();
    public List<Schedule> getTeacherScheduleHistory();
    public ScheduleResponse getStudentSchedules();
    public List<Schedule> getStudentScheduleHistory();
    public void createAttendance(String scheduleId);
    public List<ProfileWithAttendance> getScheduleStudents(String scheduleId);
    public List<UserProfile> getScheduleStudentsOther(String scheduleId);
    public UserProfile addScheduleStudent(String scheduleId, String studentCode);
    public boolean removeScheduleStudent(String scheduleId, String studentCode);
    public boolean deleteSchedule(String scheduleId);
}
