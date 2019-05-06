package be.ordina.beershop.exception;

import be.ordina.beershop.domain.exception.DomainException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class DomainExceptionHandlingAspect {

    @Around("execution(public * be.ordina.beershop..*FacadeImpl.*(..))")
    protected Object invokeUnderTrace(ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            return joinPoint.proceed();
        } catch (DomainException domainException) {
            throw new BusinessException(
                    domainException.getErrorCode().getCode(),
                    domainException.getMessage(),
                    domainException.getCause()
            );
        }
    }
}