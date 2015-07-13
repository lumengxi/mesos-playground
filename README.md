# mesos-playground

This project demonstrates how to develop Mesos frameworks in Scala.

### Usage
First, you need a Mesos cluster to run this demo project. 

To run it locally, navigate into the `vagrant` directory and start a local single-node Mesos environment 
(go to 10.141.141.10:5050 to make sure Mesos is running correctly).
```
> vagrant up
```  
To run this in an actual Mesos cluster, you will need to change the master URL in the `resources/application.conf`.

Then you could either do `./sbt assembly` to package and run this project as a jar or simply `vagrant ssh` to the Mesos VM,
and do `./sbt run`.

Happy hacking!

### Acknowledgement
- The Mesos Vagrant setting is based on [playa-mesos](https://github.com/mesosphere/playa-mesos)
