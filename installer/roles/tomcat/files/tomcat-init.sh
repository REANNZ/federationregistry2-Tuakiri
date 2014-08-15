
timeout='5 minutes'
ports=$(xmlstarlet sel -t -v '//Connector/@port | //Server/@port | //Listener/@rmiRegistryPortPlatform | //Listener/@rmiServerPortPlatform' $CATALINA_HOME/conf/server.xml)

# Checks for open Tomcat ports. Returns true if any are open.
is_listening() {
  # Start with one that will never match, so we don't have to track pipes.
  pattern=':\(Z'
  for port in $ports; do
    hex_port=$(printf '%04X' $port)
    pattern="$pattern\\|$hex_port"
  done
  pattern="$pattern\\)\\>"

  # Machine readable list of active TCP ports. Fields are:
  # sl  local_address  rem_address   st
  # We want the ones with st = '0A' which means 'listening'
  cat /proc/net/tcp /proc/net/tcp6 | awk '$4 == "0A" {print $2}' | grep -q "$pattern"
}

await_stop() {
  if is_listening; then
    expire_at=$(date -d "$timeout" +"%s")
    echo -n 'Ports in use, awaiting service stop...' >&2
    while is_listening; do
      sleep 0.1
      if [ $(date +"%s") -gt $expire_at ]; then
        echo ' Timed out.' >&2
        exit 1
      fi
    done

    echo ' Ports are available.' >&2
  fi
}

umask 0007

case $1 in
start)
  await_stop
  su $user -c '/bin/sh $CATALINA_HOME/bin/startup.sh'
;;
stop)
  su $user -c '/bin/sh $CATALINA_HOME/bin/shutdown.sh'
;;
restart)
  su $user -c '/bin/sh $CATALINA_HOME/bin/shutdown.sh'
  await_stop
  su $user -c '/bin/sh $CATALINA_HOME/bin/startup.sh'
;;
esac
exit 0
