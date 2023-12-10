// EmailCallback.java
package com.example.tests;
public interface EmailCallback {
    void onEmailTaskComplete(boolean success);
    void onEmailSent();
    void onEmailFailed();
}
