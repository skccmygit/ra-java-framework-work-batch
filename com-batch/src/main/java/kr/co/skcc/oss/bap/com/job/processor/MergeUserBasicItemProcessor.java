package kr.co.skcc.oss.bap.com.job.processor;

import kr.co.skcc.oss.com.common.domain.userBasic.UserBasic;
import kr.co.skcc.oss.com.common.domain.userBasic.UserBasicTemp;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MergeUserBasicItemProcessor implements ItemProcessor<UserBasicTemp, UserBasic> {

    @Override
    public UserBasic process(UserBasicTemp userBasicTemp) {

        UserBasic userBasic = new UserBasic();

        userBasic.setEmpno(userBasicTemp.getEmpno());
        userBasic.setEmpKrnNm(userBasicTemp.getEmpKrnNm());
        userBasic.setEmpEnglnm(userBasicTemp.getEmpEnglnm());
        userBasic.setEmpEngfnm(userBasicTemp.getEmpEngfnm());
        userBasic.setDeptcd(userBasicTemp.getDeptcd());
        userBasic.setBthdy(userBasicTemp.getBthdy());
        userBasic.setOwgmPhno(userBasicTemp.getOwgmPhno());
        userBasic.setMphno(userBasicTemp.getMphno() == null ? "" : userBasicTemp.getMphno().replace("-",""));
        userBasic.setGroupEntcpDt(userBasicTemp.getGroupEntcpDt());
        userBasic.setTcomEntcpDt(userBasicTemp.getTcomEntcpDt());
        userBasic.setCurtpOfordCd(userBasicTemp.getCurtpOfordCd());
        userBasic.setCurtpOfordDt(userBasicTemp.getCurtpOfordDt());
        userBasic.setResgDt(userBasicTemp.getResgDt());
        userBasic.setComClCd(userBasicTemp.getComClCd());
        userBasic.setClofpCd(userBasicTemp.getClofpCd());
        userBasic.setReofoCd(userBasicTemp.getReofoCd());
        userBasic.setJgpCd(userBasicTemp.getJgpCd());
        userBasic.setVctnCd(userBasicTemp.getVctnCd());
        userBasic.setTeambrClCd(userBasicTemp.getTeambrClCd());
        userBasic.setClofpNm(userBasicTemp.getClofpNm());
        userBasic.setJgpNm(userBasicTemp.getJgpNm());
        userBasic.setVctnNm(userBasicTemp.getVctnNm());
        userBasic.setEmailaddr(userBasicTemp.getEmailaddr());
        userBasic.setBefoEmpno(userBasicTemp.getBefoEmpno());
        userBasic.setLastChngrId("BATCH");
        userBasic.setLastChngDtmd(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return userBasic;
    }
}

