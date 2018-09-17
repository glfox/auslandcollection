#!/bin/bash

frontendProcesses=$(ps -elf | grep serve)
echo "frontendProcesses=${frontendProcesses}"
found_fe="false"
found_be="false"
echo "frontend ..."
if [[ ${frontendProcesses} == *"serve -s build"* ]]; then
  echo "found the nodejs process"
  found_fe="true"
fi

echo "backend ..."
backendProcesses=$(ps -elf | grep java)
if [[ ${backendProcesses} == *"jar auslandweixin.jar"* ]]; then
  echo "found the java process"
  found_be="true"
fi

echo ${found_fe}
echo ${found_be}

if [[ ${found_fe} == "false" ]]; then
  cd /home/ubuntu/ui/
  nohup serve -s build &
fi

if [[ ${found_be} == "false" ]]; then
  cd /home/ubuntu/springboot/prod/
  nohup java -Xmx1024m  -jar auslandweixin.jar &
fi

echo "completed"
