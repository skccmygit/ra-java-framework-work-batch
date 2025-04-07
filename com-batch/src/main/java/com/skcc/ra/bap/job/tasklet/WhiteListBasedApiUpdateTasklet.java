package com.skcc.ra.bap.job.tasklet;

import com.skcc.ra.bap.domain.WhiteListBasedApi;
import com.skcc.ra.bap.repository.ApiAuthorizationInformationMapper;
import com.skcc.ra.bap.repository.WhiteListBasedApiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * WhiteListBasedApiUpdateTasklet.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-10, 최초 작성
 */
@Component
@Slf4j
public class WhiteListBasedApiUpdateTasklet implements Tasklet{

    @Autowired
    private ApiAuthorizationInformationMapper apiAuthorizationInformationMapper;

    @Autowired
    private WhiteListBasedApiRepository whiteListBasedApiRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        /*
            update all data invalid
         */
        log.info("[Step 1] set all redis data expired");
        List<WhiteListBasedApi> whiteListBasedApiList = (List<WhiteListBasedApi>) whiteListBasedApiRepository.findAll();
        List<WhiteListBasedApi> whiteListBasedApiListForSave = new ArrayList<>();
        if(!(whiteListBasedApiList == null || whiteListBasedApiList.isEmpty())) {
//            whiteListBasedApiList.forEach(
//                    i -> {
//                        i.setStatus("D");   // D : delete, I : insert
//                    }
//            );
            for(WhiteListBasedApi item : whiteListBasedApiList){

                if(item != null) {
                    item.setStatus("D");
                    whiteListBasedApiListForSave.add(item);
                }
            }
            if(!whiteListBasedApiListForSave.isEmpty()) {
                whiteListBasedApiRepository.saveAll(whiteListBasedApiListForSave);
            }
        }

        /*
            reader
         */
        log.info("[Step 2] read from db");
        List<Map<String, Object>> whiteListBasedApiUpdateList = apiAuthorizationInformationMapper.findWhiteListBasedApiUpdateList();

        /*
            each
         */
        if(!(whiteListBasedApiUpdateList == null || whiteListBasedApiUpdateList.isEmpty())) {
//            whiteListBasedApiUpdateList.forEach(
//                    i -> {
//                        log.debug("whitelist data : {}", i.toString());
//
//                        /*
//                            processor
//                         */
//                        WhiteListBasedApi entry = new WhiteListBasedApi();
//                        entry.setApiId((int) i.get("API_ID"));
//                        entry.setApiLocUrladdr((String) i.get("API_LOC_URLADDR"));
//                        entry.setHttMethodVal((String) i.get("HTT_METHOD_VAL"));
//                        entry.setStatus("I");
//
//                        /*
//                            writer
//                         */
//                        whiteListBasedApiRepository.save(entry);
//                    }
//            );

            for(Map<String, Object> item : whiteListBasedApiUpdateList){
                WhiteListBasedApi entry = new WhiteListBasedApi();
                entry.setApiId((int) item.get("API_ID"));
                entry.setApiLocUrladdr((String) item.get("API_LOC_URLADDR"));
                entry.setHttMethodVal((String) item.get("HTT_METHOD_VAL"));
                entry.setStatus("I");

                /*
                    writer
                 */
                whiteListBasedApiRepository.save(entry);
            }
        }

        /*
            delete expired data
         */
        log.info("[Step 3] delete expired redis data");
        whiteListBasedApiRepository.deleteAll(whiteListBasedApiRepository.findAllByStatus("D"));


        return RepeatStatus.FINISHED;
    }

}
