package com.gstone.user.service;

import com.gstone.user.dao.UserDao;
import com.gstone.user.pojo.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;
import util.JwtUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtUtil jwtUtil;

    public List<User> findAll(){
        return userDao.findAll();
    }

    public Page<User> findSearch(Map searchMap, int page, int size){
        Specification<User> specification = createSpecification(searchMap);
        PageRequest pageRequest = PageRequest.of(page-1, size);
        return userDao.findAll(specification, pageRequest);
    }

    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    public User findById(String id) {
        return userDao.findById(id).get();
    }

    public void add(User user) { //判断验证码是否正确
//        String syscode =
//                (String)redisTemplate.opsForValue().get("smscode_" + user.getUsername()); //提取系统正确的验证码
//        if(syscode==null){
//            throw new RuntimeException("Resend the verification code");
//        }
//        if(!syscode.equals(code)){
//            throw new RuntimeException("Incorrect Verification Code !"); }


        user.setId( idWorker.nextId()+"" );
        user.setPassword(encoder.encode(user.getPassword()));
        user.setFollowcount(0);//关注数
        user.setFanscount(0);//粉丝数
        user.setOnline(0L);//在线时长
        user.setRegdate(new Date());//注册日期
        user.setUpdatedate(new Date());//更新日期
        user.setLastdate(new Date());//最后登陆日期
        userDao.save(user);
    }


    public void update(User user) {
        userDao.save(user);
    }


    public void deleteById(String id) {

        String token = (String) request.getAttribute("claims_admin");
        if(token==null || "".equals(token)){
            throw new RuntimeException("权限不足！");
        }
        userDao.deleteById(id);
    }




    public void sendSMS(String phone){
        //1. 生成验证码
        String code = RandomStringUtils.randomNumeric(6);
        System.out.println(phone+" , the verification code is: "+code);
        //2. 将验证码放入redis, 15min 后过期
        redisTemplate.opsForValue().set("smscode_"+phone, code, 15, TimeUnit.MINUTES);

        //3. 将验证码和手机号发送到rabbitmq, 给用户：
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("code", code);
        rabbitTemplate.convertAndSend("sms",map);

    }

    public User login(String phone,String password){
        User user = userDao.findByPhone(phone);
        if(user!=null && encoder.matches(password,user.getPassword())){
            return user;
        }
        return null;
    }

    @Transactional
    public void updatefanscountandfollowcount(int x,String userid,String friendid){
        userDao.updatefanscount(x,friendid);
        userDao.updatefollowcount(x,userid);
    }



    private Specification<User> createSpecification(Map searchMap) {
        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }

                return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }
}

