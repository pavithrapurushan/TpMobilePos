package in.co.tsmith.tpmobilepos;

public class ProductModel {
    String slno,name,code,mnfr,mrp,soh,pmid;

    public ProductModel(String slno,String name,String code,String mnfr,String mrp,String soh,String pmid){
        this.slno=slno;
        this.name=name;
        this.code=code;
        this.mnfr=mnfr;
        this.mrp=mrp;
        this.soh=soh;
        this.pmid=pmid;

       // this.eancode=eancode;
       // this.location=location;

    }

    public String getSlno(){
        return slno;
    }
    public String getName(){
        return name;
    }
    public String getCode(){
        return code;
    }
    public String getMnfr(){
        return mnfr;
    }
    public String getMrp(){
        return mrp;
    }
    public String getSoh(){
        return soh;
    }
    public String getPmid(){
        return pmid;
    }

//    public String getEancode(){
//        return eancode;
//    }
//    public String getLocation(){
//        return location;
//    }
}
