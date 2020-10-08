package com.gstone.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @FeignClient注解用于指定从哪个服务中调用功能 ，注意 里面的名称与被调用的服务 名保持一致，并且不能包含下划线
 * @RequestMapping注解用于对被调用的微服务进行地址映射。注意 @PathVariable注 解一定要指定参数名称，否则出错
 */

@Component
@FeignClient("gs-user")
public interface UserClient {

    @RequestMapping(value="/user/{userid}/{friendid}/{x}",method = RequestMethod.PUT)
    public void updatefanscountandfollowcount(@PathVariable("userid") String userid, @PathVariable("friendid") String friendid, @PathVariable("x") int x);

}
