package com.ghlh.data.db;

public class StocktradeVO implements java.io.Serializable{
  public static final String TABLE_NAME = "stocktrade";
  private boolean hasId;
  public boolean isHasId(){
    return hasId;
  }
  private boolean whereId;
  public boolean isWhereId(){
    return this.whereId;
  }
  public void setWhereId(boolean whereId){
    this.whereId = whereId;
  }
  private int id;
  public void setId(int id){
    this.hasId = true;
    this.id = id;
  }
  public int getId(){
    return id;
  }
  private boolean hasStockid;
  public boolean isHasStockid(){
    return hasStockid;
  }
  private boolean whereStockid;
  public boolean isWhereStockid(){
    return this.whereStockid;
  }
  public void setWhereStockid(boolean whereStockid){
    this.whereStockid = whereStockid;
  }
  private String stockid;
  public void setStockid(String stockid){
    this.hasStockid = true;
    this.stockid = stockid;
  }
  public String getStockid(){
    return stockid;
  }
  private boolean hasTradealgorithm;
  public boolean isHasTradealgorithm(){
    return hasTradealgorithm;
  }
  private boolean whereTradealgorithm;
  public boolean isWhereTradealgorithm(){
    return this.whereTradealgorithm;
  }
  public void setWhereTradealgorithm(boolean whereTradealgorithm){
    this.whereTradealgorithm = whereTradealgorithm;
  }
  private String tradealgorithm;
  public void setTradealgorithm(String tradealgorithm){
    this.hasTradealgorithm = true;
    this.tradealgorithm = tradealgorithm;
  }
  public String getTradealgorithm(){
    return tradealgorithm;
  }
  private boolean hasBuydate;
  public boolean isHasBuydate(){
    return hasBuydate;
  }
  private boolean whereBuydate;
  public boolean isWhereBuydate(){
    return this.whereBuydate;
  }
  public void setWhereBuydate(boolean whereBuydate){
    this.whereBuydate = whereBuydate;
  }
  private java.util.Date buydate;
  public void setBuydate(java.util.Date buydate){
    this.hasBuydate = true;
    this.buydate = buydate;
  }
  public java.util.Date getBuydate(){
    return buydate;
  }
  private boolean hasBuybaseprice;
  public boolean isHasBuybaseprice(){
    return hasBuybaseprice;
  }
  private boolean whereBuybaseprice;
  public boolean isWhereBuybaseprice(){
    return this.whereBuybaseprice;
  }
  public void setWhereBuybaseprice(boolean whereBuybaseprice){
    this.whereBuybaseprice = whereBuybaseprice;
  }
  private double buybaseprice;
  public void setBuybaseprice(double buybaseprice){
    this.hasBuybaseprice = true;
    this.buybaseprice = buybaseprice;
  }
  public double getBuybaseprice(){
    return buybaseprice;
  }
  private boolean hasBuyprice;
  public boolean isHasBuyprice(){
    return hasBuyprice;
  }
  private boolean whereBuyprice;
  public boolean isWhereBuyprice(){
    return this.whereBuyprice;
  }
  public void setWhereBuyprice(boolean whereBuyprice){
    this.whereBuyprice = whereBuyprice;
  }
  private double buyprice;
  public void setBuyprice(double buyprice){
    this.hasBuyprice = true;
    this.buyprice = buyprice;
  }
  public double getBuyprice(){
    return buyprice;
  }
  private boolean hasNumber;
  public boolean isHasNumber(){
    return hasNumber;
  }
  private boolean whereNumber;
  public boolean isWhereNumber(){
    return this.whereNumber;
  }
  public void setWhereNumber(boolean whereNumber){
    this.whereNumber = whereNumber;
  }
  private int number;
  public void setNumber(int number){
    this.hasNumber = true;
    this.number = number;
  }
  public int getNumber(){
    return number;
  }
  private boolean hasSelldate;
  public boolean isHasSelldate(){
    return hasSelldate;
  }
  private boolean whereSelldate;
  public boolean isWhereSelldate(){
    return this.whereSelldate;
  }
  public void setWhereSelldate(boolean whereSelldate){
    this.whereSelldate = whereSelldate;
  }
  private java.util.Date selldate;
  public void setSelldate(java.util.Date selldate){
    this.hasSelldate = true;
    this.selldate = selldate;
  }
  public java.util.Date getSelldate(){
    return selldate;
  }
  private boolean hasWinsellprice;
  public boolean isHasWinsellprice(){
    return hasWinsellprice;
  }
  private boolean whereWinsellprice;
  public boolean isWhereWinsellprice(){
    return this.whereWinsellprice;
  }
  public void setWhereWinsellprice(boolean whereWinsellprice){
    this.whereWinsellprice = whereWinsellprice;
  }
  private double winsellprice;
  public void setWinsellprice(double winsellprice){
    this.hasWinsellprice = true;
    this.winsellprice = winsellprice;
  }
  public double getWinsellprice(){
    return winsellprice;
  }
  private boolean hasLostsellprice;
  public boolean isHasLostsellprice(){
    return hasLostsellprice;
  }
  private boolean whereLostsellprice;
  public boolean isWhereLostsellprice(){
    return this.whereLostsellprice;
  }
  public void setWhereLostsellprice(boolean whereLostsellprice){
    this.whereLostsellprice = whereLostsellprice;
  }
  private double lostsellprice;
  public void setLostsellprice(double lostsellprice){
    this.hasLostsellprice = true;
    this.lostsellprice = lostsellprice;
  }
  public double getLostsellprice(){
    return lostsellprice;
  }
  private boolean hasStatus;
  public boolean isHasStatus(){
    return hasStatus;
  }
  private boolean whereStatus;
  public boolean isWhereStatus(){
    return this.whereStatus;
  }
  public void setWhereStatus(boolean whereStatus){
    this.whereStatus = whereStatus;
  }
  private int status;
  public void setStatus(int status){
    this.hasStatus = true;
    this.status = status;
  }
  public int getStatus(){
    return status;
  }
  private boolean hasPrevioustradeid;
  public boolean isHasPrevioustradeid(){
    return hasPrevioustradeid;
  }
  private boolean wherePrevioustradeid;
  public boolean isWherePrevioustradeid(){
    return this.wherePrevioustradeid;
  }
  public void setWherePrevioustradeid(boolean wherePrevioustradeid){
    this.wherePrevioustradeid = wherePrevioustradeid;
  }
  private int previoustradeid;
  public void setPrevioustradeid(int previoustradeid){
    this.hasPrevioustradeid = true;
    this.previoustradeid = previoustradeid;
  }
  public int getPrevioustradeid(){
    return previoustradeid;
  }
  private boolean hasComments;
  public boolean isHasComments(){
    return hasComments;
  }
  private boolean whereComments;
  public boolean isWhereComments(){
    return this.whereComments;
  }
  public void setWhereComments(boolean whereComments){
    this.whereComments = whereComments;
  }
  private String comments;
  public void setComments(String comments){
    this.hasComments = true;
    this.comments = comments;
  }
  public String getComments(){
    return comments;
  }
  private boolean hasCreatedtimestamp;
  public boolean isHasCreatedtimestamp(){
    return hasCreatedtimestamp;
  }
  private boolean whereCreatedtimestamp;
  public boolean isWhereCreatedtimestamp(){
    return this.whereCreatedtimestamp;
  }
  public void setWhereCreatedtimestamp(boolean whereCreatedtimestamp){
    this.whereCreatedtimestamp = whereCreatedtimestamp;
  }
  private java.util.Date createdtimestamp;
  public void setCreatedtimestamp(java.util.Date createdtimestamp){
    this.hasCreatedtimestamp = true;
    this.createdtimestamp = createdtimestamp;
  }
  public java.util.Date getCreatedtimestamp(){
    return createdtimestamp;
  }
  private boolean hasLastmodifiedtimestamp;
  public boolean isHasLastmodifiedtimestamp(){
    return hasLastmodifiedtimestamp;
  }
  private boolean whereLastmodifiedtimestamp;
  public boolean isWhereLastmodifiedtimestamp(){
    return this.whereLastmodifiedtimestamp;
  }
  public void setWhereLastmodifiedtimestamp(boolean whereLastmodifiedtimestamp){
    this.whereLastmodifiedtimestamp = whereLastmodifiedtimestamp;
  }
  private java.util.Date lastmodifiedtimestamp;
  public void setLastmodifiedtimestamp(java.util.Date lastmodifiedtimestamp){
    this.hasLastmodifiedtimestamp = true;
    this.lastmodifiedtimestamp = lastmodifiedtimestamp;
  }
  public java.util.Date getLastmodifiedtimestamp(){
    return lastmodifiedtimestamp;
  }
}
