// src/main/java/org/example/train_ticket_system/repository/TrainScheduleRepository.java
package org.example.train_ticket_system.repository;

import org.example.train_ticket_system.entity.TrainSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TrainScheduleRepository extends JpaRepository<TrainSchedule, Long> {
    List<TrainSchedule> findByRunDate(LocalDate runDate);
}