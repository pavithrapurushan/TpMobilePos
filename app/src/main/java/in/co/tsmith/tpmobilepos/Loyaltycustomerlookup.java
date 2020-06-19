package in.co.tsmith.tpmobilepos;

import java.util.ArrayList;
import java.util.List;

class LoyaltyCustomerLookup {

    public LoyaltyCustomerLookup()
    {
        LoyaltyCustomer = new ArrayList<LoyaltyCustomer>();
    }
    public ArrayList<LoyaltyCustomer> LoyaltyCustomer;
    public int ErrorStatus;
    public String Message;
}
