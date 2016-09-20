docker ps -a | awk '{ print $1,$2 }' | grep demo:actuator | awk '{print $1 }' | xargs -I {} docker rm -f {}
docker rmi demo:actuator
