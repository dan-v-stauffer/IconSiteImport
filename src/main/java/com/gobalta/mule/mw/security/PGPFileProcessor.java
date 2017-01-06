package com.gobalta.mule.mw.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.ResourceUtils;

public class PGPFileProcessor {

    private String passphrase;
    private String secretKeyFileName;
    private String outputFileDirectory = "";

    public void decrypt(InputStream in, String originalFileName) throws Exception {
    	StringBuilder outputFile = new StringBuilder();
    	outputFile.append(outputFileDirectory).append("/");
    	outputFile.append(new SimpleDateFormat("yyyy_MM_dd_hhmmss").format(new Date()));
    	outputFile.append("_").append(originalFileName);
        FileInputStream keyIn = new FileInputStream(ResourceUtils.getFile(secretKeyFileName));
        FileOutputStream out = new FileOutputStream(outputFile.toString());
        PGPUtils.decryptFile(in, out, keyIn, passphrase.toCharArray());
        in.close();
        out.close();
        keyIn.close();
    }
    

    public void setPassphrase(String passphrase) {
            this.passphrase = passphrase;
    }

    public void setSecretKeyFileName(String secretKeyFileName) {
            this.secretKeyFileName = secretKeyFileName;
    }

	public void setOutputFileDirectory(String outputFileDirectory) {
		this.outputFileDirectory = outputFileDirectory;
	}
    
}