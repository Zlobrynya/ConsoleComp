#pragma once
#include <mmdeviceapi.h>
#include <endpointvolume.h>
#include <iostream>

class ControlComp
{
public:
	ControlComp();
	~ControlComp();
	bool changeVolume(double nVolume);
	void muteVolume();

private:
	IAudioEndpointVolume* initEndpointVolume(HRESULT hr);
};

