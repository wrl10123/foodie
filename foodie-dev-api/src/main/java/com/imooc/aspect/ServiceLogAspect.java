package com.imooc.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLogAspect {

    /**
     * AOP通知：
     * 1. 前置通知：在方法调用之前执行
     * 2. 后置通知：在方法正常调用之后执行
     * 3. 环绕通知：在方法调用之前和之后，都分别可以执行
     * 4. 异常通知：在方法调用过程中发生异常，则通知
     * 5. 最终通知：在方法调用之后执行，报异常也会执行
     */

    /**
     * 切面表达式：
     * execution 代表所要执行的表达式主体
     *
     * 第一处 * 代表方法返回类型，表示所有类型
     * 第二处 包名 代表aop监控的类所在的包
     * 第三处 .. 代表包以及其子包下的所有类的方法
     * 地四处 * 代表类名，表示所有类
     * 第五处 *(..) 代表类中的方法名
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
//    @Around(value = "execution(* com.imooc.service.impl..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("====== 开始执行 {}.{} ",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());
        long beginTime = System.currentTimeMillis();
        //执行目标service
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long takeTime = endTime - beginTime;
        if (takeTime > 3000) {
            log.error("====== 执行结束，耗时:{}", takeTime);
        } else if (takeTime > 2000) {
            log.warn("====== 执行结束，耗时:{}", takeTime);
        } else {
            log.info("====== 执行结束，耗时:{}", takeTime);
        }
        return result;
    }
}
