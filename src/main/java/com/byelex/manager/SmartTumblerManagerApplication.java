package com.byelex.manager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SmartTumblerManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartTumblerManagerApplication.class, args);
	}

	ArrayList<SheduleUnit> shedule;
	TimerTask timerTask;
	Timer timer;

	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

	@PostMapping("/hello")
	public void createProfile(@RequestBody DeviceRequest[] request) {
		System.out.println(Arrays.toString(request));

		if (shedule == null) {
			shedule = new ArrayList<>(getSheduleFromRequest(request));
		} else {
			shedule.addAll(getSheduleFromRequest(request));
		}

		System.out.println(shedule.toString());
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				System.out.println(shedule.get(0).dateTime);
			}
		}, convertToDate(shedule.get(0).dateTime));
	}

	public ArrayList<SheduleUnit> getSheduleFromRequest(DeviceRequest[] request) {
		ArrayList<SheduleUnit> newShedule = new ArrayList<>();

		for (DeviceRequest unit : request) {
			for (Action action : unit.actions) {
				newShedule.add(new SheduleUnit(unit.deviceID,
						unit.date.atStartOfDay().plusHours(action.timeOn.getHour()).plusMinutes(action.timeOn.getMinute()).plusSeconds(action.timeOn.getSecond()),
						ActionType.turnOn));
				newShedule.add(new SheduleUnit(unit.deviceID,
						unit.date.atStartOfDay().plusHours(action.timeOff.getHour()).plusMinutes(action.timeOff.getMinute()).plusSeconds(action.timeOff.getSecond()),
						ActionType.turnOff));
			}
		}

		return newShedule;
	}

	public static class SheduleUnit {
		String deviceID;
		LocalDateTime dateTime;
		ActionType action;

		public SheduleUnit(String deviceID, LocalDateTime dateTime, ActionType action) {
			this.deviceID = deviceID;
			this.dateTime = dateTime;
			this.action = action;
		}

		@Override
		public String toString() {
			return "SheduleUnit{" +
					"deviceID='" + deviceID + '\'' +
					", dateTime=" + dateTime +
					", action=" + action +
					'}';
		}
	}

	enum ActionType {
		turnOn,
		turnOff
	}

	public static class DeviceRequest {
		String deviceID;
		LocalDate date;
		Action[] actions;

		public DeviceRequest(String deviceID, LocalDate date, Action[] actions) {
			this.deviceID = deviceID;
			this.date = date;
			this.actions = actions;
		}

		@Override
		public String toString() {
			return "DeviceRequest{" +
					"deviceID='" + deviceID + '\'' +
					", date=" + date +
					", actions=" + Arrays.toString(actions) +
					'}';
		}
	}

	public static class Action {
		LocalTime timeOn;
		LocalTime timeOff;

		public Action(LocalTime timeOn, LocalTime timeOff) {
			this.timeOn = timeOn;
			this.timeOff = timeOff;
		}

		@Override
		public String toString() {
			return "Action{" +
					"timeOn=" + timeOn +
					", timeOff=" + timeOff +
					'}';
		}
	}

	public Date convertToDate(LocalDateTime dateToConvert) {
		return java.sql.Timestamp.valueOf(dateToConvert);
	}


}