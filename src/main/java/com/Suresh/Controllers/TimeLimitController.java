package com.Suresh.Controllers;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
public class TimeLimitController {

	private static final Logger logger = LoggerFactory.getLogger(TimeLimitController.class);

	/*
	 * One point is to be noted here that the method which is annotated
	 * with @TimeLimiter should return a type CompletableFuture mandatorily
	 */

	@GetMapping("/getMessage")
	@TimeLimiter(name = "getMessageTL")
	public CompletableFuture<String> getMessage() {

		return CompletableFuture.supplyAsync(this::getResponse);
	}

	private String getResponse() {

		if (Math.random() < 0.4) { // Expected to fail 40% of the time
			return "Executing Within the time Limit...";
		} else {
			try {
				logger.info("Getting Delayed Execution");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return "Exception due to Request Timeout.";
	}
}
