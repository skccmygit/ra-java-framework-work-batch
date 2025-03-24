package kr.co.skcc.base.bap.com.job.processor;

import kr.co.skcc.base.com.account.domain.hist.AccountStsChng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class AccountStsDeptChgLogItemProcessor implements ItemProcessor<Object, List<AccountStsChng>> {


    @Override
    public List<AccountStsChng> process(Object items) {

        Object[] objects = (Object[]) items;
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        List<String> list = new ArrayList<>();

        while(iterator.hasNext()){
            String value = String.valueOf(iterator.next());
            list.add(value);
        }

        List<AccountStsChng> accountStsChngList = new ArrayList<>();
        AccountStsChng accountStsChng = new AccountStsChng();

        accountStsChng.setUserid(list.get(0));
        accountStsChng.setChngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        accountStsChng.setLastChngDtmd(LocalDateTime.now());
        accountStsChng.setLastChngrId("BATCH");

        String useridSts = list.get(3);


        /*
            1. 역할 자동 생성 / 부서 변경 이력 생성 / 직책 변경 이력 생성
            2. 역할 자동 생성 / 부서 변경 이력 생성
            3. 역할 자동 생성 / 직책 변경 이력 생성
            4. 기본 역할 생성 / 부서 변경 이력 생성 / 직책 변경 이력 생성
            5. 기본 역할 생성 / 부서 변경 이력 생성
            6. 기본 역할 생성 / 직책 변경 이력 생성
         */
        if("1".equals(useridSts) || "4".equals(useridSts)) {
            accountStsChng.setChngColEngshNm("DEPTCD");
            accountStsChng.setChngColVal(list.get(1));

            accountStsChngList.add(accountStsChng);

            accountStsChng.setChngColEngshNm("REOFO_CD");
            accountStsChng.setChngColVal(list.get(2));

            accountStsChngList.add(accountStsChng);
        }
        else if("2".equals(useridSts) || "5".equals(useridSts)) {
            accountStsChng.setChngColEngshNm("DEPTCD");
            accountStsChng.setChngColVal(list.get(1));

            accountStsChngList.add(accountStsChng);
        }
        else if("3".equals(useridSts) || "6".equals(useridSts)) {
            accountStsChng.setChngColEngshNm("REOFO_CD");
            accountStsChng.setChngColVal(list.get(2));

            accountStsChngList.add(accountStsChng);
        }
        else if("7".equals(useridSts) ) {
            accountStsChng.setChngColEngshNm("VCTN_CD");
            accountStsChng.setChngColVal(list.get(4));

            accountStsChngList.add(accountStsChng);
        }

        return accountStsChngList;
    }
}