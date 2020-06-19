package in.co.tsmith.tpmobilepos;

import java.util.ArrayList;
import java.util.List;

public class LoyaltycustomerDetailsResponse {
    public LoyaltycustomerDetailsResponse()
    {
        LoyaltyCustomerDetail = new ArrayList<Loyaltycustomerdetail>();
    }
    public List<Loyaltycustomerdetail> LoyaltyCustomerDetail;
    public int ErrorStatus;
    public String Message;
}
