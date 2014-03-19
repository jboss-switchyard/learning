package org.jboss.example.homeloan;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;
import javax.xml.namespace.QName;

import org.switchyard.Exchange;
import org.switchyard.ExchangeInterceptor;
import org.switchyard.HandlerException;
import org.switchyard.label.BehaviorLabel;

@Named("ContentType")
public class ContentTypeInterceptor implements ExchangeInterceptor {

	@Override
	public void before(String target, Exchange exchange) {
		// NOP
	}

	@Override
	public void after(String target, Exchange exchange) throws HandlerException {
		QName msgType = exchange.getContract().getProviderOperation().getOutputType();
		exchange.getMessage().getContext().setProperty(
				Exchange.CONTENT_TYPE, msgType).addLabels(BehaviorLabel.TRANSIENT.label());
	}

	@Override
	public List<String> getTargets() {
		return Arrays.asList(PROVIDER);
	}

}
