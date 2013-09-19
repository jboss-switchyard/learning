package mortgages;

import javax.inject.Named;

import org.apache.camel.Exchange;
import org.switchyard.bus.camel.audit.Auditor;
import org.switchyard.bus.camel.processors.Processors;

@Named("MyInspector")
public class MyInspector implements Auditor {

    public void beforeCall(Processors processor, Exchange exchange) {
        System.out.println("MyInspector :: before processor " + processor.name());
    }

    public void afterCall(Processors processor, Exchange exchange) {
        System.out.println("MyInspector :: after processor " + processor.name());
    }

}