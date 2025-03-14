package kr.co.skcc.oss.bap.com.job.processor;

import kr.co.skcc.oss.bap.com.repository.BssmacdRepository;
import kr.co.skcc.oss.com.common.domain.dept.Bssmacd;
import org.springframework.batch.item.ItemProcessor;

public class DeleteBssmacdItemProcessor implements ItemProcessor<Bssmacd, Bssmacd> {

    BssmacdRepository bssmacdRepository;

    public DeleteBssmacdItemProcessor(BssmacdRepository bssmacdRepository) {
        this.bssmacdRepository = bssmacdRepository;
    }

    @Override
    public Bssmacd process(Bssmacd bssmacd) {

        bssmacdRepository.deleteById(bssmacd.getBssmacd());

        return null;
    }
}

