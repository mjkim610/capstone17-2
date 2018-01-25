# Cloudy: Latency Reduction between Mobile Users and Edge Servers

## Overview
Cloudy is a proof-of-concept workload offloading system. Cloudy creates random workloads to be performed by an Android device, and automatically offloads the workloads to available servers.
Cloudy consists of:
- [Simulations](https://github.com/mjkim610/capstone17-2/tree/master/simulations) that run on the Android platform
- [Module](https://github.com/mjkim610/capstone17-2/tree/master/MultithreadingAndroid) that offloads the simulations to available servers
- cloud servers

## Inspiration and Goal
Mobile phones have relatively low processing power and limited battery life. So there are situations where it is desirable to offload resource-intensive workloads from the mobile device to one or more cloud servers and simple retrieve the result from the mobile device.

Our goal was to:
- Implement techniques to reduce workload offloading latency between mobile devices and cloud servers
- Define the appropriate situations for different workload offloading techniques.

## Techniques
Cloudy uses 3 different techniques:
- Using operating-system-level virtualization (Docker) rather than full virtualization (VMWare, VirtualBox)
- Using a custom scheduling algorithm to reduce time overhead by minimizing the types of simulations each cloud server handles
- Take into account the hop distance between the mobile device and the cloud server when calculating estimated completion time

## Demo
![Cloudy Demo](/resources/demo.gif)

## License
See the [LICENSE](LICENSE.md) file for license rights and limitations (MIT).
