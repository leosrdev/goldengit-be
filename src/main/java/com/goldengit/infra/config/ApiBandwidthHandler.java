package com.goldengit.infra.config;

import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;

public class ApiBandwidthHandler implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //TODO add API bandwidth based on auth token

        HttpSession session = request.getSession(true);
        Bucket bucket = (Bucket) session.getAttribute("bucket");
        if (bucket == null) {
            bucket = createNewBucket();
            session.setAttribute("bucket", bucket);
        }

        if (!bucket.tryConsume(1)) {
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value());
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(limit -> limit.capacity(50).refillGreedy(5, Duration.ofSeconds(10)))
                .build();
    }
}
