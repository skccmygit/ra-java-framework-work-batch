package kr.co.skcc.oss.bap.com.job.processor;

import kr.co.skcc.oss.com.account.domain.account.Account;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class CapAccountCreationItemProcessor implements ItemProcessor<Object, Account> {

    @Override
    public Account process(Object items) {

        Object[] objects = (Object[]) items;
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        List<String> list = new ArrayList<>();

        while(iterator.hasNext()){
            String value = String.valueOf(iterator.next());
            list.add(value);
        }

        Account account = new Account();

        account.setUserid(list.get(0));
        account.setUserNm(list.get(1));
        account.setConnPsswd(DigestUtils.sha512Hex(account.getUserid()).toUpperCase());
        account.setPsswdExpirDt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        account.setUserContPhno(list.get(2));
        account.setUserGroupCd("1");
        account.setDeptcd(list.get(3));
        account.setReofoCd(list.get(4));
        account.setVctnCd(list.get(5));

        String vctnCd = list.get(5);

        if ("160".equals(vctnCd) || "163".equals(vctnCd) || "165".equals(vctnCd) || "175".equals(vctnCd)
                || "176".equals(vctnCd) || "177".equals(vctnCd) || "178".equals(vctnCd) || "155".equals(vctnCd)
                || "156".equals(vctnCd) || "157".equals(vctnCd)) {
            account.setInnerUserClCd("3");
        } else if ("310".equals(vctnCd) || "340".equals(vctnCd)) {
            account.setInnerUserClCd("4");
        } else if ("210".equals(vctnCd) || "215".equals(vctnCd) || "220".equals(vctnCd)
                || "225".equals(vctnCd) || "230".equals(vctnCd) || "213".equals(vctnCd) || "226".equals(vctnCd)) {
            account.setInnerUserClCd("2");
        } else {
            account.setInnerUserClCd("0");
        }

        account.setUserIdentNo(list.get(0));
        account.setUseridStsCd("X");
        account.setPsswdErrFrqy(0);
        account.setFstRegDtmd(LocalDateTime.now());
        account.setLastChngDtmd(LocalDateTime.now());
        account.setLastChngrId("BATCH");

        return account;
    }
}