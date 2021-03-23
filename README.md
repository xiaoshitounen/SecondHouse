# SecondHouse

### 1.视觉效果
![](https://android-project-1300729795.cos.ap-guangzhou.myqcloud.com/secondhouseandroid/second_house_first.jpg)
![](https://android-project-1300729795.cos.ap-guangzhou.myqcloud.com/secondhouseandroid/second_house_second.jpg)
![](https://android-project-1300729795.cos.ap-guangzhou.myqcloud.com/secondhouseandroid/second_house_third.jpg)
![](https://android-project-1300729795.cos.ap-guangzhou.myqcloud.com/secondhouseandroid/second_house_fourth.jpg)
![](https://android-project-1300729795.cos.ap-guangzhou.myqcloud.com/secondhouseandroid/second_house_fiveth.jpg)

### 2.使用到的第三方库
1> 完成沉浸式状态栏：[ImmersionBar](https://github.com/gyf-dev/ImmersionBar)

2> 自定义标题栏：[android-titlebar](https://github.com/wuhenzhizao/android-titlebar)

3> 网络请求：[okhttp](https://github.com/square/okhttp)

4> Json数据解析：[gson](https://github.com/google/gson)

5> 图片加载：[glide](https://github.com/bumptech/glide)

6> 轮播图：[banner](https://github.com/youth5201314/banner)

7> 智能刷新：[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout) --- 智能刷新库API介绍: [Android智能刷新框架SmartRefreshLayout](https://www.jianshu.com/p/29e315ff44a6)

8> 图片放大浏览：[BigImageViewPager](https://github.com/SherlockGougou/BigImageViewPager)

9> 相机相册选择图片：[EasyPhotos](https://github.com/HuanTanSheng/EasyPhotos)

10> 本地数据库操作：[room](https://developer.android.com/jetpack/androidx/releases/room)

11> 腾讯云对象存储存储头像：[cos-xml](https://cloud.tencent.com/document/product/436/12159#1.-.E5.AE.9E.E7.8E.B0.E8.8E.B7.E5.8F.96.E4.B8.B4.E6.97.B6.E5.AF.86.E9.92.A5)


### 3.房源信息爬取
如何爬取链家房源信息参考我的另一个项目：https://github.com/xiaoshitounen/SecondHouseSpider

### 4.后端API介绍

后端使用的是云服务器CentOS，采用Apache + PHP的方案

① 获取指定城市区县信息，目前只支持cq(重庆)和gz(广州)
```
http://182.254.228.71/secondhouse/config.php?city=cq
http://182.254.228.71/secondhouse/config.php?city=gz
```

② 获取指定城市 指定区县 指定偏移的 10条房子信息
```
http://182.254.228.71/secondhouse/message.php?city=cq&area=yubei&offset=3
http://182.254.228.71/secondhouse/message.php?city=gz&area=haizhu&offset=3
```

③ 获取指定城市 指定房子的基本属性
```
http://182.254.228.71/secondhouse/base.php?city=cq&house=1
```

④ 获取指定城市 指定房子的交易属性
```
http://182.254.228.71/secondhouse/business.php?city=cq&house=1
```

⑤ 获取指定城市 指定房子的特色信息
```
http://182.254.228.71/secondhouse/special.php?city=cq&house=1
```

⑥ 获取指定城市 指定房子的图片信息
```
http://182.254.228.71/secondhouse/picture.php?city=cq&house=1
```

⑦ 获取指定城市 指定id的房子信息
```
http://182.254.228.71/secondhouse/house.php?city=gz&house=1
```

### 5.整体架构
![](https://android-project-1300729795.cos.ap-guangzhou.myqcloud.com/secondhouseandroid/second_house_framework.png)
