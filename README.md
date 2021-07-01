
### RxActivity 

> 让你的`Activity`跳转与参数传递更简单、便捷

[![Release](https://jitpack.io/v/voisen/RxActivity.svg)](https://jitpack.io/#voisen/RxActivity) 

类似于 `Retrofit` 网络请求框架

##### 简介
- 简化 `Activity` 跳转过程
- 简化 `Activity` 参数传递 `@RxExtraValue`
- 简化 `Activity` 参数传递赋值 `@RxAutowired`
- 简化 `Activity` 生命周期状态自动保存 `@RxSaveState`
- 统一化管理 `Activity` ， 让维护更简单

#### 优势
- 注解式指定要跳转的 `Activity`
- 支持隐式跳转等功能
- 全局可拦截`Activity`跳转， 权限控制更方便
- `RxActivityObserve` 加持的`onActivityResult`， 免去很多过程
- 参数传递到目标`Activity`后支持注解式赋值， 无需特殊处理
- 支持注解式自动保存与恢复`Activity`状态信息（例如屏幕旋转值丢失问题）

#### 使用 
> 借用`JitPack`

- Step 1. Add the JitPack repository to your build file

>Add it in your root build.gradle at the end of repositories:

````
allprojects {
	...
	repositories {
		...
		maven { url 'https://jitpack.io' }
		...
	}
	...
}

````


- Step 2. Add the dependency ,
TAG: [![Release](https://jitpack.io/v/voisen/RxActivity.svg)](https://jitpack.io/#voisen/RxActivity) 

````
dependencies {
    ....
	 implementation 'com.github.voisen.rxactivity:rxactivity:TAG'
    annotationProcessor 'com.github.voisen.RxActivity:rxactivityprocessor:TAG'
    ....
}
	
````


## Coding

- 初始化

> 请在Application的onCreate方法中初始化

````java

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RxNavigation.init(this);
    }
}

````

- 创建一个`Interface` 管理 `Activity`

````java

public interface ActivityService {

    /**
     * 指定跳转的Activity为`AboutActivity`
     * @param value 传递的参数
     */
    @RxActivity(path = "app/about")
    RxActivityObserve<Intent> goAbout(@RxExtraValue("value") String value);

    /**
     * 隐式跳转到指定的Action, @IntentOptions 优先级 要小于 @IntentValue 优先级
     * @param pkgName 传入包名, 相当与调用 intent.setPackageName(pkgName)
     */
    @RxIntentOptions(action = "rxactivity_test")
    RxActivityObserve<Intent> goImplicit(@RxIntentValue(RxIntentType.PackageName) String pkgName);

    /**
     * 指定跳转Activity, 且无回传参数
     */
    @RxActivity(clazz = FullscreenActivity.class)
    void goFullscreen();

    /**
     * 指定跳转Activity, 并指定跳转的`ActivityOptions`
     */
    @RxActivity(clazz = ImageActivity.class)
    void showImage(ActivityOptions options);

    /**
     * 指定跳转Activity, 该Activity使用RxPath注解， 并指定值
     */
    @RxActivity(path = "app2lib/login")
    void goLogin();
    
    /**
     * 获取指定路径的 fragment 实例
     */
    @RxFragment(path = "app2lib/fragment")
    Fragment messageFragment();

}

````

- 执行跳转和接收数据

````java 

 //跳转到  关于界面， 并传入 Hello RxActivity
RxNavigation.shared()
	.create(ActivityService.class)
	.goAbout('Hello RxActivity')
	.map(data-> data.getStringExtra("date"))
    .then(new RxListener<String>() {
        @Override
        public void onResult(String data) {
				//数据
        }

        @Override
        public void onError(Throwable e) {
				//错误
        }
    });

````
AboutActivity

```` java

public class AboutActivity extends AppCompatActivity {

    //这里用来自动注入参数
    @RxAutowired //OR @RxAutowired("value") 指定key值
    String value;
    
    public void goBack(View view){
        Intent data = new Intent();
        data.putExtra("value", "ImplicitActivity Back");
        setResult(RESULT_OK, data);
        finish();
    }
}

````

## API


#### 注解类

`RxPath`

> 用于标记`Activity`的路径。 


`@RxAutowired`

> 被标记的变量将自动注入 `Intent` 中传递过来的值， 只要初始化了之后，即使不使用RxActivity中提供的方法跳转， 也会自动注入。`key` 指定 `Intent`中的`key`值。 

`@RxExtraValue`

> 标记要传入到`Intent`中的变量， 必须指定`key`值

`@RxIntentOptions`

> 设置`Intent`中相关属性值，属性的值与类型请正确设置， 该注解用于方法上， 注解优先级低于 `@IntentValue`

`@RxIntentValue`

> 设置`Intent`中相关属性值，属性的值与类型请正确设置

`@RxSaveState`

> 标记`Activity`中的成员变量需要保存状态

`@RxActivity`

> 注解需要跳转的`Activity`类， 支持类名、跳转动画设置。

`@RxFragment`

> 获取指定类或路径的`Fragment`实例


#### 接口

`RxActivityInterceptor`

> 拦截器（需要在`RxNavigation`中配置），可以在这里处理错误信息， 修改跳转前参数， 跳转后返回参数以及拦截跳转。

`RxParcelable`

> Parcelable 接口默认实现， 原则适用所有类。


#### 工具类

`RxActivityUtils`

> 用于注入`Activity`跳转传递的`intentValue` 或者 `saveInstanceState` 的保存与恢复
