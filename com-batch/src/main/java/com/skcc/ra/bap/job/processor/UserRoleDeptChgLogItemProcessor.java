package com.skcc.ra.bap.job.processor;

import com.skcc.ra.bap.repository.DeptRepository;
import com.skcc.ra.bap.repository.UserRoleRepository;
import com.skcc.ra.account.domain.auth.UserRole;
import com.skcc.ra.account.domain.hist.UserRoleHist;
import com.skcc.ra.common.domain.dept.Dept;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class UserRoleDeptChgLogItemProcessor implements ItemProcessor<Object, List<UserRoleHist>> {

    UserRoleRepository userRoleRepository;
    DeptRepository deptRepository;

    public UserRoleDeptChgLogItemProcessor(UserRoleRepository userRoleRepository, DeptRepository deptRepository) {
        this.userRoleRepository = userRoleRepository;
        this.deptRepository = deptRepository;
    }

    @Override
    public List<UserRoleHist> process(Object items) {

        Object[] objects = (Object[]) items;
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        List<String> list = new ArrayList<>();


        while(iterator.hasNext()){
            String value = String.valueOf(iterator.next());
            list.add(value);
        }

        List<UserRole> userRoleList = userRoleRepository.findByUserid(list.get(0));
        List<UserRoleHist> userRoleHistList = new ArrayList<>();
        Dept dept = deptRepository.findByDeptcd(list.get(1));

        for(int i = 0 ; i< userRoleList.size(); i++){
            UserRoleHist userRoleHist = new UserRoleHist();
            userRoleHist.setUserid(userRoleList.get(i).getUserid());
            userRoleHist.setUserRoleId(userRoleList.get(i).getUserRoleId());
            userRoleHist.setChngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
            userRoleHist.setCrudClCd("C");
            userRoleHist.setDeptcd(dept.getDeptcd());
            userRoleHist.setDeptNm(dept.getDeptNm());
            userRoleHist.setLastChngrId("BATCH");
            userRoleHist.setChngResonCntnt("부서변경으로 인한 권한 생성");
            userRoleHist.setLastChngDtmd(LocalDateTime.now());
            userRoleHistList.add(userRoleHist);
        }

        return userRoleHistList;
    }
}