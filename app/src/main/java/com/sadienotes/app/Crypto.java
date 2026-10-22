package com.sadienotes.app;
import android.content.Context;
import android.security.keystore.*;
import android.util.Base64;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import org.json.*;
final class Crypto {
 static final SecureRandom RANDOM=new SecureRandom();
 static byte[] random(int n){byte[] b=new byte[n];RANDOM.nextBytes(b);return b;}
 static String b64(byte[] b){return Base64.encodeToString(b,Base64.NO_WRAP);}
 static byte[] un64(String s){return Base64.decode(s,Base64.NO_WRAP);}
 static SecretKey key() throws Exception {
  KeyStore store=KeyStore.getInstance("AndroidKeyStore");store.load(null);
  if(!store.containsAlias("sadienotes-data")){KeyGenerator gen=KeyGenerator.getInstance("AES","AndroidKeyStore");gen.init(new KeyGenParameterSpec.Builder("sadienotes-data",KeyProperties.PURPOSE_ENCRYPT|KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).setKeySize(256).build());gen.generateKey();}
  return (SecretKey)store.getKey("sadienotes-data",null);
 }
 static byte[] encrypt(byte[] data) throws Exception {Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.ENCRYPT_MODE,key());byte[] ct=c.doFinal(data),iv=c.getIV();byte[] out=new byte[1+iv.length+ct.length];out[0]=(byte)iv.length;System.arraycopy(iv,0,out,1,iv.length);System.arraycopy(ct,0,out,1+iv.length,ct.length);return out;}
 static byte[] decrypt(byte[] data) throws Exception {if(data.length<30||data[0]!=12)throw new Exception("Invalid data");Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.DECRYPT_MODE,key(),new GCMParameterSpec(128,Arrays.copyOfRange(data,1,13)));return c.doFinal(Arrays.copyOfRange(data,13,data.length));}
 static byte[] derive(String password,byte[] salt) throws Exception {PBEKeySpec spec=new PBEKeySpec(password.toCharArray(),salt,210000,256);try{return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();}finally{spec.clearPassword();}}
 static String backup(String plain,String password) throws Exception {if(password.isEmpty())return plain;byte[] salt=random(16),iv=random(12);Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(derive(password,salt),"AES"),new GCMParameterSpec(128,iv));return new JSONObject().put("format","SadieNotesEncrypted").put("version",1).put("iterations",210000).put("salt",b64(salt)).put("iv",b64(iv)).put("ciphertext",b64(c.doFinal(plain.getBytes(StandardCharsets.UTF_8)))).toString();}
 static String unbackup(String raw,String password) throws Exception {JSONObject obj=new JSONObject(raw);if(!"SadieNotesEncrypted".equals(obj.optString("format")))return raw;if(obj.optInt("version")!=1||obj.optInt("iterations")!=210000)throw new Exception("Unsupported backup");byte[] salt=un64(obj.getString("salt")),iv=un64(obj.getString("iv"));if(salt.length!=16||iv.length!=12)throw new Exception("Invalid backup");Cipher c=Cipher.getInstance("AES/GCM/NoPadding");c.init(Cipher.DECRYPT_MODE,new SecretKeySpec(derive(password,salt),"AES"),new GCMParameterSpec(128,iv));return new String(c.doFinal(un64(obj.getString("ciphertext"))),StandardCharsets.UTF_8);}
}
