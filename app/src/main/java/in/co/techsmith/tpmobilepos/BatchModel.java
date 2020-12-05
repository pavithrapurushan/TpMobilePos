package in.co.techsmith.tpmobilepos;

public class BatchModel {

    String slno,batchcode,expiry,mrp,soh,batchid;

    public BatchModel(String slno,String batchcode,String expiry,String mrp,String soh,String batchid) {

        this.slno = slno;
        this.batchcode = batchcode;
        //this.barcode = barcode;
        this.expiry = expiry;
        this.mrp = mrp;
        this.batchid = batchid;
        this.soh = soh;
    }

    public String getSlno(){
        return slno;
    }
    public String getBatchcode(){
        return batchcode;
    }
    public String getExpiry(){
        return expiry;
    }
    public String getMrp(){
        return mrp;
    }
    public String getSoh(){
        return soh;
    }
    public String getBatchid() {
        return batchid;
    }

}
