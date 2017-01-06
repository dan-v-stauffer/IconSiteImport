package com.gobalta.mule.mw.security;

import org.mule.transport.sftp.SftpInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DecryptComponent {

	private static final Logger LOGGER = LoggerFactory.getLogger(DecryptComponent.class);
	private PGPFileProcessor pgpFileProcessor;
	
	public void decrypt(SftpInputStream is) throws Exception {
		LOGGER.info("Received a file for decryption {}",is.getFileName());
		pgpFileProcessor.decrypt(is, is.getFileName());
	}

	public void setPgpFileProcessor(PGPFileProcessor pgpFileProcessor) {
		this.pgpFileProcessor = pgpFileProcessor;
	}
	
}
