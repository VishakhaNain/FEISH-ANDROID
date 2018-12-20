package com.app.feish.application.Remote;

import android.util.Log;

import org.b3ds.feish.security.Decryption;
import org.b3ds.feish.security.Encryption;

public  class EncryptionDecryption {
   public static String encode(String data)
    {
        String cipher="";
        try {
            Encryption encryption= new Encryption();
            cipher = encryption.encrypt(data);
        }catch (Exception e)
        {
            Log.i("error is here",""+e.getCause());

        }
        return cipher;
    }
    public static String decode(String data)
    {
        String cipher="";
        try {
            Decryption decryption = new Decryption();
            cipher = decryption.decrypt(data);

        }catch (Exception e)
        {
            Log.i("error is here",""+e.getCause());

        }
        return cipher;
    }
}
