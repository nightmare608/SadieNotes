package com.sadienotes.app;
import android.content.*;
import android.database.*;
import android.net.Uri;
import android.os.*;
import android.provider.OpenableColumns;
import java.io.*;
public class AttachmentProvider extends ContentProvider {
 @Override public boolean onCreate(){return true;}
 String id(Uri u){if(u.getPathSegments().size()!=2)throw new IllegalArgumentException();String id=u.getPathSegments().get(0);if(!id.matches("[a-f0-9-]{36}"))throw new IllegalArgumentException();return id;}
 @Override public String getType(Uri u){id(u);String mime=u.getQueryParameter("mime");return mime!=null&&mime.matches("[a-zA-Z0-9.+-]+/[a-zA-Z0-9.+-]+")?mime:"application/octet-stream";}
 @Override public Cursor query(Uri u,String[] p,String s,String[] args,String sort){id(u);String[] cols=p==null?new String[]{OpenableColumns.DISPLAY_NAME,OpenableColumns.SIZE}:p;MatrixCursor c=new MatrixCursor(cols);Object[] row=new Object[cols.length];for(int i=0;i<cols.length;i++)if(OpenableColumns.DISPLAY_NAME.equals(cols[i]))row[i]=u.getLastPathSegment();c.addRow(row);return c;}
 @Override public ParcelFileDescriptor openFile(Uri u,String mode)throws FileNotFoundException{if(!"r".equals(mode))throw new FileNotFoundException();try{File f=MainActivity.attachment(getContext(),id(u));if(!f.exists())throw new FileNotFoundException();ParcelFileDescriptor[] pipe=ParcelFileDescriptor.createPipe();new Thread(()->{try(OutputStream out=new ParcelFileDescriptor.AutoCloseOutputStream(pipe[1])){out.write(Crypto.decrypt(MainActivity.read(new FileInputStream(f),21*1024*1024)));}catch(Exception e){try{pipe[1].close();}catch(Exception ignored){}}},"SadieNotesAttachment").start();return pipe[0];}catch(Exception e){throw new FileNotFoundException();}}
 @Override public Uri insert(Uri u,ContentValues v){throw new UnsupportedOperationException();}
 @Override public int update(Uri u,ContentValues v,String s,String[] a){throw new UnsupportedOperationException();}
 @Override public int delete(Uri u,String s,String[] a){throw new UnsupportedOperationException();}
}
