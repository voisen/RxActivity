
### RxActivity 

> 让你的Activity跳转与参数传递更简单、便捷

[![Release](https://jitpack.io/v/voisen/RxActivity.svg)](https://jitpack.io/#voisen/RxActivity) 

类似于 `Retrofit` 网络请求框架

##### 简介
- 简化 `Activity` 跳转过程
- 简化 `Activity` 参数传递 `@ExtraValue`
- 简化 `Activity` 参数传递赋值 `@Autowired`
- 简化 `Activity` 生命周期状态自动保存 `@SaveState`
- 统一化管理 `Activity` ， 让维护更简单

#### 优势
- 注解式指定要跳转的 `Activity`
- 支持隐式跳转等功能
- 全局可拦截`Activity`跳转， 权限控制更方便
- `RxObserve` 加持的`OnActivityResult`， 免去很多过程
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


- Step 2. Add the dependency

````
dependencies {
    ....
    implementation 'com.github.voisen:RxActivity:0.0.1-alpha'
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
        RxActivity.init(this);
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
    @TrickActivity(clazz = AboutActivity.class)
    Observable<Intent> goAbout(@ExtraValue("value") String value);

    /**
     * 隐式跳转到指定的Action, @IntentOptions 优先级 要小于 @IntentValue 优先级
     * @param pkgName 传入包名, 相当与调用 intent.setPackageName(pkgName)
     */
    @IntentOptions(action = "rxactivity_test")
    Observable<Intent> goImplicit(@IntentValue(IntentType.PackageName) String pkgName);

    /**
     * 指定跳转Activity, 且无回传参数
     */
    @TrickActivity(clazz = FullscreenActivity.class)
    void goFullscreen();

    /**
     * 指定跳转Activity, 并指定跳转的`ActivityOptions`
     */
    @TrickActivity(clazz = ImageActivity.class)
    void showImage(ActivityOptions options);
    
}

````

- 执行跳转和接收数据

````java 

 ActivityService service = new RxActivity.Builder()
                .create(ActivityService.class);
 //跳转到  关于界面， 并传入 Hello RxActivity
 service.goAbout('Hello RxActivity').subscribe(new Consumer<Intent>() {
            @Override
            public void accept(Intent intent) throws Throwable {
                //接收回调数据
                
            }
        });;

````
AboutActivity

```` java

public class AboutActivity extends AppCompatActivity {

    //这里用来自动注入参数
    @Autowired //OR @Autowired("value") 指定key值
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

`@Autowired`

> 被标记的变量将自动注入 `Intent` 中传递过来的值， 只要初始化了之后，即使不使用RxActivity中提供的方法跳转， 也会自动注入。`key` 指定 `Intent`中的`key`值。 

`@ExtraValue`

> 标记要传入到`Intent`中的变量， 必须指定`key`值

`@IntentOptions`

> 设置`Intent`中相关属性值，属性的值与类型请正确设置， 该注解用于方法上， 注解优先级低于 `@IntentValue`

`@IntentValue`

> 设置`Intent`中相关属性值，属性的值与类型请正确设置

`@SaveState`

> 标记`Activity`中的成员变量需要保存状态

`@TrickActivity`

> 注解需要跳转的`Activity`类， 支持类名、跳转动画设置。


#### 接口

`RxActivityInterceptor`

> 拦截器（需要在`RxActivity.Builder`中配置），可以在这里处理错误信息， 修改跳转前参数， 跳转后返回参数以及拦截跳转。

`RxParcelable`

> Parcelable 接口默认实现， 原则适用所有类。
