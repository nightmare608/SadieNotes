package com.sadienotes.app;
import org.json.JSONObject;
import java.util.Arrays;
public class CryptoTest {
 static int checks=0;
 static void check(boolean b,String message){if(!b)throw new AssertionError(message);checks++;}
 static void rejected(String raw,String password)throws Exception{boolean failed=false;try{Crypto.unbackup(raw,password);}catch(Exception e){failed=true;}check(failed,"invalid backup accepted");}
 public static void main(String[] a)throws Exception{
  String original=new JSONObject().put("format","SadieNotesBackup").put("version",1).put("content","Türkçe notlar, emoji 📝, & < >").toString();
  String encrypted=Crypto.backup(original,"GuvenliParola-2026");
  check(!encrypted.contains("Türkçe"),"plain text leaked");
  check(original.equals(Crypto.unbackup(encrypted,"GuvenliParola-2026")),"roundtrip");
  check(!encrypted.equals(Crypto.backup(original,"GuvenliParola-2026")),"random IV/salt");
  check(original.equals(Crypto.backup(original,"")),"plain backup");
  check(original.equals(Crypto.unbackup(original,"")),"plain restore");
  rejected(encrypted,"wrong-password");
  JSONObject mutated=new JSONObject(encrypted);byte[] data=Crypto.un64(mutated.getString("ciphertext"));data[data.length/2]^=1;mutated.put("ciphertext",Crypto.b64(data));rejected(mutated.toString(),"GuvenliParola-2026");
  mutated=new JSONObject(encrypted);mutated.put("iterations",1);rejected(mutated.toString(),"GuvenliParola-2026");
  mutated=new JSONObject(encrypted);mutated.put("iv",Crypto.b64(new byte[4]));rejected(mutated.toString(),"GuvenliParola-2026");
  mutated=new JSONObject(encrypted);mutated.put("salt",Crypto.b64(new byte[2]));rejected(mutated.toString(),"GuvenliParola-2026");
  mutated=new JSONObject(encrypted);mutated.put("version",99);rejected(mutated.toString(),"GuvenliParola-2026");
  byte[] salt=new byte[16];Arrays.fill(salt,(byte)7);check(Arrays.equals(Crypto.derive("password",salt),Crypto.derive("password",salt)),"deterministic derivation");check(!Arrays.equals(Crypto.derive("password",salt),Crypto.derive("password2",salt)),"distinct keys");
  System.out.println("{\"passed\":"+checks+",\"scope\":\"Production Crypto.java backup encryption tests on host JVM; Android Base64 shim; Keystore path requires device\"}");
 }
}
