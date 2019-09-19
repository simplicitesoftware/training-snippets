package com.simplicite.objects.Training;

import java.util.*;
import com.simplicite.util.*;
import com.simplicite.util.tools.*;

/**
 * Business object TrnProduct
 * see https://www.simplicite.io/resources/4.0/javadoc/com/simplicite/util/ObjectDB.html
 * and https://www.simplicite.io/resources/4.0/javadoc/com/simplicite/util/ObjectField.html
 */
public class TrnProduct extends ObjectDB {
	private static final long serialVersionUID = 1L;

	// to be called by specific action
	public void addStock(){
		ObjectField stock = getField("trnPrdStock");
		stock.setValue(stock.getInt(0)+10);
		save();
	}

	// to be called by TrnOrder
	public void decreaseStock(int qte){
		ObjectField prdStock = getField("trnPrdStock");
		prdStock.setValue(prdStock.getInt(0)-qte);
		save();
	}
}
