package in.co.techsmith.tpmobilepos;

import java.util.ArrayList;
import java.util.List;

public class LoyaltycustomerDetailsResponse {
    public LoyaltycustomerDetailsResponse()
    {
        LoyaltyCustomerDetail = new ArrayList<Loyaltycustomerdetail>();
    }
    public List<Loyaltycustomerdetail> LoyaltyCustomerDetail; //Actually no need to declare this as list
    public int ErrorStatus;
    public String Message;
}
