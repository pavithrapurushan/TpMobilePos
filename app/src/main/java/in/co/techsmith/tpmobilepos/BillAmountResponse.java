package in.co.techsmith.tpmobilepos;

public class BillAmountResponse {
    public BillAmountResponse() {
        ErrorStatus = 0;
        Message = "";
    }

    public SalesSummary SalesSummary;
    public int ErrorStatus;
    public String Message;

}
