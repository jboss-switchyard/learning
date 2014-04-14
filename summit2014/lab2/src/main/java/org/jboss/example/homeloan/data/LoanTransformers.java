package org.jboss.example.homeloan.data;

import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;

public final class LoanTransformers {

    @Transformer(from = "{http://jboss.com/demo/products/fsw/6.0/Application.xsd}Application")
    public LoanApplication transformApplicationToLoanApplication(Element from) {
        return LoanApplication.fromElement(from);
    }

}
