package com.ghlh.data.db;

public class StockdailyinfoVO implements java.io.Serializable{
  public static final String TABLE_NAME = "stockdailyinfo";
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
  private boolean hasDate;
  public boolean isHasDate(){
    return hasDate;
  }
  private boolean whereDate;
  public boolean isWhereDate(){
    return this.whereDate;
  }
  public void setWhereDate(boolean whereDate){
    this.whereDate = whereDate;
  }
  private java.util.Date date;
  public void setDate(java.util.Date date){
    this.hasDate = true;
    this.date = date;
  }
  public java.util.Date getDate(){
    return date;
  }
  private boolean hasOpenprice;
  public boolean isHasOpenprice(){
    return hasOpenprice;
  }
  private boolean whereOpenprice;
  public boolean isWhereOpenprice(){
    return this.whereOpenprice;
  }
  public void setWhereOpenprice(boolean whereOpenprice){
    this.whereOpenprice = whereOpenprice;
  }
  private double openprice;
  public void setOpenprice(double openprice){
    this.hasOpenprice = true;
    this.openprice = openprice;
  }
  public double getOpenprice(){
    return openprice;
  }
  private boolean hasCloseprice;
  public boolean isHasCloseprice(){
    return hasCloseprice;
  }
  private boolean whereCloseprice;
  public boolean isWhereCloseprice(){
    return this.whereCloseprice;
  }
  public void setWhereCloseprice(boolean whereCloseprice){
    this.whereCloseprice = whereCloseprice;
  }
  private double closeprice;
  public void setCloseprice(double closeprice){
    this.hasCloseprice = true;
    this.closeprice = closeprice;
  }
  public double getCloseprice(){
    return closeprice;
  }
  private boolean hasHighestprice;
  public boolean isHasHighestprice(){
    return hasHighestprice;
  }
  private boolean whereHighestprice;
  public boolean isWhereHighestprice(){
    return this.whereHighestprice;
  }
  public void setWhereHighestprice(boolean whereHighestprice){
    this.whereHighestprice = whereHighestprice;
  }
  private double highestprice;
  public void setHighestprice(double highestprice){
    this.hasHighestprice = true;
    this.highestprice = highestprice;
  }
  public double getHighestprice(){
    return highestprice;
  }
  private boolean hasLowestprice;
  public boolean isHasLowestprice(){
    return hasLowestprice;
  }
  private boolean whereLowestprice;
  public boolean isWhereLowestprice(){
    return this.whereLowestprice;
  }
  public void setWhereLowestprice(boolean whereLowestprice){
    this.whereLowestprice = whereLowestprice;
  }
  private double lowestprice;
  public void setLowestprice(double lowestprice){
    this.hasLowestprice = true;
    this.lowestprice = lowestprice;
  }
  public double getLowestprice(){
    return lowestprice;
  }
  private boolean hasCreatedtime;
  public boolean isHasCreatedtime(){
    return hasCreatedtime;
  }
  private boolean whereCreatedtime;
  public boolean isWhereCreatedtime(){
    return this.whereCreatedtime;
  }
  public void setWhereCreatedtime(boolean whereCreatedtime){
    this.whereCreatedtime = whereCreatedtime;
  }
  private java.util.Date createdtime;
  public void setCreatedtime(java.util.Date createdtime){
    this.hasCreatedtime = true;
    this.createdtime = createdtime;
  }
  public java.util.Date getCreatedtime(){
    return createdtime;
  }
  private boolean hasLastmodifiedtime;
  public boolean isHasLastmodifiedtime(){
    return hasLastmodifiedtime;
  }
  private boolean whereLastmodifiedtime;
  public boolean isWhereLastmodifiedtime(){
    return this.whereLastmodifiedtime;
  }
  public void setWhereLastmodifiedtime(boolean whereLastmodifiedtime){
    this.whereLastmodifiedtime = whereLastmodifiedtime;
  }
  private java.util.Date lastmodifiedtime;
  public void setLastmodifiedtime(java.util.Date lastmodifiedtime){
    this.hasLastmodifiedtime = true;
    this.lastmodifiedtime = lastmodifiedtime;
  }
  public java.util.Date getLastmodifiedtime(){
    return lastmodifiedtime;
  }
}
