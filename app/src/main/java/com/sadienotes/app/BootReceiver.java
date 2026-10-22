package com.sadienotes.app;
import android.content.*;
import org.json.*;
public class BootReceiver extends BroadcastReceiver {
 @Override public void onReceive(Context c,Intent i){if(!Intent.ACTION_BOOT_COMPLETED.equals(i.getAction()))return;try{ReminderReceiver.schedule(c,new JSONArray(c.getSharedPreferences("reminders",0).getString("items","[]")));}catch(Exception ignored){}}
}
