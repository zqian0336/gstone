package com.gstone.user.controller;

import com.gstone.user.pojo.User;
import com.gstone.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;


    @RequestMapping(value="/{userid}/{friendid}/{x}",method = RequestMethod.PUT)
    public void updatefanscountandfollowcount(@PathVariable String userid,@PathVariable String friendid,@PathVariable int x){
        userService.updatefanscountandfollowcount(x,userid,friendid);
    }


    /**
     * Login
     * @param user
     * @return
     */
    @RequestMapping(value="/login",method = RequestMethod.POST)
    public Result login(@RequestBody User user){
        user = userService.login(user.getUsername(),user.getPassword());
        if(user==null){
            return new Result(false,StatusCode.LOGINERROR,"登录失败");
        }
        String token = jwtUtil.createJWT(user.getId(),user.getUsername(),"user");
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        map.put("roles","user");
        return new Result(true,StatusCode.OK,"登录成功",map);
    }


    /**
     * SMS
     * @param phone
     * @return
     */

    @RequestMapping(value="/sendsms/{phone}",method = RequestMethod.POST)
    public Result sendSms(@PathVariable String phone){
        userService.sendSMS(phone);
        return new Result(true,StatusCode.OK,"Sent");
    }


    @RequestMapping(value="/register/{code}",method = RequestMethod.POST)
    public Result register(@PathVariable String code,@RequestBody User user){
        //得到缓存中的验证码
        String checkcodeRedis = (String)redisTemplate.opsForValue().get("smscode_"+ user.getUsername());
        if(checkcodeRedis == null){
            return new Result(false,StatusCode.OK,"Please wait for verification code");
        }
        if(!checkcodeRedis.equals(code)){
            return new Result(false,StatusCode.OK,"Invalid Verification Code");
        }
        userService.add(user);
        return new Result(true,StatusCode.OK,"SignUp Success");
    }


    /**
     * 查询全部数据
     * @return
     */
    @RequestMapping(method= RequestMethod.GET)
    public Result findAll(){
        return new Result(true,StatusCode.OK,"Search Success",userService.findAll());
    }

    /**
     * 根据ID查询
     * @param id ID
     * @return
     */
    @RequestMapping(value="/{id}",method= RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"Search Success",userService.findById(id));
    }


    /**
     * 分页+多条件查询
     * @param searchMap 查询条件封装
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return  new Result(true,StatusCode.OK,"Search Success",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
    }

    /**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"Search Success",userService.findSearch(searchMap));
    }

    /**
     * 增加
     * @param user
     */
    @RequestMapping(method=RequestMethod.POST)
    public Result add(@RequestBody User user  ){
        userService.add(user);
        return new Result(true,StatusCode.OK,"Add Success");
    }

    /**
     * 修改
     * @param user
     */
    @RequestMapping(value="/{id}",method= RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id ){
        user.setId(id);
        userService.update(user);
        return new Result(true,StatusCode.OK,"Edit Success");
    }

    /**
     * 删除  必须有admin角色才能删除
     * @param id
     */
    @RequestMapping(value="/{id}",method= RequestMethod.DELETE)
    public Result delete(@PathVariable String id ){
        userService.deleteById(id);
        return new Result(true,StatusCode.OK,"Delete Success");
    }
}
