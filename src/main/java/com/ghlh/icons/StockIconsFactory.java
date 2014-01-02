package com.ghlh.icons;

import javax.swing.*;

import com.jidesoft.icons.IconsFactory;

/**
 * A helper class to contain icons for demo of JIDE products. Those icons are copyrighted by JIDE Software, Inc.
 */
public class StockIconsFactory {
  
    public static final String Title32 = "Stocks.png";
    public static final String Title48 = "Stocks48.png";
    public static final String Title64 = "Stocks64.png";    
    public static final String TabFeed32 = "Rss-32.png";
    public static final String TabConfig32 = "Configuration-32.png";
    public static final String TabDecision32 = "Green-Cart-32.png";
    public static final String PageFinancial = "Stocks48.png";
   

    public static final String LOGO = "images/logo.png";

    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(StockIconsFactory.class, name);
        else
            return null;
    }
}
