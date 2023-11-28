package com.miyum.virtualclassplatform.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.miyum.virtualclassplatform.models.Schedule;
import com.miyum.virtualclassplatform.models.UserProfile;
import com.miyum.virtualclassplatform.payload.request.CreateScheduleRequest;
import com.miyum.virtualclassplatform.payload.response.HttpBaseResponse;
import com.miyum.virtualclassplatform.payload.response.MessageResponse;
import com.miyum.virtualclassplatform.payload.response.ProfileWithAttendance;
import com.miyum.virtualclassplatform.payload.response.ScheduleResponse;
import com.miyum.virtualclassplatform.repositories.UserScheduleRepository;
import com.miyum.virtualclassplatform.services.ScheduleService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

	@Autowired
	ScheduleService scheduleService;
	
	@Autowired
	UserScheduleRepository scheduleRepository;

	@PostMapping("/create")
	public ResponseEntity<?> createSchedule(@RequestBody CreateScheduleRequest scheduleRequest) {
		boolean createScheduleResponse = scheduleService.createSchedule(scheduleRequest);
		if(createScheduleResponse) {
			return ResponseEntity.status(HttpStatus.CREATED).body(new HttpBaseResponse(createScheduleResponse, null));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HttpBaseResponse(createScheduleResponse, false));
		}
	}

	@GetMapping("/get-all-teacher-schedules")
	public ResponseEntity<?> getTeacherSchedules() {
		ScheduleResponse scheduleResponse = scheduleService.getTeacherSchedules();
		return ResponseEntity.status(HttpStatus.OK).body(new HttpBaseResponse(true, scheduleResponse));
	}

	@GetMapping("/get-schedule-history")
	public ResponseEntity<?> getTeacherScheduleHistory() {
		List<Schedule> scheduleHistory = scheduleService.getTeacherScheduleHistory();
		return ResponseEntity.status(HttpStatus.OK).body(new HttpBaseResponse(true, scheduleHistory));
	}

	@GetMapping("/get-all-student-schedules")
	public ResponseEntity<?> getStudentSchedules() {
		ScheduleResponse studentSchedules = scheduleService.getStudentSchedules();
		return ResponseEntity.status(HttpStatus.OK).body(new HttpBaseResponse(true, studentSchedules));
	}

	@GetMapping("/get-student-schedules-history")
	public ResponseEntity<?> getStudentScheduleHistory() {
		List<Schedule> scheduleHistory = scheduleService.getStudentScheduleHistory();
		return ResponseEntity.status(HttpStatus.OK).body(new HttpBaseResponse(true, scheduleHistory));
	}

	@GetMapping("/create-attendance")
	public ResponseEntity<?> createAttendanceForSchedule(@RequestParam(name = "schedule_id") String scheduleId) {
		scheduleService.createAttendance(scheduleId);
		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse("Marked attendance"));
	}

	@GetMapping("/get-all-students")
	public ResponseEntity<?> getScheduleStudents(@RequestParam(name = "schedule_id") String scheduleId) {
		List<ProfileWithAttendance> userProfiles = scheduleService.getScheduleStudents(scheduleId);
		return ResponseEntity.status(HttpStatus.OK).body(new HttpBaseResponse(true, userProfiles));
	}

	@PostMapping("/add_schedule_student")
	public ResponseEntity<?> addScheduleStudent(@RequestParam(name = "schedule_id") String scheduleId, @RequestParam(name = "student_code") String studentCode) {
		UserProfile profile = scheduleService.addScheduleStudent(scheduleId, studentCode);
		if(profile != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new HttpBaseResponse(true, profile));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HttpBaseResponse(false, null));
		}
	}

	@DeleteMapping("/remove_schedule_student")
	public ResponseEntity<?> removeScheduleStudent(@RequestParam(name = "schedule_id") String scheduleId, @RequestParam(name = "student_code") String studentCode) {
		boolean responseStatus = scheduleService.removeScheduleStudent(scheduleId, studentCode);
		if(responseStatus) {
			return ResponseEntity.status(HttpStatus.OK).body(new HttpBaseResponse(true, null));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HttpBaseResponse(false, null));
		}
	}

	@DeleteMapping("/remove_schedule")
	public ResponseEntity<?> deleteSchedule(@RequestParam(name = "schedule_id") String scheduleId) {
		boolean stauts = scheduleService.deleteSchedule(scheduleId);
		return ResponseEntity.status(HttpStatus.OK).body(new HttpBaseResponse(stauts, scheduleId));
	}

	@PutMapping("/update")
	public ResponseEntity<?> updateSchedule(@RequestBody CreateScheduleRequest scheduleRequest) {
		boolean createScheduleResponse = scheduleService.updateSchedule(scheduleRequest);
		if(createScheduleResponse) {
			return ResponseEntity.status(HttpStatus.CREATED).body(new HttpBaseResponse(createScheduleResponse, null));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new HttpBaseResponse(createScheduleResponse, false));
		}
	}
}
