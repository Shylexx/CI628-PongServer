package com.almasb.fxglgames.pong.net;

// The server receives COMMANDS and sends DATA
// The server processes COMMANDS (from clients) validates them internally, and then sends back only the affected DATA
// Clients input (including contextually) should only send commands, not adjust anything that affects the game world
// Commands are Requests, the server decides if they are valid requests (possible in server context)
// The client does not perform the state logic, it only gives the necessary info for the server to pretend it were the player
// Data must be synchronized across clients. World state must match, but not necessarily all graphical state (UI, etc)
public interface INetCommand {
  public CommandType type = null;

  void serialise();
  void send();
}
