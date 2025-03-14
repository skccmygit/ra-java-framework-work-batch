package kr.co.skcc.oss.bap.com.job.processor;

import kr.co.skcc.oss.bap.com.repository.AccountRepository;
import kr.co.skcc.oss.com.account.domain.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class SearchDeptChgItemProcessor implements ItemProcessor<Object, Account> {

    AccountRepository accountRepository;

    public SearchDeptChgItemProcessor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account process(Object items) {

        Object[] objects = (Object[]) items;
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        List<String> list = new ArrayList<>();

        while(iterator.hasNext()){
            String value = String.valueOf(iterator.next());
            list.add(value);
        }

        Account account = accountRepository.getById(list.get(0));

        account.setDeptcd(list.get(1));
        account.setReofoCd(list.get(2));
        account.setVctnCd(list.get(6));

        String deptcd = list.get(1);
        String vctnCd = list.get(6);

        if ("160".equals(vctnCd) || "163".equals(vctnCd) || "165".equals(vctnCd) || "175".equals(vctnCd)
                || "176".equals(vctnCd) || "177".equals(vctnCd) || "178".equals(vctnCd) || "155".equals(vctnCd)
                || "156".equals(vctnCd) || "157".equals(vctnCd)) {
            account.setInnerUserClCd("3");
        } else if ("310".equals(vctnCd) || "340".equals(vctnCd)) {
            account.setInnerUserClCd("4");
        } else if ("210".equals(vctnCd) || "215".equals(vctnCd) || "220".equals(vctnCd)
                || "225".equals(vctnCd) || "230".equals(vctnCd) || "213".equals(vctnCd) || "226".equals(vctnCd)) {
            account.setInnerUserClCd("2");
        }else {
            account.setInnerUserClCd("0");
        }

        String tmpStsCd = "";
        //list.get(3) => 역할 생성 리스트 존재 여부 / list.get(4) => 부서 변경 여부 / list.get(5) => 직책 변경 여부
        /*
            1. 역할 자동 생성 / 부서 변경 이력 생성 / 직책 변경 이력 생성
            2. 역할 자동 생성 / 부서 변경 이력 생성
            3. 역할 자동 생성 / 직책 변경 이력 생성
            4. 기본 역할 생성 / 부서 변경 이력 생성 / 직책 변경 이력 생성
            5. 기본 역할 생성 / 부서 변경 이력 생성
            6. 기본 역할 생성 / 직책 변경 이력 생성
         */
        if("Y".equals(list.get(3)) && "Y".equals(list.get(4)) && "Y".equals(list.get(5))) tmpStsCd = "1";
        if("Y".equals(list.get(3)) && "Y".equals(list.get(4)) && "X".equals(list.get(5))) tmpStsCd = "2";
        if("Y".equals(list.get(3)) && "X".equals(list.get(4)) && "Y".equals(list.get(5))) tmpStsCd = "3";
        if("X".equals(list.get(3)) && "Y".equals(list.get(4)) && "Y".equals(list.get(5))) tmpStsCd = "4";
        if("X".equals(list.get(3)) && "Y".equals(list.get(4)) && "X".equals(list.get(5))) tmpStsCd = "5";
        if("X".equals(list.get(3)) && "X".equals(list.get(4)) && "Y".equals(list.get(5))) tmpStsCd = "6";

        account.setUseridStsCd(tmpStsCd);
        account.setLastChngDtmd(LocalDateTime.now());
        account.setLastChngrId("BATCH");

        return account;
    }
}