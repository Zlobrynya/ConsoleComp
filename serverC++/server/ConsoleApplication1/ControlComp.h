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
	bool changeVolume(double nVolume);
	void muteVolume();
	void keyEmulation(string id);

private:
	IAudioEndpointVolume* initEndpointVolume(HRESULT hr);
};

