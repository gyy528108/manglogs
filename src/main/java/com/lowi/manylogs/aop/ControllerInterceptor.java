package com.lowi.manylogs.aop;

import cn.hutool.json.JSONUtil;
import com.lowi.manylogs.config.Result;
import com.lowi.manylogs.utils.LoggerBuilderUtils;
import lombok.Getter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * ControllerInterceptor.java
 * ==============================================
 * Copy right 2015-2017  by http://www.51lick.com
 * ----------------------------------------------
 * This is not a free software, without any authorization is not allowed to use and spread.
 * ==============================================
 *
 * @desc: 控制器拦截
 * @author: huyb
 * @version: v2.0.0
 * @since: 2017-4-24 16:38
 */
@Aspect
@Component
@Getter
public class ControllerInterceptor {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 定义拦截规则：拦截com.lyx.oaserver.controller包下面的所有类中，有@RequestMapping注解的方法。
     */
    @Pointcut("execution(* com.lowi.manylogs.controller..*(..))")
    public void controllerMethodPointcut() {
    }

    private static Set<String> setList = new HashSet<>();

    private static String inNewLogMethod;

    @Value("${inNewLogMethod}")
    public void setInNewLogMethod(String inNewLogMethod) {
        ControllerInterceptor.inNewLogMethod = inNewLogMethod;
        if (!inNewLogMethod.isEmpty()) {
            String[] split = inNewLogMethod.split(",");
            setList.addAll(Arrays.asList(split));
        }
    }

    private final static String serviceName = "lowi";

    /**
     * 拦截器具体实现
     *
     * @param pjp
     * @param requestMapping
     * @return Object 被拦截方法的执行结果
     */
    @Around("controllerMethodPointcut() && @annotation(requestMapping)") //指定拦截器规则；也可以直接把“execution(* .........)”写进这里
    public Object Interceptor(ProceedingJoinPoint pjp, RequestMapping requestMapping) {
        Result result = new Result();
        result.setCode(1);
        result.setMsg("fail");
        boolean inNewLog = false;
        String classPath = "";
        String methodPath = "";
        String requestPath = "";
        ch.qos.logback.classic.Logger newLogger = null;
        Map<String, Object> parameMap = new HashMap<String, Object>();
        try {
            //类的注解请求路径
            classPath = pjp.getTarget().getClass().getAnnotation(RequestMapping.class).value()[0];
            //方法的注解请求路径
            methodPath = requestMapping.value()[0];
            inNewLog = setList.contains(methodPath);
            requestPath = classPath + methodPath;
            //取出请求参数
            Object[] args = pjp.getArgs();
            if (args != null && args.length > 0 && args[0] instanceof Map<?, ?>) {
                parameMap = (Map<String, Object>) args[0];
            }
            //执行被拦截的方法
            Object res = pjp.proceed();
            if (res instanceof Result) {
                result = (Result) res;
            }
        } catch (Throwable e) {
            logger.error("[{}]接口请求处理异常: ", requestPath, e);
        }
        if (inNewLog) {
            newLogger = LoggerBuilderUtils.getLogger(serviceName);
            newLogger.info("请求路径：{}", requestPath);
            ;
            newLogger.info("请求参数：{}", JSONUtil.toJsonStr(parameMap));
            newLogger.info("[{}]接口请求处理结果：" + JSONUtil.toJsonStr(result), requestPath);
        } else {
            logger.info("请求路径：{}", requestPath);
            logger.info("请求参数：{}", JSONUtil.toJsonStr(parameMap));
            logger.info("[{}]接口请求处理结果：" + JSONUtil.toJsonStr(result), requestPath);
        }
        return result;
    }

}
