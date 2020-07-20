package in.co.techsmith.tpmobilepos;

public class CustomerList {
    private String lid,address,cname,pnumber,mailid,fromwhere,customerid;

    public CustomerList(String s, String s1, String s2, String s3, String s4, String s5) {
        this.lid = s;
        this.cname = s1;
        this.address = s2;
        this.pnumber = s3;
        this.fromwhere=s4;
        this.customerid=s5;
    }

    public String getid() {
        return lid;
    }


    public String getName() {
        return cname;
    }

    public String getAddress() {
        return address;
    }

    public String getphoneNumber() {
        return pnumber;
    }

    public String getMailid() {
        return mailid;
    }

    public  String getFromwhere()
    {
        return fromwhere;
    }
    public  String getCustomerid()
    {
        return customerid;
    }
}
