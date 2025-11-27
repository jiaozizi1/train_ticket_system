package org.example.train_ticket_system.controller;

import org.example.train_ticket_system.entity.TicketRemain;
import org.example.train_ticket_system.entity.TicketRemainPK;
import org.example.train_ticket_system.entity.TrainSchedule;
import org.example.train_ticket_system.repository.StationRepository;
import org.example.train_ticket_system.repository.TicketRemainRepository;
import org.example.train_ticket_system.repository.TrainScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TrainScheduleRepository trainScheduleRepository;

    @Autowired
    private TicketRemainRepository ticketRemainRepository;

    // 新增：注入车站仓库（解决车站信息查询问题）
    @Autowired
    private StationRepository stationRepository;  // 确保StationRepository存在

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/train/list")
    public String trainList(Model model) {
        model.addAttribute("trains", trainScheduleRepository.findAll());
        return "admin/train/list";
    }

    @GetMapping("/train/add")
    public String addForm() {
        return "admin/train/add";
    }

    // 修正：添加车站校验+余票主键动态生成
    @PostMapping("/train/add")
    public String addTrain(
            @RequestParam String trainNo,
            @RequestParam String from,  // 出发站名称
            @RequestParam String to,    // 到达站名称
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam String runDate,
            RedirectAttributes redirectAttributes) {  // 新增重定向参数，用于错误提示

        // 1. 校验车站是否存在（解决硬编码1L/2L导致的错误）
        // 假设Station实体有name字段，且StationRepository有findByName方法
        Optional<org.example.train_ticket_system.entity.Station> fromStation = stationRepository.findByName(from);
        Optional<org.example.train_ticket_system.entity.Station> toStation = stationRepository.findByName(to);
        if (fromStation.isEmpty() || toStation.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "出发站或到达站不存在");
            return "redirect:/admin/train/add";
        }

        // 2. 保存列车时刻
        TrainSchedule schedule = new TrainSchedule();
        schedule.setTrainNo(trainNo);
        schedule.setFromStation(from);  // 与表单参数一致
        schedule.setToStation(to);
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setRunDate(LocalDate.parse(runDate));  // 确保前端传入yyyy-MM-dd格式
        trainScheduleRepository.save(schedule);

        // 3. 生成余票（使用真实车站ID）
        long fromId = fromStation.get().getId();
        long toId = toStation.get().getId();
        long[] seatTypeIds = {1L, 2L, 3L};
        int[] remains = {50, 100, 200};

        for (int i = 0; i < 3; i++) {
            TicketRemainPK pk = new TicketRemainPK();
            pk.setTrainNo(trainNo);
            pk.setRunDate(runDate);  // 与TrainSchedule的runDate格式一致
            pk.setFromStationId(fromId);  // 动态获取车站ID
            pk.setToStationId(toId);
            pk.setSeatTypeId(seatTypeIds[i]);

            // 避免重复添加
            if (!ticketRemainRepository.existsById(pk)) {
                TicketRemain remain = new TicketRemain();
                // 必须设置复合主键
                remain.setId(pk);  // 假设TicketRemain有setId方法接收TicketRemainPK
                remain.setRemain(remains[i]);
                ticketRemainRepository.save(remain);
            }
        }

        return "redirect:/admin/train/list";
    }
}