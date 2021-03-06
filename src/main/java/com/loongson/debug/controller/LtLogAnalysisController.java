package com.loongson.debug.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.loongson.debug.dto.LtlogAnalysisDTO;
import com.loongson.debug.entity.LtLog;
import com.loongson.debug.entity.LtlogAnalysis;
import com.loongson.debug.entity.LtlogInstructionMap;
import com.loongson.debug.entity.TBAnalysis;
import com.loongson.debug.service.ILtLogAnalysisService;
import com.loongson.debug.service.ILtLogService;
import com.loongson.debug.service.ILtlogInstructionMapService;
import com.loongson.debug.service.ITbAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author liuxp
 * @since 2022-04-18
 */
@RestController
@CrossOrigin
@RequestMapping("/ltLogAnalysis")
public class LtLogAnalysisController {
    @Autowired
    ILtLogService ltLogService;

    @Autowired
    ILtLogAnalysisService ltLogAnalysisService;

    @Autowired
    ILtlogInstructionMapService ltlogInstructionMapService;
    @Autowired
    ITbAnalysisService tbAnalysisService;

    @GetMapping("/getAll")
    public HashMap<String, Object> getLtlogAnalyse(int currentPage, int limit) {
        IPage<LtlogAnalysis> ltlogAnalysisIPage = ltLogAnalysisService.selectByPage(currentPage, limit);
        List<LtlogAnalysis> ltlogAnalyses = ltlogAnalysisIPage.getRecords();
        ArrayList<Integer> ids = new ArrayList<>();
        for (LtlogAnalysis ltlogAnalysis : ltlogAnalyses) {
            ids.add(ltlogAnalysis.getLtid());
        }


        List<LtLog> ltLogs = ltLogService.listByIds(ids);
        HashMap<Integer,LtLog> map = new HashMap<>();
        for (LtLog ltLog : ltLogs) {
            map.put(ltLog.getUid(),ltLog);
        }

        List<LtlogAnalysisDTO> ltlogAnalysisDTOS = new ArrayList<>();

        for (LtlogAnalysis ltlogAnalysis : ltlogAnalyses) {
            ltlogAnalysisDTOS.add(new LtlogAnalysisDTO(ltlogAnalysis,map.get(ltlogAnalysis.getLtid())));
        }


        long pages = ltlogAnalysisIPage.getPages();

        HashMap<String, Object> returnRes = new HashMap<>();

        returnRes.put("records", ltlogAnalysisDTOS);
        returnRes.put("total", ltlogAnalysisIPage.getTotal());
        returnRes.put("pages", pages);
        return returnRes;
    }

    @GetMapping("/getOne")
    public HashMap<String, Object> getLtlogAnalysis(int ltid) {
        LtlogAnalysis ltlogAnalysis = ltLogAnalysisService.getById(ltid);
        HashMap<String, Object> returnRes = new HashMap<>();
        returnRes.put("ltlog", ltlogAnalysis);
        return returnRes;
    }
}
