1本地测试运行application.yml文件要改为dev，云部署prod
2pom.xml本地运行要把屏蔽tomcat的注解注释。云部署要打开
3线上：api接口
http://101.42.168.101:8088/foodie-dev-api/doc.html
http://yinchuan.work/foodie-dev-api/doc.html
4本地：api接口（端口号80）
http://localhost/doc.html
5云服务文件位置
后端代码 /usr/local/tomcat-api/webapps
支付中心 /usr/local/tomcat-frontend/webapps
前端静态页面部署在nginx 
nginx的安装目录 /usr/local/nginx
静态页面位置：/home/website/foodie-shop /home/website/foodie-center
6日志间隔时间调整
现在本地测试每分钟打印一条日志。log4j.properties里面 log4j.appender.file.DatePatter
yyyy-MM-dd-HH-mm 精确到分钟改为精确到天 yyyy-MM-dd
7本地前端地址  D:\tomcat\apache-tomcat-8.5.31\bin
