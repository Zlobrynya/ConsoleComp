#define _CRT_SECURE_NO_WARNINGS

#include <ctime>
#include <iostream>
#include <string>
#include "stdafx.h"
#include "Server.h"

using boost::asio::ip::tcp;



int main()
{
	setlocale(LC_ALL, "RUS");

	try
	{
		while (true) {
			// Any program that uses asio need to have at least one io_service object
			boost::asio::io_service io_service;
			Server servent(io_service);
			servent.start();
			cout << "DISC";
		}
	}
	catch (std::exception& e)
	{
		std::cerr << e.what() << std::endl;
	}
	std::getchar();
	return 0;
}