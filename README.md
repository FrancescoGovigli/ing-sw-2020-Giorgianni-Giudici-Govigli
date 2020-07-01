Santorini42 - Final Test of Software Engineering - a.a. 2019-2020
======================================================================
![alt text](https://2.bp.blogspot.com/-YHuiYPBEHKs/WVKpXTzu5KI/AAAAAAAAYCs/pTVyng97P3EDoLq9PMqVv18ECzBD4K2CwCLcBGAs/s1600/copertina_santorini_2016.jpg)

The project required to implement the board game [*Santorini*](https://tabletopia.com/games/santorini).

It was developed by:
- [**Daniele Giorgianni**](https://github.com/DanieleGiorgianni)
- [**Luca Giudici**](https://github.com/LucaGiudiciPoliMi)
- [**Francesco Govigli**](https://github.com/FrancescoGovigli)

Useful links
----------
### UML
The UML diagrams are available from the folder: [*UML*](https://github.com/FrancescoGovigli/ing-sw-2020-Giorgianni-Giudici-Govigli/tree/master/deliveries/final/uml).

### JavaDoc
JavaDoc can be found in the folder: [*Javadoc*](https://github.com/FrancescoGovigli/ing-sw-2020-Giorgianni-Giudici-Govigli/tree/master/deliveries/final/javadoc).

### JAR
JAR files can be found in the folder: [*JAR*](https://github.com/FrancescoGovigli/ing-sw-2020-Giorgianni-Giudici-Govigli/tree/master/deliveries/final/jar).

Features developed
-----------------------
The developed project implements the following features by doing
reference to the requirements specified by the teachers:
- Complete rules
- Command Line Interface
- Graphic User Interface
- Socket
- 1 Advanced Functionality (Undo)

Instructions on how to perform JARs
---------------------------------
The game can be run both locally and via the network. In the first case just follow the instructions **(1)**, in the second case the instructions **(2)**.

The game can be launched in two modes, via command line or with a graphical interface. The instructions are shown below.

### Server
- **(1)** Once you download the server JAR (see link above), just open the terminal from Windows or MacOs, go to the folder where the JAR was downloaded and type the following statement:

        java -jar serverApp.jar

    In this circumstance the address of the Server will be that of the Local Host.

- **(2)** Once you download the server JAR (see link above), simply open the terminal of the device connected to the network, go to the folder where the JAR was downloaded and type the following statement:

        java -jar serverApp.jar

    In this circumstance the address of the Server will be that of whom will run the server App.jar.

Note: 
- the Server does not have a graphical interface
- The server could be also launched by clicking twice on the corresponding .jar file. It will run in the background.

### CLI
- **(1)** Once the client's JAR has been downloaded (see link above), just open the terminal from Windows or MacOs, go to the folder where the JAR was downloaded and type the following statement:

        java -jar clientApp.jar -cli

    When asked to enter the IP address, it will be
    just press *"Enter"* to automatically use the address
    of the Local Host.

- **(2)** Once the client's JAR has been downloaded (see link above), just open the terminal from Windows or MacOs, go to the folder where the JAR was downloaded and type the following statement:

        java -jar clientApp.jar -cli

    When asked to enter the IP address, it will be it is necessary to insert the IP address of the Server and then press *"Enter"*.

### GUI
Prerequisites: Java SE must be installed on the computer
Oracle Development Kit 8 (available at the link [*JDKDownload*](https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html))

- **(1)** Once the client's JAR has been downloaded (see link above), just double-click on the clientApp.jar file, both on Windows than on MacOs. When insertion is requested of the IP address, simply press the button *"Connect & Play"*, leaving the text box empty.

- **(2)** Once the client's JAR has been downloaded (see link above), just double-click on the clientApp.jar file, both on Windows than on MacOs. When insertion is requested of the IP address, you will need to enter it in the text box the IP address of the Server and then press the button *"Connect & Play"*.

### Tips for Distributed Architecture
If you want to use a Personal Computer as a Server it is recommend to:
- Download the free application [*LogMeInHamachi*](https://www.vpn.net/)
- Create an account (following the directions provided by the application)
- Create a room ("Network" -> "Create a new network ...")
- Access the participants ("Network" -> "Join an existing network ...")
- Once inside the room, one of the participants performs the step **(2)** for the Server.
- Who wants to participate as a client will perform the point **(2)** for CLI or GUI, depending on the interface