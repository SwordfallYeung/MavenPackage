@echo off
@echo "开启启动jetty..."
cd %~dp0
cd ..
start /b java -jar arrivalInspect-1.0-SNAPSHOT.jar  #后台持续运行
@echo "jetty启动成功!"
exit
