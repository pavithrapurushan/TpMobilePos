package in.co.techsmith.tpmobilepos;

import java.util.ArrayList;
import java.util.List;

public class CustomerDetailsResponse {

    public CustomerDetailsResponse() {
        CustomerDetail = new ArrayList<CustomerDetail>();
    }
    public List<CustomerDetail> CustomerDetail;
    public int ErrorStatus;
    public String Message;
}
