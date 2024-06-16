package ru.jordan.food_storage.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@Slf4j
public class RequestLoggingAspect {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Around("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime startTime = LocalDateTime.now();
        String methodName = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();
        log.info("Метод {} начался в {}", methodName, startTime.format(formatter));

        Object proceed = null;
        LocalDateTime endTime = null;
        Duration executionTime = null;
        try {
            proceed = joinPoint.proceed();  // выполнение целевого метода
            endTime = LocalDateTime.now();
            executionTime = Duration.between(startTime, endTime);
        } catch (Throwable throwable) {
            endTime = LocalDateTime.now();
            executionTime = Duration.between(startTime, endTime);
            log.error("Метод {} завершился с ошибкой в {}", methodName, endTime.format(formatter));
            log.error("Метод {}. Время выполнения: {}", methodName, formatDuration(executionTime));
            log.error("Ошибка: {}", throwable.getMessage());
            throw throwable;  // повторное выбрасывание исключения для дальнейшей обработки
        }

        log.info("Метод {} завершился в {}", methodName, endTime.format(formatter));
        log.info("Метод {}. Время выполнения: {}", methodName, formatDuration(executionTime));
        return proceed;
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;
        long millis = duration.toMillis() % 1000;
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }
}
