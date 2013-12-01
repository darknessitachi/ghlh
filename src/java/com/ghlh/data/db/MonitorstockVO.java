package com.ghlh.data.db;

public class MonitorstockVO implements java.io.Serializable{
  public static final String TABLE_NAME = "monitorstock";
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
  private boolean hasName;
  public boolean isHasName(){
    return hasName;
  }
  private boolean whereName;
  public boolean isWhereName(){
    return this.whereName;
  }
  public void setWhereName(boolean whereName){
    this.whereName = whereName;
  }
  private String name;
  public void setName(String name){
    this.hasName = true;
    this.name = name;
  }
  public String getName(){
    return name;
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
  private boolean hasAdditioninfo;
  public boolean isHasAdditioninfo(){
    return hasAdditioninfo;
  }
  private boolean whereAdditioninfo;
  public boolean isWhereAdditioninfo(){
    return this.whereAdditioninfo;
  }
  public void setWhereAdditioninfo(boolean whereAdditioninfo){
    this.whereAdditioninfo = whereAdditioninfo;
  }
  private String additioninfo;
  public void setAdditioninfo(String additioninfo){
    this.hasAdditioninfo = true;
    this.additioninfo = additioninfo;
  }
  public String getAdditioninfo(){
    return additioninfo;
  }
  private boolean hasOnmonitoring;
  public boolean isHasOnmonitoring(){
    return hasOnmonitoring;
  }
  private boolean whereOnmonitoring;
  public boolean isWhereOnmonitoring(){
    return this.whereOnmonitoring;
  }
  public void setWhereOnmonitoring(boolean whereOnmonitoring){
    this.whereOnmonitoring = whereOnmonitoring;
  }
  private String onmonitoring;
  public void setOnmonitoring(String onmonitoring){
    this.hasOnmonitoring = true;
    this.onmonitoring = onmonitoring;
  }
  public String getOnmonitoring(){
    return onmonitoring;
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
