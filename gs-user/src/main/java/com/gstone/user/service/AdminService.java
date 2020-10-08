package com.gstone.user.service;

import com.gstone.user.dao.AdminDao;
import com.gstone.user.pojo.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    private AdminDao adminDao;
    @Autowired
    private IdWorker idWorker;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public Admin login(Admin admin){
        //先根据用户名查询对象
        Admin adminLogin = adminDao.findByUsername(admin.getUsername());
        //然后拿数据库中的密码和用户输入的密码匹配
        if(adminLogin!=null && encoder.matches(admin.getPassword(),adminLogin.getPassword())){
            //密码匹配后，登录成功
            return adminLogin;
        }
        //登录失败
        return null;
    }

    public List<Admin> findAll() {
        return adminDao.findAll();
    }


    public Page<Admin> findSearch(Map whereMap, int page, int size) {
        Specification<Admin> specification = createSpecification(whereMap);
        PageRequest pageRequest =  PageRequest.of(page-1, size);
        return adminDao.findAll(specification, pageRequest);
    }


    public List<Admin> findSearch(Map whereMap) {
        Specification<Admin> specification = createSpecification(whereMap);
        return adminDao.findAll(specification);
    }

    public Admin findById(String id) {
        return adminDao.findById(id).get();
    }

    public void add(Admin admin) {
        admin.setId(idWorker.nextId()+""); //主键值
        //密码加密
        String newpassword = encoder.encode(admin.getPassword());//加密后的密码
        adminDao.save(admin);
    }

    public void update(Admin admin) {
        adminDao.save(admin);
    }


    public void deleteById(String id) {
        adminDao.deleteById(id);
    }


    private Specification<Admin> createSpecification(Map searchMap) {

        return new Specification<Admin>() {
            @Override
            public Predicate toPredicate(Root<Admin> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 登陆名称
                if (searchMap.get("username") != null && !"".equals(searchMap.get("username"))) {
                    predicateList.add(cb.like(root.get("username").as(String.class), "%" + (String) searchMap.get("username") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                // 状态
                if (searchMap.get("state") != null && !"".equals(searchMap.get("state"))) {
                    predicateList.add(cb.like(root.get("state").as(String.class), "%" + (String) searchMap.get("state") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

}
