cd /root

# install g++
sudo apt-get update
sudo apt-get install -y build-essential g++
sudo apt-get install -y python-dev autotools-dev libicu-dev build-essential libbz2-dev pkg-config sudo libglib2.0-dev
sudo apt-get install -y libboost-dev libboost-program-options-dev libexpat1-dev libboost-thread-dev uuid-dev libssl-dev
sudo apt-get install -y scons

# install Boost 1.55
wget http://sourceforge.net/projects/boost/files/boost/1.55.0/boost_1_55_0.tar.gz/download -O boost_1_55_0.tar.gz
tar xzvf boost_1_55_0.tar.gz
cd boost_1_55_0/
./bootstrap.sh --with-libraries=system,filesystem,date_time,thread,regex,log,iostreams,program_options --prefix=/usr/local
sudo ./b2 install
sudo sh -c 'echo '/usr/local/lib' >> /etc/ld.so.conf.d/local.conf'
sudo ldconfig

# Doxigen for the documentation generation
#sudo apt-get install doxygen



wget http://mirrors.kernel.org/iotivity/1.0.1/iotivity-1.0.1.tar.gz
tar -xvzf iotivity-1.0.1.tar.gz
cd iotivity-1.0.1

# install tinycbor
wget https://github.com/01org/tinycbor/archive/master.zip
apt-get install unzip
unzip master.zip
mv tinycbor-master/ extlibs/tinycbor/tinycbor

scons

#make linux

# run sample server
cd /root/iotivity-1.0.1/out/linux/x86_64/release
export LD_LIBRARY_PATH=/root/iotivity-1.0.1/out/linux/x86_64/release:$LD_LIBRARY_PATH
./sampleResourceServer



