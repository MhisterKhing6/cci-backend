#!/bin/bash

docker run -d --name cc-backend -p 9090:9090 \
-e MONGO_URL="mongodb+srv://kingsleybotchwayedu11:UZZIAHPOP%4090@cluster0.qth5ban.mongodb.net/conference?appName=Cluster0" \
-e MNOTIFY_API="yRqE0kiFfWB2oCejHyY9mt9Yz" \
-e JWT_ACCESS_KEY="mySecretAccessKey123456789abcdefghijklmnopqrstuvwxyz" \
-e JWT_REFRESH_KEY="mySecretRefreshKey987654321zyxwvutsrqponmlkjihgfedcba" \
-e JWT_EXPIRATION_TIME="286400000" \
-e FRONTEND_HOST="http://localhost:8080" \
-e PAYSTACK_CALLBACK_URL="http://localhost:8080/api/payments/paystack/callback" \
-e PAYSTACK_SECRET_KEY="sk_test_a296eddd34f4fa13f038c56d413c568cd6aeac02" \
cc-backend