package com.starcom.dater.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.typedarrays.shared.TypedArrays;
import com.google.gwt.typedarrays.shared.Uint8Array;

public abstract class JsCrypt
{
  // Hints:
  // https://stackoverflow.com/questions/18279141/javascript-string-encryption-and-decryption
  // https://github.com/diafygi/webcrypto-examples/#aes-gcm
  // https://stackoverflow.com/questions/45636545/webcrypto-string-encryption-using-user-submitted-password
  
  JavaScriptObject key;
  String clearText;
  Uint8Array encData, iv;
  
  public static int[] toArray(Uint8Array array)
  {
    int[] iArr = new int[array.length()];
    for (int i=0; i<iArr.length; i++)
    {
      iArr[i] = array.get(i);
    }
    return iArr;
  }
  
  public static Uint8Array toArray(int[] iArr)
  {
    Uint8Array array = TypedArrays.createUint8Array(iArr.length);
    array.set(array);
    return array;
  }
  
  public void requestEncryption(String clearText, String passwd)
  {
    this.clearText = clearText;
    if (key == null) { doGenKey(this, passwd); }
    else { onGenKey(key); }
  }
  
  public void requestDecryption(Uint8Array encrypted_data, Uint8Array iv, String passwd)
  {
    this.encData = encrypted_data;
    this.iv = iv;
    if (key == null) { doGenKey(this, passwd); }
    else { onGenKey(key); }
  }
  
  public abstract void responseEncryption(Uint8Array encrypted_data, Uint8Array iv);
  public abstract void responseDecryption(String decryptText);
  
  private void onEncrypt(Uint8Array encrypted_data, Uint8Array iv)
  {
    DaterWebApp.logger.fine("onEncrypt: " + encrypted_data);
    responseEncryption(encrypted_data, iv);
  }
  
  private void onDecrypt(String decryptText)
  {
    DaterWebApp.logger.fine("onDecrypt: " + decryptText);
    responseDecryption(decryptText);
  }
  
  private void onGenKey(JavaScriptObject key)
  {
    DaterWebApp.logger.info("onKey: keyExisting=" + (key!=null) + " keyReused=" + (this.key==key));
    this.key = key;
    if (clearText != null)
    {
      String tmpClearText = clearText;
      clearText = null;
      doEncrypt(this, tmpClearText, key);
    }
    else if (encData != null)
    {
      Uint8Array tmpEncData = encData;
      Uint8Array tmpIv = iv;
      encData = null;
      iv = null;
      doDecrypt(this, tmpEncData, key, tmpIv);
    }
  }
  
  private native void doGenKey(JsCrypt jsCrypt, String password) /*-{
    var key_object = null;
    var promise_key = null;
    
    promise_key = window.crypto.subtle.importKey(
        "raw",
        new TextEncoder("utf-8").encode(password),
        {"name": "PBKDF2"},
        false,
        ["deriveKey"]
    );
    promise_key.then(function(importedPassword) {
        return window.crypto.subtle.deriveKey(
            {
                "name": "PBKDF2",
                "salt": new TextEncoder("utf-8").encode("the salt is this random string"),
                "iterations": 100000,
                "hash": "SHA-256"
            },
            importedPassword,
            {
                "name": "AES-GCM",
                "length": 128
            },
            false,
            ["encrypt", "decrypt"]
        );
    }).then(function(key) {
        key_object = key;
        jsCrypt.@com.starcom.dater.client.JsCrypt::onGenKey(Lcom/google/gwt/core/client/JavaScriptObject;)(key_object);
    });
  }-*/;
  
  private native void doEncrypt(JsCrypt jsCrypt, String secretmessage, JavaScriptObject key_object) /*-{
    var vector = window.crypto.getRandomValues(new Uint8Array(16));
    var encrypted_data = null;
    var encrypt_promise = null;
    encrypt_promise = window.crypto.subtle.encrypt({name: "AES-GCM", iv: vector}, key_object, new TextEncoder("utf-8").encode(secretmessage));
    encrypt_promise.then(
      function(result) {
        encrypted_data = new Uint8Array(result);
        jsCrypt.@com.starcom.dater.client.JsCrypt::onEncrypt(Lcom/google/gwt/typedarrays/shared/Uint8Array;
                                                             Lcom/google/gwt/typedarrays/shared/Uint8Array;)(encrypted_data, vector);
      },
      function(e) {
        alert("Error while encrypting data: " + e.message);
      }
    );
  }-*/;
  
  private native void doDecrypt(JsCrypt jsCrypt, Uint8Array encrypted_data, JavaScriptObject key_object, Uint8Array iv) /*-{
    var vector = iv;
    var decrypt_promise = null;
    var decrypted_data = null;
    decrypt_promise = window.crypto.subtle.decrypt({name: "AES-GCM", iv: vector}, key_object, encrypted_data);

    decrypt_promise.then(
        function(result){
            decrypted_data = new Uint8Array(result);
            jsCrypt.@com.starcom.dater.client.JsCrypt::onDecrypt(Ljava/lang/String;)(new TextDecoder("utf-8").decode(decrypted_data));
        },
        function(e) {
            alert("Error while decrypting data: " + e.message);
        }
    );
  }-*/;
  
}
