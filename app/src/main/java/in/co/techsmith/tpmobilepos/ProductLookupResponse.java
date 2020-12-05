package in.co.techsmith.tpmobilepos;


//commenetd  by Pavithra on 04-12-2020
//public class ProductLookupResponse {
//    public ProductLookupResponse() {
//        Productlookup = new Productlookup();
//    }
//
//    public Productlookup Productlookup;
//}



//Following added by Pavithra on 04-12-2020
public class ProductLookupResponse {

    public ProductLookupResponse()
    {
        Productlookup = new ProductlookupResult();
    }
    public ProductlookupResult Productlookup ;
    public int ErrorStatus ;
    public String Message ;
}