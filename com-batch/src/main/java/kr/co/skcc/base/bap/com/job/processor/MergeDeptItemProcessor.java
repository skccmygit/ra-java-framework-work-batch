package kr.co.skcc.base.bap.com.job.processor;

import kr.co.skcc.base.com.common.domain.dept.Dept;
import kr.co.skcc.base.com.common.domain.dept.DeptTemp;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MergeDeptItemProcessor implements ItemProcessor<DeptTemp, Dept> {

    @Override
    public Dept process(DeptTemp deptTemp) {

        Dept dept = new Dept();

        if("100001".equals(deptTemp.getDeptcd())){
            deptTemp.setDeptNm("사업총괄");
            deptTemp.setUseYn("Y");
        }
        if("100000".equals(deptTemp.getDeptcd())) {
            deptTemp.setSuperDeptcd("");
        }

        if(deptTemp.getDeptcd().equals(deptTemp.getSuperDeptcd())){
            deptTemp.setSuperDeptcd("");
        }

        dept.setDeptcd(deptTemp.getDeptcd());
        dept.setMgmtDeptcd(deptTemp.getMgmtDeptcd());
        dept.setDeptNm(deptTemp.getDeptNm());
        dept.setEngshDeptNm(deptTemp.getEngshDeptNm());
        dept.setEngshDeptAbbrNm(deptTemp.getEngshDeptAbbrNm());
        dept.setBssmacd(deptTemp.getBssmacd());
        dept.setSuperDeptcd(deptTemp.getSuperDeptcd());
        dept.setDeptGradeCd(deptTemp.getDeptGradeCd());
        dept.setDeptCratDt(deptTemp.getDeptCratDt());
        dept.setDeptAbolDt(deptTemp.getDeptAbolDt());
        dept.setFempDeptcd(deptTemp.getFempDeptcd());
        dept.setWhtaxBzplCd(deptTemp.getWhtaxBzplCd());
        dept.setSalsDeptcd(deptTemp.getSalsDeptcd());
        dept.setInvDeptcd(deptTemp.getInvDeptcd());
        dept.setInvntWrhusCd(deptTemp.getInvntWrhusCd());
        dept.setVatBzplCd(deptTemp.getVatBzplCd());
        dept.setZipcd(deptTemp.getZipcd());
        dept.setBasicAddr(deptTemp.getBasicAddr());
        dept.setDetilAddr(deptTemp.getDetilAddr());
        dept.setPhno(deptTemp.getPhno() == null ? "" : deptTemp.getPhno().replace("-",""));
        dept.setFaxNo(deptTemp.getFaxNo() == null ? "" : deptTemp.getFaxNo().replace("-",""));
        dept.setBzno(deptTemp.getBzno());
        dept.setBzmanAcqDt(deptTemp.getBzmanAcqDt());
        dept.setTxofcCd(deptTemp.getTxofcCd());
        dept.setTxofcNm(deptTemp.getTxofcNm());
        dept.setComBscndNm(deptTemp.getComBscndNm());
        dept.setComItmNm(deptTemp.getComItmNm());
        dept.setComCorpNm(deptTemp.getComCorpNm());
        dept.setUseYn(deptTemp.getUseYn());
        dept.setCcntrCd(deptTemp.getCcntrCd());
        dept.setRprttNm(deptTemp.getRprttNm());
        dept.setExtsnNo(deptTemp.getExtsnNo() == null ? "" : deptTemp.getExtsnNo().replace("-",""));
        dept.setLastChngrId("BATCH");
        dept.setLastChngDtmd(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return dept;
    }
}

