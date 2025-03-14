package kr.co.skcc.oss.bap.com.job.writer;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.List;

@Slf4j
public class JpaItemListWriter<T> implements ItemWriter<List<T>> {

    private final JpaItemWriter<T> jpaItemWriter;
    public JpaItemListWriter(EntityManagerFactory entityManagerFactory){
        this.jpaItemWriter = new JpaItemWriter<>();
        this.jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
    }

//    @Override
//    public void write(List<? extends List<T>> items){
//        List<T> totalList = new ArrayList<>();
//
//        for(List<T> list : items){
//            totalList.addAll(list);
//        }
//        jpaItemWriter.write(totalList);
//    }

    @Override
    public void write(Chunk<? extends List<T>> chunk) throws Exception {
        Chunk<T> totalList = new Chunk<>();

        for(List<T> list : chunk){
            totalList.addAll(list);
        }
        jpaItemWriter.write(totalList);
    }
}