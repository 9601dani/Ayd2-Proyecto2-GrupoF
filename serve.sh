#!/bin/bash

set -e

echo "Moving Frontend to NGInx"
sudo rm -rf /var/www/html/*
sudo cp -r /home/jenkins/deploy/frontend/app-frontend/browser/* /var/www/html/
sudo chown -R www-data:www-data /var/www/html
sudo chmod -R 755 /var/www/html
sudo systemctl restart nginx
echo "Frontend deployed"

echo "Killing Backend processes"
sudo pkill -f 'java -jar' || true

echo "Init Gateway"
nohup java -jar /home/jenkins/deploy/backend/gateway.jar > /home/jenkins/logs/gateway.log 2>&1 &

echo "Init microservices"
for jar in /home/jenkins/deploy/backend/*.jar; do
    if [[ "$jar" != *"gateway.jar" ]]; then
        SERVICE_NAME=$(basename "$jar" .jar)
        echo "🔹 Init $SERVICE_NAME"
        nohup java -jar "$jar" > "/home/jenkins/logs/$SERVICE_NAME.log" 2>&1 &
    fi
done