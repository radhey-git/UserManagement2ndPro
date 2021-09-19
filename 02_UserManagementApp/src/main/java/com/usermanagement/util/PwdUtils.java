package com.usermanagement.util;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.usermanagement.exception.PasswordEncryptDecryptException;
import com.usermanagement.exception.UserManagementAppException;

public class PwdUtils {
	private static final String SECRET_KEY = "abc123xyz123abcd";
	private static final String INIT_VECTOR = "abc123xyz123abcd";

	private static Logger logger=LoggerFactory.getLogger(PwdUtils.class);
	
	private PwdUtils() {
	}

	public static String encryptPwd(String pazzword) throws UserManagementAppException {
		byte[] encrypted = null;
		logger.debug("Request {}",pazzword);
		try {
			IvParameterSpec ivParamSpec = new IvParameterSpec(INIT_VECTOR.getBytes());

			SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParamSpec);

			encrypted = cipher.doFinal(pazzword.getBytes());
			
		} catch (Exception e) {
			logger.error("Error Occured in Pazzword Encryption",new PasswordEncryptDecryptException(e.getMessage()),e);
		}
		String encryptPazzword = Base64.getEncoder().encodeToString(encrypted);
		logger.debug("Response {}",encryptPazzword);
		logger.info("Response {}",encryptPazzword);
		return encryptPazzword;
	}

	public static String decryptPwd(String pazzword) throws PasswordEncryptDecryptException {
		byte[] decrypted = null;
		try {
			IvParameterSpec ivParamSpec = new IvParameterSpec(INIT_VECTOR.getBytes());

			SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");

			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParamSpec);

			byte[] decodedMsg = Base64.getDecoder().decode(pazzword);

			decrypted = cipher.doFinal(decodedMsg);
		} catch (Exception e) {
			logger.error("Pazzword Encryption Error",new PasswordEncryptDecryptException(e.getMessage()),e);
		}

		return new String(decrypted);

	}
}
