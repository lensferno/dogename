# 一个用来点名的玩意/Some thing to choose name

看下面


## 介绍/Introduce
这是一个以Java语言编写，采用Google Material Design（Google MD）为界面风格的用来点名的东西。
更新什么的基本上是不打算的了ヾ§ ￣▽)ゞ

#### 用法介绍
##### 0 . 安装与配置

+ 如果您使用Windows系统
您从release下载了提供Windows的程序包后，您需要通过运行压缩包中的“configure”文件来完成安装与配置。如果一切顺利，Dogename会立即开始运行，并在桌面上创建一个狗头的图标。
	
如果您是玩弄bat文件的高手，还可以修改“configure”文件来根据您的喜好安装。
	
如果您更是玩弄Java的好手，也可以直接运行“dogename.jar”。

+ 如果您使用linux系统
从release下载digename.jar后........
既然都用linux系统了，那么运行一个jar文件并不困难吧。

+ 如果您用Mac系统.......
嗯.....
	
##### 1 . 开始点名
+ 
     很简单，你只需要点击“安排一下”就开始了。您可以根据需要调整不同的功能。挑选中的名字或数字会被箭头指着。不过在开始前，你需要添加几个名字或设定挑选数字的范围。下面就是介绍如何添加名字或设定挑选的数字范围（如果你想使用“数字挑选法”）。
----
##### 2 .添加名字

点击“名字挑选法”后，点击“名单管理”（见下面的截图），即可出现名单管理的界面。

+ 一个一个添加........
    + 输入一个名字后，点击“添加”就可以辣～
+ 批量添加
    + 您可以一个一个输入名字，每输入一个名字后再“回车”换一行，像这样：

![像这样](https://github.com/eatenid/dogename/raw/master/exmaple.png)

   然后再点击“添加”即可批量添加
   
   + 有时候一个一个输入有点不大现实，这是您可以从包含有整列名单的Word或Excel表格中复制含有名字的那一整列，将其粘贴到名字输入框中，然后再点击“添加”，注意，粘贴上去的效果要像上面那样，要不然会有其他的东西出来。

----

##### 3.更改挑选数字范围
点击“数字挑选法”后（见下面的截图），在原来“名单管理”按钮的地方就会出现输入数字范围的小玩意，这时只要输入最大最小值就好了。

----

##### 4.关于“娱乐模式”和“机会均等”
勾选“机会均等”后，将会保存已点过的的名字和数字到文件中，下次启动时仍不会被点到，直到全部名字或数字被点完 或点击“机会均等”的“重置”按钮。注意：仅保存“这次点过就不点了”模式下选中的名字或数字。
勾选“娱乐模式”后，会使被点过的名字在挑选列表中多出现4-5次，增加了再次被点中的几率。注意：仅在勾选此模式后点中的名字才会被多增加4-5次，不勾选时选中的名字不受影响。退出后会自动重置，不影响下次使用。

## 截图/Screenshot:

>此为在Deepin Linux系统的截图效果，其他系统运行的效果可能会稍有差别

**主界面：**

![每日古诗词](https://github.com/eatenid/dogename/raw/master/main_gushici.png)

![主界面](https://github.com/eatenid/dogename/raw/master/screenshot_main.png)

**名单界面*

![名单界面](https://github.com/eatenid/dogename/raw/master/screenshot_namepane.png)

**作者信息界面*

![作者信息界面](https://github.com/eatenid/dogename/raw/master/screenshot_info.png)


## 使用到的第三方库/Third-party library used：


[JFoenix](https://github.com/jfoenixadmin/JFoenix)(8.0.4)


[Apache Commons Codec](http://commons.apache.org/proper/commons-codec/)(1.11)


[Gson](https://github.com/google/gson)(2.8.5)


