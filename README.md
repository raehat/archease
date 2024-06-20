## THIS PROJECT IS SUBMISSION FOR ARCHWAY HUNT-A-THON 

Project consists of two modules
- archeasecontracts
- archeaseapp

# archeasecontracts
This module includes the contract code deployed on archway constantine chain. Mobile App interacts with this smart contract
to execute functionalities
Contract address: archway1aw33ujp5nevaz4dm4vslrlvnnl78ssq85nultv2s5lax2kyp3cjqku076t

## To deploy contracts locally, follow these steps

1) Open command line
2) Run the command to clone repository
   ```https://github.com/raehat/archease```
3) move into archeasecontracts directory
   ```cd archeasecontracts```
4) Run the command to install archway-cli
   ```npm i -g @archwayhq/cli```
5) Create a new wallet
   ```archway accounts new```
6) Send some test tokens to newly created account
7) Build contracts
   ```archway contracts build project```
8) Deploy contracts
   ```archway contracts store project```
9) Instantiate contracts
    ```archway contracts instantiate project```

# archeaseapp 
This module includes the mobile app code. 

## To build mobile app locally, follow these steps

1) Open command line
2) Run the command to clone repository in Android Studio
   ```https://github.com/raehat/archease```
3) move into archeaseapp directory
   ```cd archeasecontracts```
4) Run the command to build app
   ```./gradlew assembleDebug```

## Project Architecture
Explains how these modules interact with each other

![image](https://github.com/raehat/archease/assets/77321971/9f2e48c0-9ed7-403c-af3e-b55e3267584f)

