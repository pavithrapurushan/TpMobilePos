package in.co.techsmith.tpmobilepos;

public class LoyaltyCustomerList {
    private String lid,lnumber,cname,pnumber,type,mailid;
    public LoyaltyCustomerList(String id, String number, String name, String phone, String type, String s) {
        this.lid=id;
        this.lnumber=number;
        this.cname=name;
        this.pnumber=phone;
        this.type=type;
        this.mailid=s;

    }





    public String getid() {
        return lid;
    }

    public String getNumber() {
        return lnumber;
    }

    public String getName() {
        return cname;
    }

    public String getphoneNumber() {
        return pnumber;
    }

    public String getType() {
        return type;
    }


    public String getMailid() {
        return mailid;
    }
}

