# Change directory
cd ~/VirtualBox\ VMs/

# Set variables
VM_NAME="ubuntu16"
VM_HD_PATH="ubuntu16.vdi" # The path to VM hard disk (to be created).
HD_SIZE=5000
RAM_SIZE=4096
VRAM_SIZE=128
VM_ISO_PATH=~/Documents/dev/vm/iso/ubuntu-16.04.3-server-amd64.iso # Change path as needed
# SHARED_PATH=~ # Share home directory with the VM

# Create and modify VM spec
vboxmanage createvm --name $VM_NAME --ostype Ubuntu_64 --register
vboxmanage createhd --filename $VM_NAME.vdi --size $HD_SIZE
vboxmanage storagectl $VM_NAME --name "SATA Controller" --add sata --controller IntelAHCI
vboxmanage storageattach $VM_NAME --storagectl "SATA Controller" --port 0 --device 0 --type hdd --medium $VM_HD_PATH
vboxmanage storagectl $VM_NAME --name "IDE Controller" --add ide
vboxmanage storageattach $VM_NAME --storagectl "IDE Controller" --port 0 --device 0 --type dvddrive --medium $VM_ISO_PATH
vboxmanage modifyvm $VM_NAME --ioapic on
vboxmanage modifyvm $VM_NAME --memory $RAM_SIZE --vram $VRAM_SIZE
vboxmanage modifyvm $VM_NAME --nic1 nat
vboxmanage modifyvm $VM_NAME --natpf1 "guestssh,tcp,,2222,,22"
vboxmanage modifyvm $VM_NAME --natdnshostresolver1 on
# vboxmanage sharedfolder add $VM_NAME --name shared --hostpath $SHARED_PATH --automount

# After initial setup and installation of openssh-server via GUI, start vm with command
vboxmanage startvm $VM_NAME --type headless
# Connect to VM via ssh
ssh -p 2222 capstone@localhost
