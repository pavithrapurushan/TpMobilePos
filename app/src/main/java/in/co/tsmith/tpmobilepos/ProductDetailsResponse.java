package in.co.tsmith.tpmobilepos;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsResponse {
    public ProductDetailsResponse() {
        ProductDetail = new ArrayList<Productdetail>();
    }

    public List<Productdetail> ProductDetail;
    public int ErrorStatus;
    public String Message;
}