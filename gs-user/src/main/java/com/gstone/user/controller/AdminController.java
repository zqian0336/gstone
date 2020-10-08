package com.gstone.user.controller;

import com.gstone.user.pojo.Admin;
import com.gstone.user.service.AdminService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import util.JwtUtil;

import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value="/login",method= RequestMethod.POST)
    public Result login(@RequestBody Admin admin){
        Admin adminLogin = adminService.login(admin);
        if(adminLogin==null){
            return new Result(false,StatusCode.LOGINERROR,"Login Fail");
        }
        //使得前后端可以通话的操作，采用JWT来实现
        //生成令牌
        String token = jwtUtil.createJWT(adminLogin.getId(),adminLogin.getUsername(),"admin");
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        map.put("role","admin");
        return new Result(true,StatusCode.OK,"Login Success",map);
    }


    @RequestMapping(method= RequestMethod.GET)
    public Result findAll(){
        return new Result(true,StatusCode.OK,"Search Complete",adminService.findAll());
    }


    @RequestMapping(value="/{id}",method= RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"Search Complete",adminService.findById(id));
    }




    @RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
        Page<Admin> pageList = adminService.findSearch(searchMap, page, size);
        return  new Result(true,StatusCode.OK,"Search Complete",  new PageResult<Admin>(pageList.getTotalElements(), pageList.getContent()) );
    }



    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"Search Complete",adminService.findSearch(searchMap));
    }




    @RequestMapping(method=RequestMethod.POST)
    public Result add(@RequestBody Admin admin  ){
        adminService.add(admin);
        return new Result(true,StatusCode.OK,"Add Complete");
    }



    @RequestMapping(value="/{id}",method= RequestMethod.PUT)
    public Result update(@RequestBody Admin admin, @PathVariable String id ){
        admin.setId(id);
        adminService.update(admin);
        return new Result(true,StatusCode.OK,"Edit Complete");
    }



    @RequestMapping(value="/{id}",method= RequestMethod.DELETE)
    public Result delete(@PathVariable String id ){
        adminService.deleteById(id);
        return new Result(true,StatusCode.OK,"Delete Complete");
    }
}
