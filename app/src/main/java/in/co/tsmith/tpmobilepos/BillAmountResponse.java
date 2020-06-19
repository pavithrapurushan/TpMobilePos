package in.co.tsmith.tpmobilepos;

public class BillAmountResponse {
    public BillAmountResponse()
    {
        ErrorStatus = 0;
        Message = "";
    }
//    public Salesbill SalesSummary;

    public SalesSummary SalesSummary;
    public int ErrorStatus;
    public String Message;

}
