package in.co.techsmith.tpmobilepos;


import java.util.ArrayList;
import java.util.List;

//Created by Pavithra on 04-12-2020

public class ProductlookupResult {

    public ProductlookupResult()
    {
        Product = new ArrayList<>();
    }
    public List<ProductRQPL> Product;
    public int ErrorStatus;
    public String Message;
}
