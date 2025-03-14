package kr.co.skcc.oss.bap.com.job.writer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
public class NoOpItemWriter implements ItemWriter {

    @Override
    public void write(Chunk chunk) throws Exception {

    }
}