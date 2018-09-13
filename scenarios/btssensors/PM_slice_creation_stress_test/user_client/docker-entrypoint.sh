#!/bin/bash

/auth.sh
for i in {1..25}; do node user_client.js; done