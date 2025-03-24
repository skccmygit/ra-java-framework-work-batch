package kr.co.skcc.base.bap.com.job.tasklet;

import kr.co.skcc.base.bap.com.domain.RoleBasedApi;
import kr.co.skcc.base.bap.com.repository.ApiAuthorizationInformationMapper;
import kr.co.skcc.base.bap.com.repository.RoleBasedApiRepository;
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
 * RoleBasedApiUpdateTasklet.java
 * : 작성필요
 *
 * @author Lee Ki Jung(jellyfishlove@sk.com)
 * @version 1.0.0
 * @since 2022-02-10, 최초 작성
 */
@Component
@Slf4j
public class RoleBasedApiUpdateTasklet implements Tasklet {

    @Autowired
    private ApiAuthorizationInformationMapper apiAuthorizationInformationMapper;

    @Autowired
    private RoleBasedApiRepository roleBasedApiRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        /*
            update all data invalid
        */
        log.info("[Step 1] set all redis data expired");
        List<RoleBasedApi> roleBasedApiList = (List<RoleBasedApi>) roleBasedApiRepository.findAll();
        List<RoleBasedApi> roleBasedApiListForSave = new ArrayList<>();
        if(!(roleBasedApiList == null || roleBasedApiList.isEmpty())) {

            for(RoleBasedApi item : roleBasedApiList){
                if(item != null) {
                    item.setStatus("D");
                    roleBasedApiListForSave.add(item);
                }
            }
            if(!roleBasedApiListForSave.isEmpty()) {
                roleBasedApiRepository.saveAll(roleBasedApiListForSave);
            }
        }

        /*
            reader
         */
        log.info("[Step 2] read from db");
        List<Map<String, Object>> roleBasedApiUpdateList = apiAuthorizationInformationMapper.findRoleBasedApiUpdateList();

        /*
            each
         */
        if(!(roleBasedApiUpdateList == null || roleBasedApiUpdateList.isEmpty())) {
            for(Map<String, Object> item : roleBasedApiUpdateList){
                RoleBasedApi entry = new RoleBasedApi();
                entry.setUserRoleId((String) item.get("USER_ROLE_ID"));
                entry.setScrenId((String) item.get("SCREN_ID"));
                entry.setBttnId((String) item.get("BTTN_ID"));
                entry.setApiId((int) item.get("API_ID"));
                entry.setApiLocUrladdr((String) item.get("API_LOC_URLADDR"));
                entry.setHttMethodVal((String) item.get("HTT_METHOD_VAL"));
                entry.setStatus("I");

                roleBasedApiRepository.save(entry);
            }
        }
        /*
            delete expired data
         */
        //roleBasedApiRepository.deleteAllByStatus("D");
        log.info("[Step 3] delete expired redis data");
        roleBasedApiRepository.deleteAll(roleBasedApiRepository.findAllByStatus("D"));

        return RepeatStatus.FINISHED;
    }
}
