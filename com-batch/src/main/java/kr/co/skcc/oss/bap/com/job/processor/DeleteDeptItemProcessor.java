package kr.co.skcc.oss.bap.com.job.processor;

import kr.co.skcc.oss.bap.com.repository.DeptRepository;
import kr.co.skcc.oss.com.common.domain.dept.Dept;
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

