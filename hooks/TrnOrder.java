package com.simplicite.objects.Training;

import java.util.*;
import com.simplicite.util.*;
import com.simplicite.util.tools.*;

/**
 * Business object TrnOrder
 * see https://www.simplicite.io/resources/4.0/javadoc/com/simplicite/util/ObjectDB.html
 * and https://www.simplicite.io/resources/4.0/javadoc/com/simplicite/util/ObjectField.html
 */
public class TrnOrder extends ObjectDB {
	private static final long serialVersionUID = 1L;

	// created through editor's Snippet (CTRL + space)
	@Override
	public List<String> postValidate() {
		List<String> msgs = new ArrayList<String>();

		// retrieve values of the order's object fields
		// getInt takes a default value as parameter, in case it fails (not a number)
		int qte = getField("trnOrdQuantity").getInt(0);
		int stock = getField("trnPrdStock").getInt(0);

		// The error message must be configured in Administration > Static texts:
		// List "TEXT" | Code "TRN_ERR_QTE_NEG"
		if(qte <= 0)
			msgs.add(Message.formatError("TRN_ERR_QTE_NEG", null, "trnOrdQuantity"));

		if(qte > stock)
			msgs.add(Message.formatError("TRN_ERR_QTE_STOCK", null, "trnOrdQuantity"));

		return msgs;
	}

	@Override
	public String postUpdate() {
		String oldState = getField("trnOrdState").getOldValue();
		String newState = getField("trnOrdState").getValue();
		String productId = getFieldValue("trnOrdPrdId"); //equivalent to getField("").getValue()
		int orderQuantity = getField("trnOrdQuantity").getInt(0);

		// When validating an order
		if("PEN".equals(oldState) && "VAL".equals(newState)){
			// retrieve a "temporary instance" of the product object
			ObjectDB tmp = getGrant().getTmpObject("TrnProduct");
			// cast ObjectDB into TrnProduct, so we can access its specific methods (decreaseStock)
			TrnProduct prd = (TrnProduct) tmp;
			// fill object with DB values based  on row_id
			prd.select(productId);
			// call
			prd.decreaseStock(orderQuantity);
		}

		return null;
	}

	// To be called by the publication action
	public byte[] pubExcel(PrintTemplate pt){
		try{
			ExcelPOITool xls = new ExcelPOITool(true);
			ExcelPOITool.ExcelSheet sheet = xls.newSheet("Feuille 1");
			sheet.addFullRow(0, new String[]{"A", "B", "C"}); // at line 0
			xls.add(sheet);
			return xls.generateToByteArray();
		}
		catch(Exception e){
			AppLog.error(getClass(), "pubExcel", "Excel generation error", e, getGrant());
			return null;
		}
	}
}
