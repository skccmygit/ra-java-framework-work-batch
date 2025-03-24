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
public class AccountDormantLogItemProcessor implements ItemProcessor<Object, AccountStsChng> {


    @Override
    public AccountStsChng process(Object items) {

        Object[] objects = (Object[]) items;
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        List<String> list = new ArrayList<>();

        while(iterator.hasNext()){
            String value = String.valueOf(iterator.next());
            list.add(value);
        }

        AccountStsChng accountStsChng = new AccountStsChng();

        accountStsChng.setUserid(list.get(0));
        accountStsChng.setChngColEngshNm("USERID_STS_CD");
        //임시비밀번호 부여 상태
        accountStsChng.setChngColVal("L");
        accountStsChng.setChngDtm(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        accountStsChng.setLastChngDtmd(LocalDateTime.now());
        accountStsChng.setLastChngrId("BATCH");

        return accountStsChng;
    }
}