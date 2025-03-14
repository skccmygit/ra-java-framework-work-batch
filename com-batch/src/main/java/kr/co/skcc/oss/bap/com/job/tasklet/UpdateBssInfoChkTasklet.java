package kr.co.skcc.oss.bap.com.job.tasklet;

import kr.co.skcc.oss.bap.com.repository.BssmacdRepository;
import kr.co.skcc.oss.bap.com.repository.DeptRepository;
import kr.co.skcc.oss.bap.com.repository.UserBasicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class UpdateBssInfoChkTasklet implements Tasklet {

    private String gbn;
    final private double threshold = 0.95;

    private UserBasicRepository userBasicRepository;

    private BssmacdRepository bssmacdRepository;

    private DeptRepository deptRepository;


    public UpdateBssInfoChkTasklet(String gbn,
                            UserBasicRepository userBasicRepository,
                            BssmacdRepository bssmacdRepository,
                            DeptRepository deptRepository) {
        this.gbn = gbn;
        this.userBasicRepository = userBasicRepository;
        this.bssmacdRepository = bssmacdRepository;
        this.deptRepository = deptRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        String today= LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        log.debug("gbn : " + gbn);

        switch(gbn) {
            case "userBasic":
                if(!checkUserBasic(today))   contribution.setExitStatus(ExitStatus.FAILED);
                break;
            case "bssmacd":
                if(!checkBssmacd(today))    contribution.setExitStatus(ExitStatus.FAILED);
                break;
            case "dept":
                if(!checkDept(today))        contribution.setExitStatus(ExitStatus.FAILED);
                break;
        }

        log.debug(" *** contribution : " + contribution.getExitStatus());

        return RepeatStatus.FINISHED;
    }

    public boolean checkUserBasic(String today){

        Long all = userBasicRepository.count();
        Long todayCnt = userBasicRepository.countTodayUpdate(today);

        log.info(" ** checkUserBasic Check Start ");
        return checkCnt(all, todayCnt);
    }
    public boolean checkBssmacd(String today){

        Long all = bssmacdRepository.count();
        Long todayCnt = bssmacdRepository.countTodayUpdate(today);

        log.info(" ** checkBssmacd Check Start ");
        return checkCnt(all, todayCnt);

    }
    public boolean checkDept(String today){

        Long all = deptRepository.count();
        Long todayCnt = deptRepository.countTodayUpdate(today);

        log.info(" ** checkDept Check Start ");
        return checkCnt(all, todayCnt);

    }
    public boolean checkCnt(Long all, Long todayCnt){

        log.info(" Percent : {} ", (double) todayCnt / all);

        if(todayCnt > 0 && all > 0){
            if((double) todayCnt / all >= threshold){
                return true;
            }
        }
        return false;
    }
}
