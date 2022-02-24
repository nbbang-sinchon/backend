package nbbang.com.nbbang.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MemberIdAspect {
    @Around("@annotation(MemberIdCheck)")
    public Object findMemberIdFromSession(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();
        System.out.println("hello there");
        return proceed;
    }
}
