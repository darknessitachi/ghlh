package com.ghlh.data.db;

public class StockreportVO implements java.io.Serializable{
  public static final String TABLE_NAME = "stockreport";
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
  private boolean hasType;
  public boolean isHasType(){
    return hasType;
  }
  private boolean whereType;
  public boolean isWhereType(){
    return this.whereType;
  }
  public void setWhereType(boolean whereType){
    this.whereType = whereType;
  }
  private int type;
  public void setType(int type){
    this.hasType = true;
    this.type = type;
  }
  public int getType(){
    return type;
  }
  private boolean hasMairu;
  public boolean isHasMairu(){
    return hasMairu;
  }
  private boolean whereMairu;
  public boolean isWhereMairu(){
    return this.whereMairu;
  }
  public void setWhereMairu(boolean whereMairu){
    this.whereMairu = whereMairu;
  }
  private int mairu;
  public void setMairu(int mairu){
    this.hasMairu = true;
    this.mairu = mairu;
  }
  public int getMairu(){
    return mairu;
  }
  private boolean hasZengchi;
  public boolean isHasZengchi(){
    return hasZengchi;
  }
  private boolean whereZengchi;
  public boolean isWhereZengchi(){
    return this.whereZengchi;
  }
  public void setWhereZengchi(boolean whereZengchi){
    this.whereZengchi = whereZengchi;
  }
  private int zengchi;
  public void setZengchi(int zengchi){
    this.hasZengchi = true;
    this.zengchi = zengchi;
  }
  public int getZengchi(){
    return zengchi;
  }
  private boolean hasZhongxin;
  public boolean isHasZhongxin(){
    return hasZhongxin;
  }
  private boolean whereZhongxin;
  public boolean isWhereZhongxin(){
    return this.whereZhongxin;
  }
  public void setWhereZhongxin(boolean whereZhongxin){
    this.whereZhongxin = whereZhongxin;
  }
  private int zhongxin;
  public void setZhongxin(int zhongxin){
    this.hasZhongxin = true;
    this.zhongxin = zhongxin;
  }
  public int getZhongxin(){
    return zhongxin;
  }
  private boolean hasJianchi;
  public boolean isHasJianchi(){
    return hasJianchi;
  }
  private boolean whereJianchi;
  public boolean isWhereJianchi(){
    return this.whereJianchi;
  }
  public void setWhereJianchi(boolean whereJianchi){
    this.whereJianchi = whereJianchi;
  }
  private int jianchi;
  public void setJianchi(int jianchi){
    this.hasJianchi = true;
    this.jianchi = jianchi;
  }
  public int getJianchi(){
    return jianchi;
  }
  private boolean hasMaichu;
  public boolean isHasMaichu(){
    return hasMaichu;
  }
  private boolean whereMaichu;
  public boolean isWhereMaichu(){
    return this.whereMaichu;
  }
  public void setWhereMaichu(boolean whereMaichu){
    this.whereMaichu = whereMaichu;
  }
  private int maichu;
  public void setMaichu(int maichu){
    this.hasMaichu = true;
    this.maichu = maichu;
  }
  public int getMaichu(){
    return maichu;
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
