package in.co.tsmith.tpmobilepos;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsResponsePL {

    public ProductDetailsResponsePL() {
        Product = new ArrayList<Products>();
    }

    public List<Products> Product;
    public int ErrorStatus;
    public String Message;
}
