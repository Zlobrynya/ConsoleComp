#pragma once
#include "stdafx.h"
#include <iostream>
#include <string>

using namespace std;

static const string DEBUG = "deb";
static const string ALLOWED = "allowed";
static const string VOLUME = "volume";
static const string DISCONNECT = "discon";
static const string ID = "id";

class Server
{
public:
	Server(boost::asio::io_service& ioserv);
	~Server();
	void start();
	void closeSocket();

private:
	tcp::socket socket;
	tcp::acceptor acceptor;
	bool bRun;
	bool bSend;
	string sSend;

	void read();
	void write();
	void choiceAction(string choice);

};

