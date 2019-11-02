#!/bin/bash
echo "开始启动jetty..."
baseDirForScriptSelf=$(cd "$(dirname "$0")"; pwd)
echo "full path to currently executed script is : ${baseDirForScriptSelf}"
cd ${baseDirForScriptSelf}
cd ..
pwd
nohup java -jar arrivalInspect-1.0-SNAPSHOT.jar & #后台持续运行

echo "jetty启动成功!"
exit