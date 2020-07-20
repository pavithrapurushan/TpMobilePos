package in.co.techsmith.tpmobilepos;

import java.util.ArrayList;
import java.util.List;

public class Salesdetail {

    public Salesdetail()
    {
        BillRow = new ArrayList<>();
    }
    public List<Billrow> BillRow;
    public int ErrorStatus;
    public String Message;
}
