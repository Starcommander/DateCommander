# DateCommander
Create a survey for your Contacts similar to doodle

![Selection](/readme/Selection.png "Selection view")
![Table](/readme/Table.png "Table view")

This app is ...
* Built with the google web toolkit
* Very simple
* Self hosted
* Server and client side written in java

Build
* You can Build it with: ant build
* First you have to get GWT and libs:
* See lib/Info.txt and gwt-sdk/Info.txt

Test
* You can test the app locally with ant devmode

Installer
* There are 2 installers that you can execute:
* Debian - Creates a *.deb for installing on a linux-debian machine.
* Docker - Creates a docker image, for running on any linux machine.

Current status:
* You can create/enter/modify a survey very easy.
* You can use markdown in description, so the use of * or 1. or http:// is converted.
* You can see it live on a hosted page: [DaterWebApp](http://kxn4rm.myvserver.online:8080/DaterWebApp/DaterWebApp.html)

Next steps:
* Create installer for docker
* Add Image-upload possibility for main and choices
* Allow multible entries from same browser?
