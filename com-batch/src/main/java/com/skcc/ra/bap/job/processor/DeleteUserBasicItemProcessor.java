package com.skcc.ra.bap.job.processor;

import com.skcc.ra.bap.repository.UserBasicRepository;
import com.skcc.ra.common.domain.userBasic.UserBasic;
import org.springframework.batch.item.ItemProcessor;

public class DeleteUserBasicItemProcessor implements ItemProcessor<UserBasic, UserBasic> {

    UserBasicRepository userBasicRepository;

    public DeleteUserBasicItemProcessor(UserBasicRepository userBasicRepository) {
        this.userBasicRepository = userBasicRepository;
    }

    @Override
    public UserBasic process(UserBasic userBasic) {

        userBasicRepository.deleteById(userBasic.getEmpno());

        return null;
    }
}

