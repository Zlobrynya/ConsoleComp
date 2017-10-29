#pragma once

#include <mmdeviceapi.h>
#include <endpointvolume.h>
#include <iostream>
#include <string>
#include <Windows.h>

using namespace std;

class ControlComp
{
public:
	ControlComp();
	~ControlComp();
	void keyEmulation(string id);
};

