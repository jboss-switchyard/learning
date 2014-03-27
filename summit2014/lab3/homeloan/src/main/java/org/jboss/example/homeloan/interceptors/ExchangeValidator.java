package org.jboss.example.homeloan.interceptors;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.overlord.rtgov.switchyard.exchange.AbstractExchangeValidator;
import org.switchyard.Exchange;
import org.switchyard.ExchangeInterceptor;
import org.switchyard.ExchangePhase;
import org.switchyard.HandlerException;

/**
 * This class observes exchanges and validates them against validation policies.
 *
 */
@Named("ExchangeValidator")
public class ExchangeValidator extends AbstractExchangeValidator implements ExchangeInterceptor {
    
    /**
     * {@inheritDoc}
     */
    public void before(String target, Exchange exchange) throws HandlerException {
        if (exchange.getPhase() == ExchangePhase.IN) {
            handleExchange(exchange);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void after(String target, Exchange exchange) throws HandlerException {
        if (exchange.getPhase() == ExchangePhase.OUT) {
            handleExchange(exchange);
        }
    }
    
    @Override
    public List<String> getTargets() {
        return Arrays.asList(PROVIDER);
    }
}
