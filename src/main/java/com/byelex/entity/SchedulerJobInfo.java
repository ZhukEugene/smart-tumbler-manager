package com.byelex.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@ToString
@Getter
@Setter
@Entity
@Table(name = "scheduler_job_info")
public class SchedulerJobInfo {

    private static int ID = 0;

    public SchedulerJobInfo() {
        this.jobId = ID++;
    }

    @Id
    private int jobId;
    private LocalDate jobDate;
    private LocalTime jobTime;
    private String jobDeviceId;
    private Action.ActionType jobActionType;

}
