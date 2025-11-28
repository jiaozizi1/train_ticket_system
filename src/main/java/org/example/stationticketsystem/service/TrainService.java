package org.example.stationticketsystem.service;

import java.math.BigDecimal;
import org.example.stationticketsystem.entity.Train;
import org.example.stationticketsystem.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainService {
    private final TrainRepository trainRepository;

    public Train addTrain(Train train) {
        // 验证逻辑保持不变
        if (train.getTrainNumber() == null || train.getTrainNumber().trim().isEmpty()) {
            throw new RuntimeException("车次编号不能为空");
        }
        if (train.getStartStation() == null || train.getStartStation().trim().isEmpty()) {
            throw new RuntimeException("出发站不能为空");
        }
        if (train.getEndStation() == null || train.getEndStation().trim().isEmpty()) {
            throw new RuntimeException("到达站不能为空");
        }
        if (train.getDepartureTime() == null || train.getArrivalTime() == null) {
            throw new RuntimeException("时间信息不能为空");
        }
        if (train.getPrice() == null || train.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("票价必须大于0");
        }
        if (train.getTotalSeats() == null || train.getTotalSeats() <= 0) {
            throw new RuntimeException("座位数必须大于0");
        }
        if (trainRepository.findByTrainNumber(train.getTrainNumber()).isPresent()) {
            throw new RuntimeException("车次编号已存在");
        }

        train.setAvailableSeats(train.getTotalSeats());
        return trainRepository.save(train);
    }

    public List<Train> findAllTrains() {
        return trainRepository.findAll();
    }

    public Optional<Train> findById(Long id) {
        return trainRepository.findById(id);
    }

    /**
     * 带锁查询（必须在事务中执行）
     */
    @Transactional(readOnly = true)
    public Optional<Train> findByIdWithLock(Long id) {
        return trainRepository.findByIdWithLock(id);
    }

    public List<Train> searchTrains(String start, String end) {
        return trainRepository.findByStartStationAndEndStation(start, end);
    }

    public void deleteTrain(Long id) {
        trainRepository.deleteById(id);
    }

    /**
     * 扣减余票（并发安全版）
     */
    @Transactional
    public boolean decreaseSeats(Long trainId) {
        // 带锁查询（已在事务中）
        Train train = findByIdWithLock(trainId)
                .orElseThrow(() -> new RuntimeException("车次不存在，ID：" + trainId));

        // 双重校验余票
        if (train.getAvailableSeats() == null || train.getAvailableSeats() <= 0) {
            return false;
        }

        // 执行余票扣减
        int affectedRows = trainRepository.decreaseAvailableSeats(trainId);
        return affectedRows > 0;
    }

    /**
     * 恢复余票（退票时使用）
     */
    @Transactional
    public void increaseSeats(Long trainId) {
        // 锁定记录后再更新
        findByIdWithLock(trainId)
                .orElseThrow(() -> new RuntimeException("车次不存在，ID：" + trainId));

        trainRepository.increaseAvailableSeats(trainId);
    }
}