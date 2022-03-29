<br />
<p align="center">
  <h1 align="center">Asteroids</h1>

  <p align="center">
    Shoot as many Asteroids and Ships as possible without crashing and being shot.
  </p>

## Table of Contents

* [About the Project](#about-the-project)
    * [Built With](#built-with)
* [Getting Started](#getting-started)
    * [Prerequisites](#prerequisites)
    * [Installation](#installation)
* [Design Description](#design-description)
    * [Control Package](#control-package)
      * [Actions](#actions)
      * [Commands](#commands)
    * [Database Package](#database-package)
    * [Game Observer Package](#game-observer-package)
    * [Model Package](#model-package)
      * [Game Objects](#game-objects)
    * [Networking Package](#networking-package)
      * [Client](#client)
      * [Server](#server)
      * [Data Command](#data-command)
    * [Util Package](#util-package)
    * [View Package](#view-package)
      * [Panels](#panels)
      * [View Models](#view-models)
* [Evaluation](#evaluation)
* [Teamwork](#teamwork)
* [Usage](#usage)
  * [Controls](#controls)
  * [Single Mode](#single-mode)
  * [Host Multiplayer](#host-multiplayer)
  * [Join and Spectate Multiplayer](#join-and-spectate-multiplayer)
  * [High Score](#high-score)

## About The Project

The project started as a Single Player mode. We have upgraded the game and added a Multiplayer and a Spectator Game
Mode. Also, we now have a database that stores all the high scores for both of the game modes.
To manage the different players, the game is based on a UDP connection. This multiplayer game mode is intended to
be played locally, all the players being inside the same network.

### Built With

* [Java](https://www.java.com/es/)
* [UDP](https://en.wikipedia.org/wiki/User_Datagram_Protocol)

## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

The latest versions of the following:

* Java

### Installation

1. Navigate to the Asteroids folder
2. Clean and build the project using:

```sh
mvn install
```

3. Run the `Main` method of Asteroids using:

```sh
mvn exec:java
```

4. Alternatively you can run the `main` method in `Asteroids.java` using an IDE of your choice (e.g. IntelliJ)

Should you want to run this program standalone, you can create a JAR file with the following maven command:

```sh
mvn clean package
```

The JAR file will appear in the `/target` directory.

## Design Description

### Control Package

The purpose of this package is to manage the actions performed in the game. It takes care of the actions of the
spaceship, we have implemented a Command Pattern, this receives a KeyEvent and executes an action.

It also contains the Game Controller and Game Updater. Both these classes are meant to manage the game. The Game Updater
is in charge of adding players, spaceships, asteroids, etc... all of these are stored in Arrays of objects. While the
Game Updater is the class that refreshes and calculates all the position every game tick.

#### Actions

Here are the actions that a view can implement. Such as creating a new main menu, new game or quitting the game.

#### Commands

Here you can find all the files dedicated to each command implemented for the Command Pattern. These files take care of
all the possible actions that a Spaceship can perform.

### Database Package

The implementation of the database in order to store all the scores. We have created a simple Database. It administers
the scores storing a number of transactions, each of them containing the user, the game mode and the final score of the
player.

We created a Score class containing all the required fields, in order to easily manage the database and ease the
understanding of the program.

### Game Observer Package

Interface intentionally created so that any class that implements this interface would like to be notified when a game
is updated.

### Model Package

It states all the models that can appear in a Game. It is also in charge of the enumeration for the Asteroid Size and
the Game.java class.

The most important file in this package is the Game class. This class is the main model for the Asteroids game. It
contains all game objects, and has methods to start and stop the game. This class is only a model, and therefore it does
not update, neither change anything. This is done in the Game Updater class in the Control Package. It only contains the
state of the game.

#### Game Objects

Consists of a single class for each object that can appear in a Game. Each game object contains its own functions.

### Networking Package

Is in charge of managing the Client-Server connection and the other way around. It is divided into three sub-packages,
one destined to the Client, one the other to the Server and one for handling packets reception.

The class CommandData represents a command executed by a Spaceship. It is used to send commands as the Data body.

The class DataCommandHandler is used to implement a Command Pattern. It is used by the client, when it receives a packet
it executes the DataCommand indicated in the Data objectType field.

Apart from that we have a class Data that represents what the server and client send to each other, a PacketHandler
class that has to send and receive methods and a record ConnectionDetails that is used to represent a connection.

#### Client

Three files are found here, Client, ClientMain and the ClientUpdater.

The Client class contains the constructor of the client and the main run loop, dedicated to send data packets to the
server, mostly command executed by the client Spaceship. It does so, by using the Data class created. This way, all the
packets that are sent and received are always of the same type, and it's easier to handle and manage the connection.

The ClientMain is the main method of the Client. It contains the method startClient that starts the Client and makes it
work connecting to a requested port.

Finally, the ClientUpdater class is meant to receive Data packets from the server and manage them.

#### Server

Four files are needed to handle the server: ServerMain, ClientHandler, Server and ServerEngine

The Server Main class is the main method of the Server. It's only method is startServer, and it is meant to execute and
start running the server creating a new Server() on a new Thread().

The ClientHandler class manages a single Client connected to the Server. It sends the Data packets through the socket
provided to the client. It sends the game updates, and it is also in charge of disconnecting the player.

The Server Engine Class receives all the Data packets from all the clients. Then the server will calculate the next step
and finally send back to the all the clients the new state of the game.

The Server class is in charge of executing the main loop of the Server. It handles new incoming requests. When a new
client is connected, it will add a new player to the game identified by his thread number and his nickname. A new
clientHandler will be created for each client that connects to the server.

#### Data Command

We can find the DataCommand interface and all the different DataCommands. This way is easy to send and handle new
objects type over the UDP connection.

### Util Package

Contains the PolarCoordinate class. Represents a polar coordinate: an angle, in radians, along with a radius. The values
are normalized so that any polar coordinate has an angle between 0 (inclusive)
and 2 * PI (exclusive), as well as a positive radius.

It also contains the Pair class that is used to paint the score of each spaceship in the game screen.

### View Package

Represents the package dedicated at the view of the program. It has two sub-packages: Panels and View Models. Apart from
that, it also contains the AsteroidFrame and the MainFrame.

The MainFrame class is the frame of the menu. It will contain all the different panels that will be shown depending on
where we are in the menu. It initialises the frame and also adds new panels to the frame.

The AsteroidFrame class is the frame that will display the game.

The ViewController class is the one that manages all the different views available.

#### Panels

Contains all the different panels that can be added to the MainFrame. To manage all the panels we have implemented a
Factory Design Pattern. That way we have a class for each Panel, we just call the ViewController, and it will ensure the
new Panel is displayed properly and efficiently.

The UI is created using the java Swing utility.

#### View Models

Contains all the objects that will be displayed in the asteroid panel, they all extend GameObjectViewModel that has the
drawObject method.

## Evaluation

Starting at the initial part of the program. The Main Menu has all the features required. The panel factory works
flawlessly, and it delivers the panel requested when clicking the buttons.

The single game works as intended since it was already implemented at the beginning of the assignment.

The multiplayer mode is divided into two parts. The Host and the Client. When a new host is created, we have implemented
an intermediate panel where all connected users will wait until the host starts the game. The clients while waiting are
not allowed to start the game because the button Start Game is disabled, but they can exit.

The spectate mode is as implemented as the multiplayer mode, but no spaceship is added for the spectator.

The High Score panel also works perfectly. You can choose the best scores from either the single player or the
multiplayer mode. These scores are stored in a Map during the game and once the game has ended the database is updated.
It is also persistent, so if we close the program and open it again, the scores will remain in the database.

We have played a few multiplayer games, and it has some bugs, if the game ends while the client is receiving an update,
the asteroids get bugged.
Another bug that we found, and can't figure out the solution. If you join a multiplayer game
and exit the waiting room before the game has started and then come back, your name will only appear once in the waiting
room of the host. But in your screen (client) you will see your name twice.

If we had more time we would have implemented more game mode as 1 vs. 1, 1 vs. 5, score all the points you can in 5
minutes or survive all the time you can.


## Teamwork

We both made an initial design, after understanding how the provided code worked. Then we discussed the main structure of the
project, thinking about new panels, how to swap them, how to implement new game modes... Once we had a solid idea we
started programing.

Albert worked mostly on the networking and David on the views and the game updates. All the other features were done
when needed by who needed it. We live together, so each time one found a problem the other stopped to help.
And when it was a design difficulty we both stopped programing and decided how to handle it before continuing.

It is hard to determine exactly who made what. Most of the files were initially created and later modified
depending on what we needed in order to keep the program working.

The teamwork between both of us has been foolproof. The fact that we are living together made the development of
this assignment easier. We were able to spend way more hours than we would have done by ourselves.

## Usage

Once you run the program you see the main menu.

### Controls

W key - Accelerate A key - turn left D key - turn right SPACE key - fire

### Single Mode

To play this mode you just have to click the button.

### Host Multiplayer

If you want to host a game with other computers you need to find out your computer's Wi-Fi interface ip, because clients
will need it.

### Join and Spectate Multiplayer

You need to insert the host ip and the host port, and you will be redirected to the multiplayer waiting room.

### High score

By clicking this button you will see the high scores.

