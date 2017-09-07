# Make VM up-to-date
sudo apt-get update -y
sudo apt-get upgrade -y
sudo apt-get dist-upgrade -y
sudo apt-get autoremove -y
sudo apt-get autoclean -y

# Install SSH, JDK, and git
sudo apt-get install -y openssh-server
sudo apt-get install default-jdk
sudo apt-get install git

# After VM has been prepared, export it
vboxmanage export ubuntu16 -o ubuntu16.ova
# Now all you need is the .ova file to start the VM
#vboxmanage import ubuntu16.ova
