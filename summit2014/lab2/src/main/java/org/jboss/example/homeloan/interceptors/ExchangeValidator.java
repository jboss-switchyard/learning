/*
 * 2012-3 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
