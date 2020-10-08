package com.gstone.web.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

/**
 * 启动gs-web会发现过滤器已经执行 filterType:返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过
 * 滤器类型，具体如下:
 * pre :可以在请求被路由之前调用
 * route :在路由请求时候被调用
 * post :在route和error过滤器之后被调用 error :处理请求时发生错误时被调用
 * filterOrder :通过int值来定义过滤器的执行顺序
 * shouldFilter :返回一个boolean类型来判断该过滤器是否要执行，所以通过此函数可
 * 实现过滤器的开关。在上例中，我们直接返回true，所以该过滤器总是生效 run :过滤器的具体逻辑。
 */


//ZuulFilter 必须所有method必须全部实现
@Component
public class WebFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre"; // 前置过滤器
    }

    @Override
    public int filterOrder() {
        return 0; // 优先级为0，数字越大，优先级越低
    }

    @Override
    public boolean shouldFilter() {
        return true; // 是否执行该过滤器，此处为true，说明需要过滤
    }

    @Override
    public Object run() throws ZuulException {
        System.out.println("This is Zuul Filter");
        return null;
    }
}
