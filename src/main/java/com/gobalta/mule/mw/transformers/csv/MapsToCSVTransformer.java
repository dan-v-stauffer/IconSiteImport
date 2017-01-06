package com.gobalta.mule.mw.transformers.csv;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.flatpack.writer.DelimiterWriterFactory;

import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.types.DataTypeFactory;

public class MapsToCSVTransformer extends AbstractCSVTransformer {

	public MapsToCSVTransformer() {
		setReturnDataType(DataTypeFactory.STRING);
		registerSourceType(DataTypeFactory.create(List.class));
	}

	@SuppressWarnings("rawtypes")
	protected Object doTransform(Object src, String encoding)
			throws TransformerException {
		try {
			StringWriter output = new StringWriter();
			net.sf.flatpack.writer.Writer writer = createWriter(output);

			Iterator rowIter = ((List) src).iterator();
			while (rowIter.hasNext()) {
				Map row = (Map) rowIter.next();
				writeRow(writer, row);
			}

			writer.close();
			return output.toString();
		} catch (IOException iox) {
			throw new TransformerException(this, iox);
		}
	}

	private net.sf.flatpack.writer.Writer createWriter(java.io.Writer output)
			throws TransformerException, IOException {
		if ((this.delimiter != null) && (this.delimiter.length() > 1)) {
			throw new TransformerException(
					MessageFactory
							.createStaticMessage("Delimiter length exceeded"));
		}

		if ((this.qualifier != null) && (this.qualifier.length() > 1)) {
			throw new TransformerException(
					MessageFactory
							.createStaticMessage("Qualifier length exceeded"));
		}

		return new DelimiterWriterFactory(createMappingReader(),
				getDelimiterChar(), getQualifierChar()).createWriter(output);
	}

	@SuppressWarnings("rawtypes")
	private void writeRow(net.sf.flatpack.writer.Writer writer, Map row)
			throws IOException {
		Iterator keyIter = row.keySet().iterator();
		while (keyIter.hasNext()) {
			String key = (String) keyIter.next();
			writer.addRecordEntry(key, row.get(key));
		}
		writer.nextRecord();
	}
}
