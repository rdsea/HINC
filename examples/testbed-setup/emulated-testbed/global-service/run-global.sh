### BEGIN INIT INFO
# Provides:          hinc-global-service
# Required-Start:
# Required-Stop:
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start HINC Global Service daemon
# Description:       To start-stop hinc-global
### END INIT INFO

RUN_MODE="daemons"

# Checking for java
JAVA=/opt/jre1.7.0/bin/java
if [ -x "$JAVA" ]; then
		echo "Default java found !"
elif which java > /dev/null; then
    JAVA=`which java`
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    JAVA=$JAVA_HOME/bin:$PATH
else
    echo "Java is not found, please check !"
   	exit 1
fi

NAME=hinc-global-service

DAEMONDIR=./
DAEMON=global-management-service-1.0-war-exec.jar
WORKING_DIR=/tmp/hinc-global-daemon
PIDFILE=$WORKING_DIR/$NAME.pid
LOG_DIR=$WORKING_DIR/logs
HTTP_PORT=8080

test -x $JAVA -a -f $DAEMONDIR/$DAEMON || echo "test -x $JAVA -a -f $DAEMONDIR/$DAEMON failed"  
test -x $JAVA -a -f $DAEMONDIR/$DAEMON || exit 0

. /lib/lsb/init-functions
mkdir -p "$WORKING_DIR" 

case "$1" in
	start)
		log_daemon_msg "Starting $NAME daemon"
		log_progress_msg "$NAME"
		# Make sure we have our PIDDIR, even if it's on a tmpfs
		# install -o root -g root -m 755 -d $PIDDIR
        if ! start-stop-daemon --start --chdir $DAEMONDIR --quiet --pidfile $PIDFILE --make-pidfile --background --exec $JAVA -- -DLOG_DIR=$LOG_DIR -jar $DAEMON -httpPort $HTTP_PORT ; then
		    log_end_msg 1
		    exit 1
		fi
		log_end_msg 0
		;;
	stop)
		log_daemon_msg "Stopping $NAME daemon"
		log_progress_msg "$NAME"

		start-stop-daemon --stop --quiet --pidfile $PIDFILE
		# Wait a little and remove stale PID file
		sleep 1
		if [ -f $PIDFILE ] && ! ps h `cat $PIDFILE` > /dev/null
		then
			# Stale PID file (Service was succesfully stopped),
			# remove it
			rm -f $PIDFILE
		fi
		log_end_msg 0
		;;
	restart)
		$0 stop
		sleep 1
		$0 start
		;;
	status)
		pidofproc -p $PIDFILE $JAVA >/dev/null
		status=$?
		if [ $status -eq 0 ]; then
			log_success_msg "$NAME is running"
		else
			log_failure_msg "$NAME is not running"
		fi
		exit $status
		;;
	*)
		echo "Usage: $0 {start|stop|restart|status}"
		exit 1
		;;
esac
 
exit 0
