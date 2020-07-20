package in.co.techsmith.tpmobilepos;

import java.util.ArrayList;

class LoyaltyCustomerLookup {

    public LoyaltyCustomerLookup()
    {
        LoyaltyCustomer = new ArrayList<LoyaltyCustomer>();
    }
    public ArrayList<LoyaltyCustomer> LoyaltyCustomer;
    public int ErrorStatus;
    public String Message;
}
