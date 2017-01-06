package com.gobalta.mule.mw.transformers.csv;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.mule.api.transformer.TransformerException;
import org.mule.config.i18n.MessageFactory;
import org.mule.transformer.AbstractTransformer;
import org.mule.util.IOUtils;

public abstract class AbstractCSVTransformer extends AbstractTransformer{

	protected static String DEFAULT_QUALIFIER = "\"";
	protected static String DEFAULT_DELIMITER = ",";

	protected String delimiter = DEFAULT_DELIMITER;
	protected String qualifier = DEFAULT_QUALIFIER;

	protected boolean ignoreFirstRecord = false;
	protected String mappingFile;
	protected String pzmap;
	
	
	public char getDelimiterChar() {
		return this.delimiter.charAt(0);
	}

	public String getDelimiter() {
		return this.delimiter;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = StringEscapeUtils.unescapeJava(delimiter);
	}

	public char getQualifierChar() {
		return this.qualifier.charAt(0);
	}

	public String getQualifier() {
		return this.qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = StringEscapeUtils.unescapeJava(qualifier);
	}

	public String getMappingFile() {
		return this.mappingFile;
	}

	public void setMappingFile(String mappingFile) {
		this.mappingFile = mappingFile;
	}

	public boolean isIgnoreFirstRecord() {
		return this.ignoreFirstRecord;
	}

	public void setIgnoreFirstRecord(boolean ignoreFirstRecord) {
		this.ignoreFirstRecord = ignoreFirstRecord;
	}
	
	
	public Reader createMappingReader() throws TransformerException {
		if (this.pzmap != null) {
			return new StringReader(this.pzmap);
		}

		try {
			this.pzmap = FileUtils.readFileToString(new File(this.mappingFile));
			return new StringReader(this.pzmap);
		} catch (Exception localException1) {
			try {
				this.pzmap = IOUtils.getResourceAsString(this.mappingFile,
						super.getClass());
				return new StringReader(this.pzmap);
			} catch (Exception localException2) {
				throw new TransformerException(
						MessageFactory
								.createStaticMessage("No mapping file found on filesystem or classpath"));
			}
		}
	}
}
