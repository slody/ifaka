<h1 align="center">
    IFaKa
</h1>

<p align="center">
	<img src='https://img.shields.io/badge/java-8.0+-success?style=for-the-badge'>
    <img src='https://img.shields.io/badge/version-1.0-blue?style=for-the-badge'>
    <img src='https://img.shields.io/badge/license-MIT-critical?style=for-the-badge'>
</p>

<p align="center">一款简单易用，无需配置复杂环境的个人发卡程序</p>

# 简介

> IFaKa是一款由Java编写并且基于SpringBoot的个人自动发卡平台程序，安装简单且无需安装额外的数据库和环境

# 环境要求

> 程序内置了Sqlite数据库，所以仅需确保安装Java8或以上版本的Java环境即可

# 界面截图

![image1](https://i.loli.net/2020/04/23/DecrHdX1yUK7WEP.png)

![image2](https://i.loli.net/2020/04/23/sVxjfhOdbHPWeCE.png)

![image3](https://i.loli.net/2020/04/23/uIEhfc24JKWRp9G.png)

![image4](https://i.loli.net/2020/04/23/VpXWkcqhm2EBKCb.png)

# 使用方法

预览地址:[http://ifaka.lovedev.tk](http://ifaka.lovedev.tk)

```bash
# 普通方式
java -jar ifaka.jar
# 保持后台运行方式
screen java -jar ifaka.jar
# 自定义启动端口
java -jar ifaka.jar --server.port=8888

后台登录地址: http://域名/:
后台账号密码均为admin
```

# 支付接口

> 码支付
>
> 绿点支付
>
> （暂时只支持这两个,如果你有好的支付接口推荐可以在issues中告知）

# 反馈交流

欢迎加入开发者交流群，群聊号码：[823977396](https://shang.qq.com/wpa/qunwpa?idkey=1e341e6092d59f948f76c35e1dd4acb8be2b111a029e8841f2bec28a2cc7b3c1)

## 开源协议

MIT License