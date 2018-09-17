#!/bin/bash

cd /home/ubuntu/upload/packing/photo/processed

find ./ -ctime +90 -delete 


cd /home/ubuntu/upload/order/excel

find ./ -ctime +10 -delete 





