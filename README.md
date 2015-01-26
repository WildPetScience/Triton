# Triton

[![Build Status](https://travis-ci.org/WildPetScience/Triton.svg)](https://travis-ci.org/WildPetScience/Triton)

Triton is the code name for the portion of the project that will be run by the
end-user.

## Sections
There are three sections to this code:
* **Lib**: This is where the majority of the code goes, and is shared across
  all aspects of the project. It contains code that is largely platform
  independent.

* **Client-PC**: This contains code that is run to execute the application on a
  PC.

* **Client-PI**: This contains code that is run to execute the application on a
  Raspberry Pi.

## Setting up the environment
First, download
[IntelliJ IDEA Ultimate edition](https://www.jetbrains.com/idea/download/) -
this is free for students and you should create an account with your @cam email
address.

Next, install [Git](http://git-scm.com/downloads) - on Linux just `sudo apt-get
install git`.

Now, set up
[Git to interact with Github](https://help.github.com/articles/set-up-git/) -
you will be cloning with SSH.

Now, clone the repository. In a terminal, change to a project directory for
WildPetScience, then:

    $ git clone git@github.com:WildPetScience/Triton.git

The repo is now cloned.

Now open IntelliJ and click Import Project. Click Open, and then select the
folder you cloned into.

Finally we must run the code - in the top right of IntelliJ, find the drop-down
box next to the green run button. Select "Triton: Client-PC [run]", then click
the green play button. Gradle will download dependencies and run the
application.
