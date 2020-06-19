package in.co.tsmith.tpmobilepos;

import org.json.JSONObject;

class CreditCardList {
    private String Cardid,Cardname,Company;
    public CreditCardList(String Cid, String Cname, String company) {
        this.Cardid = Cid;
        this.Cardname= Cname;
        this.Company = company;

    }
    public String getCardid() {
        return Cardid;
    }

    public String getCardname() {
        return Cardname;
    }

    public String getCompany() {
        return Company;
    }

}
