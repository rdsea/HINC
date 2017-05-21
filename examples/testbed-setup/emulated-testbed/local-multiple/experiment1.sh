QUERY=http://localhost:9898/global-management-service-1.0/rest/sdg/datapoint?timeout=15000

bash run-local.sh 0000pilot
curl $QUERY

bash run-local.sh 01 02 03 04 05 06 07 08 09 10
curl $QUERY
bash run-local.sh 11 12 13 14 15 16 17 18 19 20
curl $QUERY
bash run-local.sh 21 22 23 24 25 26 27 28 29 30
curl $QUERY

QUERY=http://localhost:9898/global-management-service-1.0/rest/sdg/datapoint?timeout=20000

bash run-local.sh 31 32 33 34 35 36 37 38 39 40
curl $QUERY
bash run-local.sh 41 42 43 44 45 46 47 48 49 50
curl $QUERY
bash run-local.sh 51 52 53 54 55 56 57 58 59 60
curl $QUERY

QUERY=http://localhost:9898/global-management-service-1.0/rest/sdg/datapoint?timeout=25000

bash run-local.sh 61 62 63 64 65 66 67 68 69 70
curl $QUERY
bash run-local.sh 71 72 73 74 75 76 77 78 79 80
curl $QUERY
bash run-local.sh 81 82 83 84 85 86 87 88 89 90
curl $QUERY
bash run-local.sh 91 92 93 94 95 96 97 98 99
curl $QUERY
