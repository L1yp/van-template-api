TODAY=`date +'%Y-%m-%d'`

SRVNAME=van-template-api
BASEDIR=/data/SRVNAME
JARNAME=$SRVNAME.jar
JAVAEXE=$JAVA_HOME/bin/java

JARFILE=$BASEDIR/$JARNAME
if [ ! -f $JARFILE ]; then
  echo "ERROR: server jar file not found!";
  exit 1;
fi

ulimit -n 65535

cd $BASEDIR

$JAVAEXE -server --enable-preview -Dspring.profiles.active=prod -XX:+ShowCodeDetailsInExceptionMessages -XX:MetaspaceSize=128m -Xms512M -jar $JARFILE  > $BASEDIR/logs/runtime.${TODAY}.log &


exit 0