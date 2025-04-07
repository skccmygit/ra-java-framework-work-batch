package com.skcc.ra.bap.job.processor;

import com.skcc.ra.common.domain.dept.Bssmacd;
import com.skcc.ra.common.domain.dept.BssmacdTemp;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MergeBssmacdItemProcessor implements ItemProcessor<BssmacdTemp, Bssmacd> {

    @Override
    public Bssmacd process(BssmacdTemp bssmacdTemp) {

        Bssmacd bssmacd = new Bssmacd();

        bssmacd.setBssmacd(bssmacdTemp.getBssmacd());
        bssmacd.setBssHqNm(bssmacdTemp.getBssHqNm());
        bssmacd.setUseYn(bssmacdTemp.getUseYn());
        bssmacd.setLastChngrId("BATCH");
        bssmacd.setLastChngDtmd(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return bssmacd;
    }
}

