#!/bin/bash
export $(cat goldengit.env | xargs)
sudo -E java -jar goldengit-0.0.1-SNAPSHOT.jar > application.log &
