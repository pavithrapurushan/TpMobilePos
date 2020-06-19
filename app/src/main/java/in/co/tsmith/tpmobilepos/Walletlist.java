package in.co.tsmith.tpmobilepos;

class Walletlist {

    private String Walletdid,Walletname,Company;
    public Walletlist(String walletid, String walletname, String company) {
        this.Walletdid= walletid;
        this.Walletname= walletname;
        this.Company = company;

    }
    public String getWalletdid() {
        return Walletdid;
    }

    public String getWalletname() {
        return Walletname;
    }

    public String getCompany() {
        return Company;
    }


}
