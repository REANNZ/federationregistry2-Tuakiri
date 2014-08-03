# description: FR Start Stop Restart
# processname: tomcat
# chkconfig: 3 20 80

export JAVA_HOME=/usr/lib/jvm/jre-openjdk
export CATALINA_HOME=/opt/federationregistry/tomcat/current
export CATALINA_OUT=$CATALINA_HOME/temp/catalina.out
export JAVA_OPTS=""

CATALINA_OPTS="-server -Xms768m -Xmx1280m -XX:MaxPermSize=512m"
CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote"
CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote.ssl=false"
CATALINA_OPTS="$CATALINA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
export CATALINA_OPTS

user=federationregistry

. $CATALINA_HOME/bin/tomcat-init.sh
