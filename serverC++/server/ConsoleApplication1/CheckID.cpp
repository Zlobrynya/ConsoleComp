#include "stdafx.h"
#include "CheckID.h"


CheckID::CheckID()
{
}


CheckID::~CheckID()
{
}

bool CheckID::check(string id)
{
	vector<string> vId;
	vId = getID();
	if (vId.size() <= 0) {
		return connectionRequest(id);
	}
	else {
		for (int i = 0; i < vId.size(); i++) {
			if (vId.at(i).compare(id)) {
				return true;
			}
		}
	}
	return connectionRequest(id);
}

bool CheckID::connectionRequest(string id)
{
	cout << "Попытка подключения неизвестного устройства: " << id << endl;
	cout << "Разрешить подключение? (y/n)" << endl;
	char b;
	cin >> b;
	if (b == 'y') {
		writeFile(id);
		cout << "Подключение разрешено.\n";
		return true;
	}
	cout << "Подключение запрещено.\n";
	return false;
}

vector<string> CheckID::getID()
{
	ifstream fin("id");
	vector<string> id;
	if (fin.is_open()) {
		while (!fin.eof()) {
			string data;
			getline(fin, data);
			id.push_back(data);
		}
	}
	fin.close();
	return id;
}

void CheckID::writeFile(string id)
{
	ofstream fout;
	fout.open("id", ios_base::app);
	fout << hashCode(id) << "\n";
	fout.close();
}

int CheckID::hashCode(string id)
{
	boost::hash<std::string> string_hash;
	return string_hash(id);;
}
