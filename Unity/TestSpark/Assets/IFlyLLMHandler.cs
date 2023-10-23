using System;
using UnityEngine;
using UnityEngine.UI;
//using FfalconXR;

public class IFlyLLMHandler : MonoBehaviour
{
    public InputField input;

    public Button button;

    public Text text;

    private AndroidJavaObject sparkToolInstance;
    private void Start()
    {
        Loom.Initialize();
        sparkToolInstance = new AndroidJavaObject("com.rayneo.sparklib.SparkTool");

        InitializeSDK();

        button.onClick.AddListener(() =>
        {
            text.text = "";
            StartChat(input.text);
        });
    }
    public void InitializeSDK()
    {
        if (sparkToolInstance != null)
        {
            int ret = sparkToolInstance.Call<int>("initSDK", "9e803172", "e4045501df3916cad0c4137d43db8b3b", "ZWFiZGYwMjllNTkyYTFmNjE1YTNiMWRk", 0, "general", "");
            Debug.Log("initializeSDK error code is" + ret);
        }
        else
        {
            Debug.LogError("SparkTool instance is null. Make sure the Android plugin is properly imported.");
        }
    }
    public void StartChat(string msg)
    {
        if (sparkToolInstance != null)
        {
            int ret = sparkToolInstance.Call<int>("startChat", new SparkLLMProxy(onLLMResult, onLLMEvent, onLLMError), msg);
            Debug.Log("startChat error code is" + ret);
        }
        else
        {
            Debug.LogError("SparkTool instance is null. Make sure the Android plugin is properly imported.");
        }
    }

    private void onLLMError(int errorCode, string error)
    {
        Debug.Log("onLLMError errorCode " + errorCode + " error message " + error);
    }

    private void onLLMEvent(int eventID, string eventMsg)
    {
        Debug.Log("onLLMError eventID " + eventID + " eventMsg " + eventMsg);
    }

    private void onLLMResult(int status, string content)
    {
        Loom.QueueOnMainThread((p) =>
        {
            text.text += content;
        }, null);

        Debug.Log("onLLMResult status " + status + " content " + content);
    }

    public void UninitializeSDK()
    {
        if (sparkToolInstance != null)
        {
            sparkToolInstance.Call("unInitSDK");
        }
        else
        {
            Debug.LogError("SparkTool instance is null. Make sure the Android plugin is properly imported.");
        }
    }
}
public class SparkLLMProxy : AndroidJavaProxy
{

    private Action<int, string> onLLMResultCallback;
    private Action<int, string> onLLMEventCallback;
    private Action<int, string> onLLMErrorCallback;

    public SparkLLMProxy(Action<int, string> onLLMResult, Action<int, string> onLLMEvent, Action<int, string> onLLMError) : base("com.rayneo.sparklib.ISparkLLMProxy")
    {
        onLLMResultCallback = onLLMResult;
        onLLMEventCallback = onLLMEvent;
        onLLMErrorCallback = onLLMError;
    }

    public void onLLMResult(int status, string content)
    {
        if (onLLMResultCallback != null)
        {
            onLLMResultCallback(status, content);
        }
    }

    public void onLLMEvent(int eventID, string eventMsg)
    {
        if (onLLMEventCallback != null)
        {
            onLLMEventCallback(eventID, eventMsg);
        }
    }

    public void onLLMError(int errorCode, string error)
    {
        if (onLLMErrorCallback != null)
        {
            onLLMErrorCallback(errorCode, error);
        }
    }

}