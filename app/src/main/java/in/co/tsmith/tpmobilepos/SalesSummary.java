package in.co.tsmith.tpmobilepos;

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
    public String TotalDiscount;
    public String Addtions;
    public String RoundOff;
    public String NetAmount;
    public Salesdetail SalesDetail;
    public CustomerPL Customer;

    public SalesSummary() {
        ErrorStatus = 0;
        Message = "";
    }

    public int ErrorStatus;
    public String Message;

}
