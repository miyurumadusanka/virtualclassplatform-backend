package com.miyum.virtualclassplatform.payload.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miyum.virtualclassplatform.models.Schedule;

public class ScheduleResponse {
    
    @JsonProperty("today_schedules")
    private List<Schedule> todaySchedules;
    @JsonProperty("upcoming_schedule")
    private List<Schedule> upcomingSchedules;
    
    public List<Schedule> getTodaySchedules() {
        return todaySchedules;
    }
    public void setTodaySchedules(List<Schedule> todaySchedules) {
        this.todaySchedules = todaySchedules;
    }
    public List<Schedule> getUpcomingSchedules() {
        return upcomingSchedules;
    }
    public void setUpcomingSchedules(List<Schedule> upcomingSchedules) {
        this.upcomingSchedules = upcomingSchedules;
    }
    
}
