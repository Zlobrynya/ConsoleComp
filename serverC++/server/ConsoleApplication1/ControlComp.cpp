#include "stdafx.h"
#include "ControlComp.h"


ControlComp::ControlComp()
{
	
	CoUninitialize();
}


ControlComp::~ControlComp()
{
}

//Метод изменения звука 
bool ControlComp::changeVolume(double nVolume)
{
	HRESULT hr = NULL;
	CoInitialize(NULL);
	IAudioEndpointVolume *endpointVolume = initEndpointVolume(hr);
	float currentVolume = 0;
	endpointVolume->GetMasterVolumeLevelScalar(&currentVolume);
	double newVolume = currentVolume + nVolume;

	hr = endpointVolume->GetMasterVolumeLevelScalar(&currentVolume);
	hr = endpointVolume->SetMasterVolumeLevelScalar((float)newVolume, NULL);

	endpointVolume->Release();
	endpointVolume = NULL;
	CoUninitialize();
	return FALSE;
}

void ControlComp::muteVolume()
{
	HRESULT hr = NULL;
	CoInitialize(NULL);
	IAudioEndpointVolume *endpointVolume = initEndpointVolume(hr);
	BOOL mute;
	endpointVolume->GetMute(&mute);
	endpointVolume->SetMute(!mute, NULL);
	endpointVolume->Release();
	endpointVolume = NULL;
	CoUninitialize();
}

void ControlComp::keyEmulation(string id)
{
	HWND hwnd = FindWindow("QWidget", "VLC media player");
	if (hwnd == NULL) {
		hwnd = FindWindow("QWidget","GOM Player");
		if (hwnd == NULL) {
			hwnd = FindWindow("Google Chrome", NULL);
		}
	}
	//HWND hwnd = FindWindow("Google Chrome", NULL);
    SetForegroundWindow(hwnd);
	SendMessage(hwnd, WM_ACTIVATE, WA_ACTIVE, 0);
	SendMessage(hwnd, WM_SETFOCUS, NULL, 0);
	keybd_event(atoi(id.c_str()), 0, 0, 0); //нажата клавиша
	keybd_event(atoi(id.c_str()), 0, KEYEVENTF_KEYUP, 0); //клавиша отпущена
}

//Инициализация IAudioEndpointVolume
IAudioEndpointVolume* ControlComp::initEndpointVolume(HRESULT hr)
{
	IMMDeviceEnumerator *deviceEnumerator = NULL;
	hr = CoCreateInstance(__uuidof(MMDeviceEnumerator), NULL, CLSCTX_INPROC_SERVER,
		__uuidof(IMMDeviceEnumerator), (LPVOID *)&deviceEnumerator);
	IMMDevice *defaultDevice = NULL;

	hr = deviceEnumerator->GetDefaultAudioEndpoint(eRender, eConsole, &defaultDevice);
	deviceEnumerator->Release();
	deviceEnumerator = NULL;
	IAudioEndpointVolume *endpointVolume = NULL;
	hr = defaultDevice->Activate(__uuidof(IAudioEndpointVolume),
		CLSCTX_INPROC_SERVER, NULL, (LPVOID *)&endpointVolume);
	defaultDevice->Release();
	defaultDevice = NULL;
	return endpointVolume;
}
