package in.co.techsmith.tpmobilepos;

public class Model  {
    String tvItemName,tvRate,tvUOM,tvDisc,tvTotal,etQty,ItemId,ItemCode,MRP,PackName,UnitName,Company,TaxRate,TaxId,BatchCode,BatchId
            ,BatchExpiry,PackRate,BatchMRP,SOHInUnits,SOHInPacks,QtyInPacks,QtyInUnits,BillingRate,FreeFlag,CustType,Amount,DiscPer,
            DiscPerAmt,TaxableAmt,TaxPer,TaxType,TaxAmt,RowTotal,UperPack;  //Edited by Pavithra on 30-07-2020
//    DiscAmt

    public Model(String ItemId ,String ItemCode,String MRP,String PackName,
                 String UnitName,String Company,String TaxRate,String TaxId, String BatchCode,
                 String BatchId, String BatchExpiry, String PackRate, String BatchMRP, String SOHInUnits,
                 String SOHInPacks,String tvItemName, String tvRate, String tvUOM,String etQty){
        this.tvItemName = tvItemName;
        this.tvRate=tvRate;
        this.tvUOM=tvUOM;
        this.etQty=etQty;
        this.ItemId=ItemId;
        this.ItemCode=ItemCode;
        this.MRP=MRP;
        this.PackName=PackName;
        this.UnitName=UnitName;
        this.Company=Company;
        this.TaxRate=TaxRate;
        this.TaxId=TaxId;
        this.BatchCode=BatchCode;
        this.BatchId=BatchId;
        this.BatchExpiry=BatchExpiry;
        this.PackRate=PackRate;
        this.BatchMRP=BatchMRP;
        this.SOHInUnits=SOHInUnits;
        this.SOHInPacks=SOHInPacks;
        this.UperPack = "0";  //Added by Pavithra on 18-07-2020
        //this.tvTotal=tvTotal;
    }
    public void setTvTotal(String tvTotal)
    {
        this.tvTotal=tvTotal;

    }
    public String ItemId(){
        return ItemId;
    }
    public String ItemCode(){
        return ItemCode;
    }
    public String MRP(){
        return MRP;
    };
    public String PackName(){
        return PackName;
    }
    public String UnitName(){
        return UnitName;
    }
    public String Company(){
        return Company;
    }
    public String TaxRate(){
        return TaxRate;
    }
    public String TaxId(){
        return TaxId;
    }
    public String BatchCode(){
        return BatchCode;
    }
    public String BatchId(){
        return BatchId;
    }
    public String BatchExpiry(){
        return BatchExpiry;
    }
    public String PackRate(){
        return PackRate;
    }
    public String BatchMRP(){
        return BatchMRP;
    }
    public String SOHInUnits(){
        return SOHInUnits;
    }
    public String SOHInPacks(){
        return SOHInPacks;
    }
    public String getTvItemName(){
        return tvItemName;
    }
    public String getTvRate() {
        return tvRate;
    }
    public String getTvQty(){
        //etQty="1";
        return etQty;
}
    public String getTvUOM() {
        return tvUOM;
    }
    public String getTvDisc() {
        //tvDisc="0";
        return tvDisc;
    }
    public String getTvTotal(){
        //tvTotal= String.valueOf(tvRate);
        //SalesActivity s=new SalesActivity();
        //tvTotal=s.tot;
        //tvTotal=String.valueOf(Double.parseDouble(tvRate)*Integer.parseInt(etQty));
        return tvTotal;
    }

}