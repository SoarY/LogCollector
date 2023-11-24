# LogCollector
Android Log 日志收集库

## Demo 
[Download](https://github.com/SoarY/LogCollector/blob/master/file/LogCollectorExample-debug.apk?raw=true)

## 使用步骤
#### Step 1.依赖LogCollector
Gradle 
```groovy
dependencies{
    implementation 'com.soarsy:logCollector:1.0.0'
}
```

#### Step 2.Application中初始化
```java
private void init() {
     context = getApplicationContext();
     //崩溃日志初始化（默认开启）
     CrashHelper.getInstance().init(context);

        //建议在需调试时打开
      ////普通log日志初始化
      // LogcatHelper.getInstance().init(context);
      ////开启普通日志输出
      //LogcatHelper.getInstance().start();
}
```
## 效果
#### 有SD卡<br>
  * getExternalFilesDir<br>
  
      /sdcard/Android/data/(application package)/files/Logcat/logcat.log<br>
      
#### 无SD卡<br>
  * getFilesDir<br>
  
      /data/data/(application package)/files/Logcat/logcat.log<br>
#### 效果示例
  ![效果示例](https://github.com/SoarY/LogCollector/blob/master/file/logpath.png?raw=true)
