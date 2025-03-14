package kr.co.skcc.oss.bap.com.job.processor;

import kr.co.skcc.oss.bap.com.repository.*;
import kr.co.skcc.oss.com.account.domain.auth.UserRole;
import kr.co.skcc.oss.com.account.domain.baseAuth.UserRoleDeptMapping;
import kr.co.skcc.oss.com.account.domain.hist.UserRoleHist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class UserRoleSetItemProcessor implements ItemProcessor<Object, List<UserRole>> {

    UserRoleDeptRepository userRoleDeptRepository;
    UserRoleRepository userRoleRepository;
    UserMenuRepository userMenuRepository;
    UserScrenBttnRepository userScrenBttnRepository;
    UserRoleDeptMappingRepository userRoleDeptMappingRepository;

    public UserRoleSetItemProcessor(UserRoleDeptRepository userRoleDeptRepository, UserRoleRepository userRoleRepository, UserMenuRepository userMenuRepository, UserScrenBttnRepository userScrenBttnRepository, UserRoleDeptMappingRepository userRoleDeptMappingRepository) {
        this.userRoleDeptRepository = userRoleDeptRepository;
        this.userRoleRepository = userRoleRepository;
        this.userMenuRepository = userMenuRepository;
        this.userScrenBttnRepository = userScrenBttnRepository;
        this.userRoleDeptMappingRepository = userRoleDeptMappingRepository;
    }

    @Override
    public List<UserRole> process(Object items) {

        Object[] objects = (Object[]) items;
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        List<String> list = new ArrayList<>();
        List<UserRoleHist> userRoleHistList = new ArrayList<>();

        while(iterator.hasNext()){
            String value = String.valueOf(iterator.next());
            list.add(value);
        }

        //전체 역할 삭제
        userRoleRepository.deleteByUserid(list.get(0));
        //전체 메뉴 삭제
        userMenuRepository.deleteByUserid(list.get(0));
        //전체 화면(버튼)삭제
        userScrenBttnRepository.deleteByUserid(list.get(0));

        //신규 역할 등록
        List<UserRole> userRoleList = new ArrayList<>();

        //자동 역할 생성
        if("1".equals(list.get(3)) || "2".equals(list.get(3)) || "3".equals(list.get(3)) || "7".equals(list.get(3))) {

            if(list.get(5).endsWith("지사") || list.get(5).endsWith("영업소")) {
                if(!(list.get(2) == null || "".equals(list.get(2)))) {
                    if ("250".equals(list.get(2))) {
                        if ("340".equals(list.get(4))) {
                            UserRole userRole = new UserRole();

                            userRole.setUserid(list.get(0));
                            //TSE소팀장
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
                    } else if ("300".equals(list.get(2))) {
                        if ("340".equals(list.get(4))) {
                            UserRole userRole = new UserRole();

                            userRole.setUserid(list.get(0));
                            //TSE 팀원
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
            }

            List<UserRoleDeptMapping> roleList = userRoleDeptMappingRepository.findByRoleDeptTeamCdAndRoleMappReofoCd(list.get(1), list.get(2));

            if(!list.get(6).isEmpty() && !list.get(7).isEmpty()){
                roleList.addAll(userRoleDeptMappingRepository.findByRoleDeptTeamCdAndRoleMappReofoCd(list.get(6), list.get(7)));
            }

            //역할 리스트 생성
            for (int roleNum = 0; roleNum < roleList.size(); roleNum++) {
                UserRole userRole = new UserRole();

                userRole.setUserid(list.get(0));
                userRole.setUserRoleId(roleList.get(roleNum).getUserRoleId());
                userRole.setLastChngrId("BATCH");
                userRole.setLastChngDtmd(LocalDateTime.now());

                userRoleList.add(userRole);
            }
            //검토자 권한 생성
            if(Integer.parseInt(list.get(2)) >= 112 && Integer.parseInt(list.get(2)) <= 210){
                UserRole userRole = new UserRole();

                userRole.setUserid(list.get(0));
                userRole.setUserRoleId("C002");
                userRole.setLastChngrId("BATCH");
                userRole.setLastChngDtmd(LocalDateTime.now());

                userRoleList.add(userRole);
            }
        }
        //기본 역할 생성
        else if("4".equals(list.get(3)) || "5".equals(list.get(3)) || "6".equals(list.get(3))) {

            UserRole userRole = new UserRole();

            userRole.setUserid(list.get(0));
            userRole.setUserRoleId("Z001");
            userRole.setLastChngrId("BATCH");
            userRole.setLastChngDtmd(LocalDateTime.now());

            userRoleList.add(userRole);

            if(!(list.get(2) == null || "".equals(list.get(2)))) {
                if (Integer.parseInt(list.get(2)) >= 112 && Integer.parseInt(list.get(2)) <= 210) {

                    userRole.setUserRoleId("C002");
                    userRoleList.add(userRole);
                }
            }
        }
//
//        if(list.get(5).endsWith("지사") || list.get(5).endsWith("영업소")) {
//            if(!(list.get(2) == null || "".equals(list.get(2)))) {
//                if ("250".equals(list.get(2))) {
//                    if ("340".equals(list.get(4))) {
//                        UserRole userRole = new UserRole();
//
//                        userRole.setUserid(list.get(0));
//                        //TSE소팀장
//                        userRole.setUserRoleId("S037");
//                        userRole.setLastChngrId("BATCH");
//                        userRole.setLastChngDtmd(LocalDateTime.now());
//
//                        userRoleList.add(userRole);
//                    }
//                } else if ("300".equals(list.get(2))) {
//                    if ("340".equals(list.get(4))) {
//                        UserRole userRole = new UserRole();
//
//                        userRole.setUserid(list.get(0));
//                        //TSE 팀원
//                        userRole.setUserRoleId("S038");
//                        userRole.setLastChngrId("BATCH");
//                        userRole.setLastChngDtmd(LocalDateTime.now());
//
//                        userRoleList.add(userRole);
//                    } else if ("410".equals(list.get(4)) || "430".equals(list.get(4)) || "510".equals(list.get(4)) || "511".equals(list.get(4)) || "512".equals(list.get(4))) {
//                        UserRole userRole = new UserRole();
//
//                        userRole.setUserid(list.get(0));
//                        //BP일반
//                        userRole.setUserRoleId("S040");
//                        userRole.setLastChngrId("BATCH");
//                        userRole.setLastChngDtmd(LocalDateTime.now());
//
//                        userRoleList.add(userRole);
//                    }
//                }
//            }
//        }
//

        return userRoleList;
    }
}