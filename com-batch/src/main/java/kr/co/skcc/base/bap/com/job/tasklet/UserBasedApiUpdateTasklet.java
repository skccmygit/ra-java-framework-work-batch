package kr.co.skcc.base.bap.com.job.tasklet;

import kr.co.skcc.base.bap.com.domain.UserBasedApi;
import kr.co.skcc.base.bap.com.repository.ApiAuthorizationInformationMapper;
import kr.co.skcc.base.bap.com.repository.UserBasedApiRepository;
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
 * UserBasedApiUpdateTasklet.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-10, 최초 작성
 */
@Component
@Slf4j
public class UserBasedApiUpdateTasklet implements Tasklet {

    @Autowired
    private ApiAuthorizationInformationMapper apiAuthorizationInformationMapper;

    @Autowired
    private UserBasedApiRepository userBasedApiRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        /*
            update all data invalid
         */
        log.info("[Step 1] set all redis data expired");
        List<UserBasedApi> userBasedApiList = (List<UserBasedApi>) userBasedApiRepository.findAll();
        List<UserBasedApi> userBasedApiListForSave = new ArrayList<>();
        if(!(userBasedApiList == null || userBasedApiList.isEmpty())) {
//            userBasedApiList.forEach(
//                    i -> {
//                        i.setStatus("D");
//                    }else{
//                    whiteListBasedApiList.remove(i);
//                }
//                i++;
//            );

            for(UserBasedApi item : userBasedApiList){
                if(item != null) {
                    item.setStatus("D");
                    userBasedApiListForSave.add(item);
                }
            }
            if(!userBasedApiListForSave.isEmpty()) {
                userBasedApiRepository.saveAll(userBasedApiList);
            }
        }
        /*
            reader
         */
        log.info("[Step 2] read from db");
        List<Map<String, Object>> userBasedApiUpdateList = apiAuthorizationInformationMapper.findUserBasedApiUpdateList();

        /*
            each
         */
        if(!(userBasedApiUpdateList == null || userBasedApiUpdateList.isEmpty())) {
//            userBasedApiUpdateList.forEach(
//                    i -> {
//                        log.debug("whitelist data : {}", i.toString());
//
//                        /*
//                            processor
//                         */
//                        UserBasedApi entry = new UserBasedApi();
//                        entry.setUserid((String) i.get("USERID"));
//                        entry.setScrenId((String) i.get("SCREN_ID"));
//                        entry.setBttnId((String) i.get("BTTN_ID"));
//                        entry.setApiId((int) i.get("API_ID"));
//                        entry.setApiLocUrladdr((String) i.get("API_LOC_URLADDR"));
//                        entry.setHttMethodVal((String) i.get("HTT_METHOD_VAL"));
//                        entry.setStatus("I");
//
//                        /*
//                            writer
//                         */
//                        userBasedApiRepository.save(entry);
//                    }
//            );
            for(Map<String, Object> item : userBasedApiUpdateList){

                UserBasedApi entry = new UserBasedApi();
                entry.setUserid((String) item.get("USERID"));
                entry.setScrenId((String) item.get("SCREN_ID"));
                entry.setBttnId((String) item.get("BTTN_ID"));
                entry.setApiId((int) item.get("API_ID"));
                entry.setApiLocUrladdr((String) item.get("API_LOC_URLADDR"));
                entry.setHttMethodVal((String) item.get("HTT_METHOD_VAL"));
                entry.setStatus("I");

                userBasedApiRepository.save(entry);
            }
        }

        /*
            delete expired data
         */
        //userBasedApiRepository.deleteAllByStatus("D");
        log.info("[Step 3] delete expired redis data");
        userBasedApiRepository.deleteAll(userBasedApiRepository.findAllByStatus("D"));


        return RepeatStatus.FINISHED;
    }
}
