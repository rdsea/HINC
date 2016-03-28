apt-get update

wget http://mirror.klaus-uwe.me/apache/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz
tar -xzf apache-maven-3.3.9-bin.tar.gz
cd apache-maven-3.3.9

echo 'export JAVA_HOME=/opt/jre1.8.0_60' >> /etc/profile
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> /etc/profile

echo 'export M2_HOME=/root/apache-maven-3.3.9' >> /etc/profile
echo 'export M2=$M2_HOME/bin' >> /etc/profile
echo 'export PATH=$M2:$PATH' >> /etc/profile
. /etc/profile

# install Virtuosio
sudo apt-get install -y libtool gawk gperf autoconf automake libtool flex bison m4 make openssl libssl-dev
#git clone https://github.com/openlink/virtuoso-opensource.git Virtuoso-Opensource
wget https://github.com/openlink/virtuoso-opensource/archive/develop/7.zip
unzip 7.zip
cd virtuoso-opensource-develop-7/
./autogen.sh
./configure
make
sudo make install
