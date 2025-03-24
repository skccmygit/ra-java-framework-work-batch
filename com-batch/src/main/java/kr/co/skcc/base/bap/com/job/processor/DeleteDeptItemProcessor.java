package kr.co.skcc.base.bap.com.job.processor;

import kr.co.skcc.base.bap.com.repository.DeptRepository;
import kr.co.skcc.base.com.common.domain.dept.Dept;
import org.springframework.batch.item.ItemProcessor;

public class DeleteDeptItemProcessor implements ItemProcessor<Dept, Dept> {

    DeptRepository deptRepository;

    public DeleteDeptItemProcessor(DeptRepository deptRepository) {
        this.deptRepository = deptRepository;
    }

    @Override
    public Dept process(Dept dept) {

        deptRepository.deleteById(dept.getDeptcd());

        return null;
    }
}

