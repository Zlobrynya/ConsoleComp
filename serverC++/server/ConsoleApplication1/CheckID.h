#pragma once
#include <string>
#include <iostream>
#include <fstream>
#include <boost/functional/hash.hpp>

using namespace std;

class CheckID
{
public:
	CheckID();
	~CheckID();

	bool check(string id);
private:
	bool connectionRequest(string id);
	vector<string> getID();
	void writeFile(string id);
	int hashCode(string id);

};

