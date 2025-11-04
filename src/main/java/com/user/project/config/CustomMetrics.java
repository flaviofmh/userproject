package com.user.project.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    public CustomMetrics(MeterRegistry meterRegistry) {
        meterRegistry.counter("custom.metric.counter").increment();
        meterRegistry.gauge("custom.metric.gauge", 42);
    }
}