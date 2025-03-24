package kr.co.skcc.base.bap.com.job.processor;

import kr.co.skcc.base.com.common.domain.menu.MenuStatistics;
import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MenuStatisticsItemProcessor implements ItemProcessor<Object, MenuStatistics> {

    @Override
    public MenuStatistics process(Object items) {

        Object[] objects = (Object[]) items;
        Iterator<Object> iterator = Arrays.stream(objects).iterator();
        List<String> list = new ArrayList<>();

        while(iterator.hasNext()){
            String value = String.valueOf(iterator.next());
            list.add(value);
        }

        MenuStatistics menuStatistics = new MenuStatistics();

        menuStatistics.setSumrDt(list.get(0).substring(0,8));
        menuStatistics.setMenuId(list.get(1));
        menuStatistics.setLastChngDtmd(LocalDateTime.now());
        menuStatistics.setLastChngrId("BATCH");
        menuStatistics.setConnQty(Long.parseLong(list.get(2)));

        return menuStatistics;
    }
}