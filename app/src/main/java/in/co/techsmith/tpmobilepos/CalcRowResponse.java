package in.co.techsmith.tpmobilepos;

import java.util.ArrayList;
import java.util.List;

public class CalcRowResponse
{
    public CalcRowResponse()
    {
        BillRow= new ArrayList<Billrow>();
    }

    public List<Billrow> BillRow;
    public int ErrorStatus = 0;
    public String Message="";

}
