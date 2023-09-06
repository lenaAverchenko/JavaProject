package telran.functionality.com.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
/**
 * Class to log every step of using the app with AOP
 *
 * @author Olena Averchenko
 * */
@Component
@Aspect
@Slf4j
public class LoggerAspect {

    /**
     * Method surrounded "around" annotation for logging in packages "controller" and "converter"
     * @param joinPoint place to use aspect
     * @throws Throwable
     * @return Object result of tryProceedingMethod(joinPoint) method
     * */
    @Around(value = "execution(* telran.functionality.com.controller..*.*(..)) ||" +
            "execution(* telran.functionality.com.converter..*.*(..))")
    public  Object adviceAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        log.info("Method {} from class: {} is going to be started.", methodName, joinPoint.getSignature().getDeclaringTypeName());
        return tryProceedingMethod(joinPoint);
    }

    /**
     * Method surrounded "around" annotation for logging in package "service"
     * @param joinPoint place to use aspect
     * @throws Throwable
     * @return Object result of tryProceedingMethod(joinPoint) method
     * */
    @Around(value = "execution(* telran.functionality.com.service..*.*(..))")
    public  Object adviceAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        log.info("Method {} from class: {} is going to be started.", methodName, joinPoint.getSignature().getDeclaringTypeName());

        if (joinPoint.getArgs() != null){
            List<String> inputValues = Arrays.stream(joinPoint.getArgs()).map(Object::toString).toList();
            log.info("Input parameters are: {}",inputValues);
        }
        return tryProceedingMethod(joinPoint);
    }

    /**
     * Method surrounded "around" annotation for logging in packages "controller" and "converter"
     * @param joinPoint place to use aspect
     * @throws Throwable
     * @return Object result of executing any method
     * */
    public  Object tryProceedingMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnValue = null;
        try{
            returnValue = joinPoint.proceed();
            if (returnValue != null){
                log.info("Method {} has ended. The results are: {}", joinPoint.getSignature().getName(), returnValue);
            } else{
                log.info("Method {} has ended.", joinPoint.getSignature().getName());
            }
        } catch (Exception exception) {
            log.info("Exception {} has occurred. Notification is: {}", exception.getClass().getName(), exception.getMessage());
            throw exception;
        }
        return returnValue;
    }
}

