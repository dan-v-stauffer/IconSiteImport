package com.gobalta.mule.mw.transformers.csv;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.flatpack.DataError;
import net.sf.flatpack.DataSet;
import net.sf.flatpack.DefaultParserFactory;
import net.sf.flatpack.Parser;

import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.types.DataTypeFactory;

public class CSVToMapsTransformer extends AbstractCSVTransformer {

	public CSVToMapsTransformer() {
		registerSourceType(DataTypeFactory.BYTE_ARRAY);
		registerSourceType(DataTypeFactory.STRING);
		setReturnDataType(DataTypeFactory.create(List.class));
	}
	
	protected Object doTransform(Object src, String encoding)
			throws TransformerException {
		if (src == null) {
			return null;
		}

		Reader dataSource = createReader(src);

		Parser parser = DefaultParserFactory.getInstance().newDelimitedParser(
				createMappingReader(), dataSource, getDelimiterChar(),
				getQualifierChar(), this.ignoreFirstRecord);

		DataSet dataSet = parser.parse();

		if (dataSet == null) {
			throw new TransformerException(
					MessageFactory
							.createStaticMessage("Could not create a data set"));
		}
		if (dataSet.getErrorCount() > 0) {
			throw new TransformerException(
					buildErrorMessage(dataSet.getErrors()));
		}

		return datasetToMaps(dataSet);
	}
	
	private Reader createReader(Object src) throws TransformerException {
		if (src instanceof byte[]) {
			byte[] bytes = (byte[]) src;
			return new StringReader(new String(bytes));
		}
		if (src instanceof String) {
			return new StringReader((String) src);
		}

		String message = "Could not create a transformer for input of type "
				+ src.getClass().getName();
		throw new TransformerException(
				MessageFactory.createStaticMessage(message));
	}

	@SuppressWarnings("rawtypes")
	private Message buildErrorMessage(List errors) {
		StringBuilder message = new StringBuilder(128);

		Iterator errorIter = errors.iterator();
		while (errorIter.hasNext()) {
			DataError error = (DataError) errorIter.next();
			message.append("line ");
			message.append(error.getLineNo());
			message.append(": ");
			message.append(error.getErrorDesc());

			if (!(errorIter.hasNext()))
				continue;
			message.append("\n");
		}

		return MessageFactory.createStaticMessage(message.toString());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map<String, String>> datasetToMaps(DataSet dataSet) {
		if (dataSet == null) {
			return null;
		}

		String[] headers = dataSet.getColumns();
		List rowList = new ArrayList(dataSet.getRowCount());

		while (dataSet.next()) {
			Map row = new HashMap();

			for (int i = 0; i < headers.length; ++i) {
				String key = headers[i];
				String value = dataSet.getString(key);
				row.put(key, value);
			}

			rowList.add(row);
		}

		return rowList;
	}
	
}
