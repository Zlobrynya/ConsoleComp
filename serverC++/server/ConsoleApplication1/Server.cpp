#include "stdafx.h"
#include "Server.h"
#include "CheckID.h"
#include <boost/algorithm/string/split.hpp>
#include <boost/algorithm/string/classification.hpp>

Server::Server(boost::asio::io_service & ioserv) : socket(ioserv)
	, acceptor(ioserv, tcp::endpoint(tcp::v4(), 13))
{
	bRun = true;
	bSend = false;
	controlComp = new ControlComp();
}

Server::~Server()
{
	socket.close();
}

void Server::start()
{
	// wait and listen
	acceptor.accept(socket);
//	int i = 0;
	while (bRun) {
		read();
		if (bSend)
			write();
	}
}

void Server::closeSocket()
{
}

void Server::read()
{
	boost::array<char, 128> buf;
	boost::system::error_code error;
	size_t len = socket.read_some(boost::asio::buffer(buf), error);
	// When the server closes the connection, 
	// the ip::tcp::socket::read_some() function will exit with the boost::asio::error::eof error, 
	// which is how we know to exit the loop.
	if (len <= 0)
		return;
	if (error == boost::asio::error::eof)
		bRun = false; // Connection closed cleanly by peer.
	else if (error)
		throw boost::system::system_error(error); // Some other error.

	//cout.write(buf.data(), len);
	std::string data(buf.begin(), buf.begin()+len-1); 
	choiceAction(data);
}

void Server::write()
{
	boost::system::error_code ignored_error;
	boost::asio::write(socket, boost::asio::buffer(sSend+"\n"), ignored_error);
	bSend = false;
}

void Server::choiceAction(string choice)
{
	vector<string> splitVec;
	boost::split(splitVec, choice, boost::is_any_of(","),boost::token_compress_on);
	//cout << splitVec.at(0) << "||" << splitVec.at(1) << endl;
	if (splitVec.at(0) == ID) {
		CheckID checkId;
		if (checkId.check(splitVec.at(1))) {
			sSend = ALLOWED;
			bSend = true;
			cout << DEBUG << " true\n";
		}
		else {
			cout << DEBUG << " false\n";
			bRun = false;
		}
	}
	else if (splitVec.at(0) ==  DEBUG) {
		cout << splitVec.at(0) << " || " << splitVec.at(1) << endl;
	}
	else if (splitVec.at(0) ==  VOLUME) {
		//cout << splitVec.at(0) << " || " << splitVec.at(1) << endl;
		if (splitVec.at(1) == "+") {
			controlComp->changeVolume(0.05);
		}
		else if (splitVec.at(1) == "-") {
			controlComp->changeVolume(-0.05);
		}
		else {
			controlComp->muteVolume();
		}
	}
	else if (splitVec.at(0) == KEY) {
		controlComp->keyEmulation(splitVec.at(1));
	}
}
