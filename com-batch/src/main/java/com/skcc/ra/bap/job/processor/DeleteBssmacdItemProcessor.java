package com.skcc.ra.bap.job.processor;

import com.skcc.ra.bap.repository.BssmacdRepository;
import com.skcc.ra.common.domain.dept.Bssmacd;
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

