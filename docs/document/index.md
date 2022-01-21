## :material-tag: 发布版本

![image-20220121152048112](images/index/image-20220121152048112.png)

1. `UltimateShell-x.x.x-jar-with-dependencies.jar` ：打包了完整的依赖包（平台通用）
2. `UltimateShell-x.x.x.jar` ：没有打包任何依赖包
3. `ultimateshell_setup.exe`：Windows安装程序（Windows专用）

## :fontawesome-brands-windows: Windows 安装

:one: Windows `ultimateshell_setup.exe` 安装包

不会吧，不会吧，不会还有人不懂双击Windows安装程序安装软件吧。:zany_face:

:two: 通用包 `UltimateShell-x.x.x-jar-with-dependencies.jar`  

- 已安装JDK，并配置了系统环境变量（不懂的建议百度），可 `双击执行`

- 或者命令执行 `java -jar UltimateShell-x.x.x-jar-with-dependencies.jar ` 
- 没有配置环境变量的可以使用 `java` 程序的绝对路径，`D:\jdk-11\bin\java.exe -jar java -jar UltimateShell-x.x.x-jar-with-dependencies.jar`

:three: 最小体积 `UltimateShell-x.x.x.jar` 

同上，只不过没有将pom.xml中依赖的jar包打包进去，所以需要自己去源码中找jar包放到类加载路径中，好自为之。:stuck_out_tongue_winking_eye:



## :fontawesome-brands-linux: Linux 安装

建议使用平台通用包  `java -jar UltimateShell-x.x.x-jar-with-dependencies.jar ` 





## :fontawesome-brands-apple: MacOS 安装

建议使用平台通用包  `java -jar UltimateShell-x.x.x-jar-with-dependencies.jar ` 







