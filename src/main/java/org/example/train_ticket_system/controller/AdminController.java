package org.example.train_ticket_system.controller;

import org.example.train_ticket_system.entity.*;
import org.example.train_ticket_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TrainScheduleRepository trainScheduleRepository;     // ← 必须加

    @Autowired
    private TicketRemainRepository ticketRemainRepository;       // ← 必须加

    // 列车列表（显示管理员添加的所有车次）
    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";   // 对应 src/main/resources/templates/admin/dashboard.html
    }
    @GetMapping("/train/list")
    public String trainList(Model model) {
        model.addAttribute("trains", trainScheduleRepository.findAll());
        return "admin/train/list";
    }

    // 添加页面
    @GetMapping("/train/add")
    public String addForm() {
        return "admin/train/add";
    }

    // 终极添加方法（带日期 + 自动生成余票）
    @PostMapping("/train/add")
    public String addTrain(
            @RequestParam String trainNo,
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam String runDate) {

        // 1. 保存列车时刻
        TrainSchedule schedule = new TrainSchedule();
        schedule.setTrainNo(trainNo);
        schedule.setFromStation(from);
        schedule.setToStation(to);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setRunDate(LocalDate.parse(runDate));
        trainScheduleRepository.save(schedule);

        // 2. 自动生成三类席别余票
        long[] seatTypeIds = {1L, 2L, 3L};  // 1商务 2一等 3二等
        int[] remains = {50, 100, 200};

        for (int i = 0; i < 3; i++) {
            TicketRemainPK pk = new TicketRemainPK();
            pk.setTrainNo(trainNo);
            pk.setRunDate(runDate);
            pk.setFromStationId(1L);
            pk.setToStationId(2L);
            pk.setSeatTypeId(seatTypeIds[i]);

            if (!ticketRemainRepository.existsById(pk)) {
                TicketRemain r = new TicketRemain();
                r.setTrainNo(trainNo);
                r.setRunDate(runDate);
                r.setFromStationId(1L);
                r.setToStationId(2L);
                r.setSeatTypeId(seatTypeIds[i]);
                r.setRemain(remains[i]);
                ticketRemainRepository.save(r);
            }
        }
        return "redirect:/admin/train/list";
    }
}