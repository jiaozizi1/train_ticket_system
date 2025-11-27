package org.example.train_ticket_system.repository;

import org.example.train_ticket_system.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {
    // 根据车次号查询列车
    Optional<Train> findByTrainNo(String trainNo);
}