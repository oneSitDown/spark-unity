package com.rayneo.sparklib;

public interface ISparkLLMProxy {

    void onLLMResult(int status, String Content);

    void onLLMEvent(int eventID, String eventMsg);

    void onLLMError(int errorCode, String var2);

}
