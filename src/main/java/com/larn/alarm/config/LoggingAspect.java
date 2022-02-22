package com.larn.alarm.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingAspect {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Pointcut("execution(public * com.larn.alarm.*.service..*(..))")
	private void serviceTarget() { }

	@Around("serviceTarget()")
	public Object serviceStartAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		Object target = joinPoint.getTarget();
		String className = target.getClass().getName();

		logger.info("=================================================");
		logger.info(">>>>>>>>> LOGGING START >>>>>>>>>>");
		logger.info("Class  ===> {}", className);
		logger.info("Method ===> {}", joinPoint.getSignature().getName() + "()  START!!!");
		StopWatch sw = new StopWatch(); sw.start(); // 비즈니스 로직 (메인 로직)
		Object result = joinPoint.proceed();
		sw.stop();
		logger.info("소요시간: {} ms", sw.getLastTaskTimeMillis());
		logger.info("Class  ===> {}", className);
		logger.info("Method ===> {}", joinPoint.getSignature().getName() + "()  END!!!");
		logger.info(">>>>>>>>>> LOGGING END >>>>>>>>>>");
		logger.info("=================================================");


		return result;
		}
	}

