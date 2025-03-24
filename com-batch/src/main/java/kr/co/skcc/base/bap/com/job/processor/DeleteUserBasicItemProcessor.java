package kr.co.skcc.base.bap.com.job.processor;

import kr.co.skcc.base.bap.com.repository.UserBasicRepository;
import kr.co.skcc.base.com.common.domain.userBasic.UserBasic;
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

