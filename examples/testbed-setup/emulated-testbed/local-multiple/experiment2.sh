bash kill-all-local.sh
pkill -f "DummyProvider-1.0.jar"
sleep 5
nohup java -jar DummyProvider-1.0.jar 8888 800 &
sleep 5

rm -rf localrun/*

bash run-local.sh 01 02 03 04 05 06 07 08 09 10
bash run-local.sh 11 12 13 14 15 16 17 18 19 20
bash run-local.sh 21 22 23 24 25 26 27 28 29 30
bash run-local.sh 31 32 33 34 35 36 37 38 39 40
bash run-local.sh 41 42 43 44 45 46 47 48 49 50
bash run-local.sh 51 52 53 54 55 56 57 58 59 60
bash run-local.sh 61 62 63 64 65 66 67 68 69 70
bash run-local.sh 71 72 73 74 75 76 77 78 79 80
bash run-local.sh 81 82 83 84 85 86 87 88 89 90
bash run-local.sh 91 92 93 94 95 96 97 98 
