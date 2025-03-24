package kr.co.skcc.base.bap.com.job.processor;

import kr.co.skcc.base.bap.com.repository.BssmacdRepository;
import kr.co.skcc.base.com.common.domain.dept.Bssmacd;
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

