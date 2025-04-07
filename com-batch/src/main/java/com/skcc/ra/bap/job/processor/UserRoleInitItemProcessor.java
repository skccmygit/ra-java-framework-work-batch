package com.skcc.ra.bap.job.processor;

import com.skcc.ra.bap.repository.UserRoleDeptMappingRepository;
import com.skcc.ra.account.domain.auth.UserRole;
import com.skcc.ra.account.domain.baseAuth.UserRoleDeptMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class UserRoleInitItemProcessor implements ItemProcessor<Object, List<UserRole>> {

//    부서 - Role 맵핑
    UserRoleDeptMappingRepository userRoleDeptMappingRepository;

    public UserRoleInitItemProcessor(UserRoleDeptMappingRepository userRoleDeptMappingRepository) {
        this.userRoleDeptMappingRepository = userRoleDeptMappingRepository;
    }

    @Override
    public List<UserRole> process(Object items) {

        Object[] objects = (Object[]) items;
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        List<String> list = new ArrayList<>();

        while(iterator.hasNext()){
            String value =String.valueOf(iterator.next());
            list.add(value);
        }

        //신규 역할 등록
        List<UserRole> userRoleList = new ArrayList<>();

        //자동 역할 생성

        if("Y".equals(list.get(3))) {
            if(list.get(5).endsWith("지사") || list.get(5).endsWith("영업소")) {
                if ("250".equals(list.get(2))) {
                    if ("340".equals(list.get(4))) {
                        UserRole userRole = new UserRole();

                        //TSE소팀장
                        userRole.setUserid(list.get(0));
                        userRole.setUserRoleId("S037");
                        userRole.setLastChngrId("BATCH");
                        userRole.setLastChngDtmd(LocalDateTime.now());

                        userRoleList.add(userRole);

                        userRole = new UserRole();

                        //지식관리 권한
                        userRole.setUserid(list.get(0));
                        userRole.setUserRoleId("I016");
                        userRole.setLastChngrId("BATCH");
                        userRole.setLastChngDtmd(LocalDateTime.now());

                        userRoleList.add(userRole);

                        return userRoleList;
                    }
                } else if("300".equals(list.get(2))){
                    if ("340".equals(list.get(4))) {
                        UserRole userRole = new UserRole();

                        //TSE 팀원
                        userRole.setUserid(list.get(0));
                        userRole.setUserRoleId("S038");
                        userRole.setLastChngrId("BATCH");
                        userRole.setLastChngDtmd(LocalDateTime.now());

                        userRoleList.add(userRole);

                        userRole = new UserRole();

                        //지식관리 권한
                        userRole.setUserid(list.get(0));
                        userRole.setUserRoleId("I016");
                        userRole.setLastChngrId("BATCH");
                        userRole.setLastChngDtmd(LocalDateTime.now());

                        userRoleList.add(userRole);
                        return userRoleList;
                    } else if ("410".equals(list.get(4)) || "430".equals(list.get(4)) || "510".equals(list.get(4)) || "511".equals(list.get(4)) || "512".equals(list.get(4))) {
                        UserRole userRole = new UserRole();

                        userRole.setUserid(list.get(0));
                        //BP일반
                        userRole.setUserRoleId("S040");
                        userRole.setLastChngrId("BATCH");
                        userRole.setLastChngDtmd(LocalDateTime.now());

                        userRoleList.add(userRole);
                        return userRoleList;
                    }
                }
            }

            List<UserRoleDeptMapping> roleList = userRoleDeptMappingRepository.findByRoleDeptTeamCdAndRoleMappReofoCd(list.get(1), list.get(2));

            if(!list.get(6).isEmpty() && !list.get(7).isEmpty()){
                roleList.addAll(userRoleDeptMappingRepository.findByRoleDeptTeamCdAndRoleMappReofoCd(list.get(6), list.get(7)));
            }

            for (int roleNum = 0; roleNum < roleList.size(); roleNum++) {
                UserRole userRole = new UserRole();

                userRole.setUserid(list.get(0));
                userRole.setUserRoleId(roleList.get(roleNum).getUserRoleId());
                userRole.setLastChngrId("BATCH");
                userRole.setLastChngDtmd(LocalDateTime.now());

                userRoleList.add(userRole);
            }

            //검토자 권한 추가
            if(Integer.parseInt(list.get(2)) >= 112 && Integer.parseInt(list.get(2)) <= 210){
                UserRole userRole = new UserRole();

                userRole.setUserid(list.get(0));
                userRole.setUserRoleId("C002");
                userRole.setLastChngrId("BATCH");
                userRole.setLastChngDtmd(LocalDateTime.now());

                userRoleList.add(userRole);
            }
        }

        else if("X".equals(list.get(3))){
            //기본 사용자 역할 생성
            UserRole userRole = new UserRole();

            userRole.setUserid(list.get(0));
            userRole.setUserRoleId("Z001");
            userRole.setLastChngrId("BATCH");
            userRole.setLastChngDtmd(LocalDateTime.now());

            userRoleList.add(userRole);

            //검토자 계정 생성
            userRole.setUserRoleId("C002");
            userRoleList.add(userRole);
        }
        return userRoleList;
    }
}