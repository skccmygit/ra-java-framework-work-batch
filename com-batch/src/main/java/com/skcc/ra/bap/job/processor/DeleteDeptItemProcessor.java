package com.skcc.ra.bap.job.processor;

import com.skcc.ra.bap.repository.DeptRepository;
import com.skcc.ra.common.domain.dept.Dept;
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

