### RxActivity

> 让你的Activity跳转与参数传递更简单、便捷

类似于 `Retrofit` 网络请求框架

##### 简介
- 简化 Activity 跳转过程
- 简化 Activity 参数传递
- 简化 Activity 参数传递赋值
- 简化 Activity 生命周期状态自动保存
- 统一化管理 Activity ， 让维护更简单

#### 优势
- 注解式指定要跳转的Activity
- 支持隐式跳转等功能
- 全局可拦截`Activity`跳转， 权限控制更方便
- `RxJava` 加持的`OnActivityResult`， 免去很多过程
- 参数传递到目标`Activity`后支持注解式赋值， 无需特殊处理
- 支持注解式自动保存与恢复`Activity`状态信息（例如屏幕旋转值丢失问题）

#### 使用



