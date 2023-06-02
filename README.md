发布线上时需要修改的地方
1.project / build.gradle 放开注释
   //classpath 'com.google.gms:google-services:4.3.0'
   //classpath 'com.google.firebase:firebase-crashlytics-gradle:2.8.1'
2.settings.gradle 放开注释 //include ':analysis'
3.app/build.gradle
   1).插件注释，在顶部 plugins 中
        // id 'com.google.gms.google-services'
        // id 'com.google.firebase.crashlytics'
   2).放开 firebase 相关注释，在 dependencies 中
4.放开LoanApplication 中firebase相关注释
5.GPInfoUtils中放开注释
6.添加google-service.json文件