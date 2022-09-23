package com.byelex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.byelex.entity.SchedulerJobInfo;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface SchedulerRepository extends JpaRepository<SchedulerJobInfo, Long> {

	List<SchedulerJobInfo> findAllByJobDateAndJobDeviceId(LocalDate jobDate, String jobDeviceId);
	SchedulerJobInfo findByJobDateAndJobDeviceId(LocalDate jobDate, String jobDeviceId);

}
