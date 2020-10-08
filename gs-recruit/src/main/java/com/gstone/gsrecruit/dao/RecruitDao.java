package com.gstone.gsrecruit.dao;

import com.gstone.gsrecruit.pojo.Recruit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RecruitDao extends JpaRepository<Recruit, String>, JpaSpecificationExecutor<Recruit> {
    List<Recruit> findTop6ByStateOrderByCreatetimeDesc(String s);

    List<Recruit> findTop6ByStateNotOrderByCreatetimeDesc(String s);
}
