# Set variables
VM_NAME="ubuntu16"
ARG1='The quick brown fox jumps over the lazy dog.'

vboxmanage startvm $VM_NAME --type headless
sleep 5
ssh -p 2222 capstone@localhost "cd ~/capstone17-2/CalculateSHA1/src; java com.capstone.Main $ARG1;"
ssh -p 2222 capstone@localhost "cat ~/capstone17-2/CalculateSHA1/src/CalculateSHA1.txt" > result.txt
