package com.sadienotes.app;
import android.app.*;
import android.content.*;
import android.net.Uri;
import org.json.*;
public class ReminderReceiver extends BroadcastReceiver {
 static PendingIntent pending(Context c,String id){return PendingIntent.getBroadcast(c,0,new Intent(c,ReminderReceiver.class).setData(Uri.parse("sadienotes://reminder/"+Uri.encode(id))).putExtra("note",id),PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);}
 static void schedule(Context c,JSONArray list)throws Exception{AlarmManager a=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);for(int i=0;i<list.length();i++){JSONObject item=list.getJSONObject(i);long time=item.getLong("time");if(time>System.currentTimeMillis())a.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time,pending(c,item.getString("id")));}}
 static void replace(Context c,JSONArray next)throws Exception{android.content.SharedPreferences p=c.getSharedPreferences("reminders",0);JSONArray old=new JSONArray(p.getString("items","[]"));AlarmManager a=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE);for(int i=0;i<old.length();i++)a.cancel(pending(c,old.getJSONObject(i).getString("id")));p.edit().putString("items",next.toString()).apply();schedule(c,next);}
 @Override public void onReceive(Context c,Intent i){String id=i.getStringExtra("note");if(id==null)return;NotificationManager nm=(NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);nm.createNotificationChannel(new NotificationChannel("notes","Not hatırlatıcıları",NotificationManager.IMPORTANCE_DEFAULT));PendingIntent open=PendingIntent.getActivity(c,0,new Intent(c,MainActivity.class).setData(Uri.parse("sadienotes://note/"+Uri.encode(id))).putExtra("note",id),PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);try{nm.notify(id,1,new Notification.Builder(c,"notes").setSmallIcon(com.sadienotes.app.R.drawable.ic_launcher).setContentTitle("SadieNotes hatırlatıcı").setContentText("Notunu gözden geçirme zamanı.").setVisibility(Notification.VISIBILITY_PRIVATE).setContentIntent(open).setAutoCancel(true).build());}catch(SecurityException ignored){}}
}
