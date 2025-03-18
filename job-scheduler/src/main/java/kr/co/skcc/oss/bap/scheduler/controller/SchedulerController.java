package kr.co.skcc.oss.bap.scheduler.controller;

import kr.co.skcc.oss.bap.scheduler.controller.dto.SchedulerJobDto;
import kr.co.skcc.oss.bap.scheduler.domain.SchedulerJobInfo;
import kr.co.skcc.oss.bap.scheduler.service.SchedulerJobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/job-scheduler")
@Slf4j
public class SchedulerController {

    @Autowired
    private SchedulerJobService scheduleJobService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Ping Pong - Job Scheduler ...";
    }

    @RequestMapping(value = "/saveJob", method = RequestMethod.POST)
    public Object saveJob(@RequestBody SchedulerJobDto job) {
        log.info("job={}", job);
        ApiResponse apiResponse = ApiResponse.failure();
        try {
            scheduleJobService.saveOrUpdate(job.toEntity());
            apiResponse = ApiResponse.success();
        } catch (Exception e) {
            apiResponse.setMessage(e.getMessage());
            log.error("saveJob error:", e);
        }
        return apiResponse;
    }

    @RequestMapping("/metaData")
    public Object metaData() throws SchedulerException {
        SchedulerMetaData metaData = scheduleJobService.getMetaData();
        return metaData;
    }

    @RequestMapping("/getAllJobs")
    public List<SchedulerJobInfo> getAllJobs() {
        return scheduleJobService.getAllJobs();
    }

    @RequestMapping("/getJobInfo")
    public SchedulerJobInfo getJobInfo(@RequestBody SchedulerJobDto job) {
        return scheduleJobService.getJobInfo(job.toEntity());
    }

    @RequestMapping(value = "/startJob", method = RequestMethod.POST)
    public ApiResponse startJob(@RequestBody SchedulerJobDto job) {
        log.info("job={}", job);
        ApiResponse apiResponse = ApiResponse.failure();
        try {
            scheduleJobService.startJob(job.toEntity());
            apiResponse = ApiResponse.success();
        } catch (Exception e) {
            apiResponse.setMessage(e.getMessage());
            log.error("runJob error:", e.getMessage());
        }
        return apiResponse;
    }

    @RequestMapping(value = "/pauseJob", method = RequestMethod.POST)
    public ApiResponse pauseJob(@RequestBody SchedulerJobDto job) {
        log.info("job={}", job);
        ApiResponse apiResponse = ApiResponse.failure();
        try {
            scheduleJobService.pauseJob(job.toEntity());
            apiResponse = ApiResponse.success();
        } catch (Exception e) {
            apiResponse.setMessage(e.getMessage());
            log.error("pauseJob error:", e);
        }
        return apiResponse;
    }

    @RequestMapping(value = "/resumeJob", method = RequestMethod.POST)
    public ApiResponse resumeJob(@RequestBody SchedulerJobDto job) {
        log.info("job={}", job);
        ApiResponse apiResponse = ApiResponse.failure();
        try {
            scheduleJobService.resumeJob(job.toEntity());
            apiResponse = ApiResponse.success();
        } catch (Exception e) {
            apiResponse.setMessage(e.getMessage());
            log.error("resumeJob error:", e);
        }
        return apiResponse;
    }

    @RequestMapping(value = "/deleteJob", method = RequestMethod.POST)
    public ApiResponse deleteJob(@RequestBody SchedulerJobDto job) {
        log.info("job={}", job);
        ApiResponse apiResponse = ApiResponse.failure();
        try {
            scheduleJobService.deleteJob(job.toEntity());
            apiResponse = ApiResponse.success();
        } catch (Exception e) {
            apiResponse.setMessage(e.getMessage());
            log.error("deleteJob error:", e);
        }
        return apiResponse;
    }

    @RequestMapping("/start")
    public ApiResponse start() throws SchedulerException {
        ApiResponse apiResponse = ApiResponse.failure();
        try {
            scheduleJobService.start();
            apiResponse = ApiResponse.success();
        } catch (Exception e) {
            apiResponse.setMessage(e.getMessage());
            log.error("start error:", e);
        }
        return apiResponse;
    }

    @RequestMapping("/shutdown")
    public ApiResponse shutdown(@RequestParam(value = "wait", required = false, defaultValue = "false") boolean wait) throws SchedulerException {
        ApiResponse apiResponse = ApiResponse.failure();
        try {
            scheduleJobService.shutdown(wait);
            apiResponse = ApiResponse.success();
        } catch (Exception e) {
            apiResponse.setMessage(e.getMessage());
            log.error("shutdown error:", e);
        }
        return apiResponse;
    }
}
