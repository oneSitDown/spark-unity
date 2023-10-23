package com.rayneo.sparklib;

import android.util.Log;

import com.iflytek.sparkchain.core.LLMCallbacks;
import com.iflytek.sparkchain.core.LLMError;
import com.iflytek.sparkchain.core.LLMEvent;
import com.iflytek.sparkchain.core.LLMResult;

public class SparkLLMCallbacks implements LLMCallbacks {

    private static final String TAG = "SparkLLMCallbacks";
    private ISparkLLMProxy unityProxy;

    public SparkLLMCallbacks(ISparkLLMProxy unityProxy) {
        this.unityProxy = unityProxy;
    }

    @Override
    public void onLLMResult(LLMResult llmResult, Object usrContext) {
        Log.d(TAG, "onLLMResult\n");
        String content = llmResult.getContent();
        Log.e(TAG, "onLLMResult:" + content);
        int status = llmResult.getStatus();
        if (unityProxy != null) {
            unityProxy.onLLMResult(status, content);
        }
    }

    @Override
    public void onLLMEvent(LLMEvent event, Object usrContext) {
        Log.d(TAG, "onLLMEvent\n");
        Log.w(TAG, "onLLMEvent:" + " " + event.getEventID() + " " + event.getEventMsg());
        if (unityProxy != null) {
            unityProxy.onLLMEvent(event.getEventID(), event.getEventMsg());
        }
    }

    @Override
    public void onLLMError(LLMError error, Object usrContext) {
        Log.d(TAG, "onLLMError\n");
        Log.e(TAG, "errCode:" + error.getErrCode() + "errDesc:" + error.getErrMsg());
        if (unityProxy != null) {
            unityProxy.onLLMError(error.getErrCode(), error.getErrMsg());
        }
    }
}
