package in.co.techsmith.tpmobilepos;

//Modified by Pavithra on 30-07-2020
public class SalesSummary {
    public String BillSeries;
    public String BillNo;
    public String BillDate;
    public String CustType;
    public String StoreId;
    public String TotalAmount;
    public String TotalLinewiseTax;
    public String DiscountPer;
    public String DiscountAmt;
    public String SchemeDiscount;
    public String CardDiscount;
    public String TotalLinewiseDisc;  //added by Pavithra on 30-07-2020
    public String TotalDiscount;
    public String Addtions;
    public String RoundOff;
    public String NetAmount;





    public Salesdetail SalesDetail;
    public CustomerPL Customer;

    public String CustAddress1;
    public String CustMobile;
    public String CustPhone;  //This stores 2nd phone

    public SalesSummary() {
        ErrorStatus = 0;
        Message = "";
    }

    public int ErrorStatus;
    public String Message;

}
