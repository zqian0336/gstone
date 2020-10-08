package com.gstone.gsrecruit.dao;

import com.gstone.gsrecruit.pojo.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface EnterpriseDao extends JpaRepository<Enterprise, String>, JpaSpecificationExecutor<Enterprise> {

    /**
     * According to the state of ishot, gain the list of enterprise
     * @param ishot
     * @return
     */
    public List<Enterprise> findByIshot(String ishot);
}
