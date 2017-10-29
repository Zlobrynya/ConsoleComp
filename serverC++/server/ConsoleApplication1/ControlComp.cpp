#include "stdafx.h"
#include "ControlComp.h"


ControlComp::ControlComp()
{
	
	CoUninitialize();
}


ControlComp::~ControlComp()
{
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
	keybd_event(std::stoi(id, 0, 16), 0, 0, 0); //нажата клавиша
	keybd_event(std::stoi(id, 0, 16), 0, KEYEVENTF_KEYUP, 0); //клавиша отпущена
}
