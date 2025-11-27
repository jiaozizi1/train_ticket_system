package org.example.train_ticket_system.repository;

import org.example.train_ticket_system.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// 用于操作Station实体，继承JpaRepository获得基础CRUD方法
public interface StationRepository extends JpaRepository<Station, Long> {
    // 继承JpaRepository的findById(Long id)方法，无需额外定义
    // 如需根据名称查询车站，可添加：
    Optional<Station> findByName(String name);
}