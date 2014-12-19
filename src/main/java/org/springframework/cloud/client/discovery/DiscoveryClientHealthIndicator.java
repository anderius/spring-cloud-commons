package org.springframework.cloud.client.discovery;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;

/**
 * @author Spencer Gibb
 */
@Slf4j
public class DiscoveryClientHealthIndicator implements ApplicationContextAware, DiscoveryHealthIndicator, Ordered {

    private ApplicationContext context;

	private int order = Ordered.HIGHEST_PRECEDENCE;
	private DiscoveryClient discoveryClient;

	public DiscoveryClientHealthIndicator(DiscoveryClient discoveryClient) {
		this.discoveryClient = discoveryClient;
	}

	@Override
	public Health health() {
		Health.Builder builder = new Health.Builder();
        try {
			List<String> services = discoveryClient.getServices();
            builder.status(new Status("UP", discoveryClient.description()))
					.withDetail("services", services);
        } catch (Exception e) {
            log.error("Error", e);
            builder.down(e);
        }
		return builder.build();
    }

	@Override
	public String getName() {
		return "discoveryClient";
	}

	@Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

	@Override
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
