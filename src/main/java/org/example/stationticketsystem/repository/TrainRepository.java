package org.example.stationticketsystem.repository;

import org.example.stationticketsystem.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.LockModeType; // 确保导入这个
import java.util.List;
import java.util.Optional;

public interface TrainRepository extends JpaRepository<Train, Long> {
    Optional<Train> findByTrainNumber(String trainNumber);
    List<Train> findByStartStationAndEndStation(String start, String end);

    // 带悲观锁的查询
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Train t WHERE t.id = :id")
    Optional<Train> findByIdWithLock(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Train t SET t.availableSeats = t.availableSeats - 1 WHERE t.id = :trainId AND t.availableSeats > 0")
    int decreaseAvailableSeats(@Param("trainId") Long trainId);

    @Transactional
    @Modifying
    @Query("UPDATE Train t SET t.availableSeats = t.availableSeats + 1 WHERE t.id = :trainId")
    int increaseAvailableSeats(@Param("trainId") Long trainId);
}