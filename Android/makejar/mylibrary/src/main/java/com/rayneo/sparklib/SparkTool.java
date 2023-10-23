package com.rayneo.sparklib;

import android.app.Activity;
import android.util.Log;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;
import com.iflytek.sparkchain.core.LLM;
import com.iflytek.sparkchain.core.LLMCallbacks;
import com.iflytek.sparkchain.core.LLMConfig;
import com.iflytek.sparkchain.core.LLMError;
import com.iflytek.sparkchain.core.LLMEvent;
import com.iflytek.sparkchain.core.LLMResult;
import com.iflytek.sparkchain.core.SparkChain;
import com.iflytek.sparkchain.core.SparkChainConfig;

import java.util.IllegalFormatCodePointException;
import java.util.List;

public class SparkTool {

    private static final String TAG = "SparkLLM";
    private Activity _unityActivity;

    private String domain ="general";
    private String url = "wss://spark-api.xf-yun.com/v1.1/chat";

    public int initSDK(String appid,String apiKey,String apiSecret,int logLevel,String domain,String url) {
        // 初始化SDK，Appid等信息在清单中配置
        SparkChainConfig sparkChainConfig = SparkChainConfig.builder();
        sparkChainConfig.appID(appid)
                .apiKey(apiKey)
                .apiSecret(apiSecret)//应用申请的appid三元组
                .logLevel(logLevel);

        int ret = SparkChain.getInst().init(getActivity(),sparkChainConfig);

        this.domain = domain;
        this.url = url;

        return ret;
    }

    public void unInitSDK() {
        SparkChain.getInst().unInit();
    }

    Activity getActivity(){
        if(null == _unityActivity) {
            try {
                Class<?> classtype = Class.forName("com.unity3d.player.UnityPlayer");
                Activity activity = (Activity) classtype.getDeclaredField("currentActivity").get(classtype);
                _unityActivity = activity;
            } catch (ClassNotFoundException e) {

            } catch (IllegalAccessException e) {

            } catch (NoSuchFieldException e) {

            }
        }
        return _unityActivity;
    }

    public int startChat(ISparkLLMProxy unityProxy,String msg) {

        LLMConfig llmConfig = LLMConfig.builder();
        llmConfig.domain(domain)
                .url(url);
        LLM llm = new LLM(llmConfig);
        LLMCallbacks llmCallbacks = new SparkLLMCallbacks(unityProxy);
        llm.registerLLMCallbacks(llmCallbacks);
        String myContext = "myContext";

        int ret = llm.arun(msg,myContext);

        return ret;
    }

}
