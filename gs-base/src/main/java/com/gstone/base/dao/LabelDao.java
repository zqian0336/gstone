package com.gstone.base.dao;

import com.gstone.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;



public interface LabelDao extends JpaRepository<Label, String>, JpaSpecificationExecutor<Label> {
    //JpaRepository provide CRUD
    //JpaSpecificationExecutor is used for "select" with complex condition
}
