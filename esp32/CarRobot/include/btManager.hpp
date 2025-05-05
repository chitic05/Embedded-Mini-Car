#include <Arduino.h>
#include <BluetoothSerial.h>
#include <cstring>

BluetoothSerial serialBt;

class btManager{
  public:
    btManager(HardwareSerial& serial) : serial(serial){
      if (!serialBt.begin("ESP-32")) {
        serial.println("An error occurred initializing Bluetooth");
      } else {
        serial.println("Bluetooth initialized");
      }
    } 
    
    void Send(char* message){
      serialBt.println(message);
    }
    
    void Receieve(char* currentLine) {
      if(serialBt.available()) {
        int character = serialBt.read();

        if (character != '\n' && newLineIndex < 127) { // Check for newline and buffer overflow
          newLine[newLineIndex++] = (char)character;
          newLine[newLineIndex] = '\0'; // Null-terminate the string
        } else if (character == '\n') {

          strcpy(currentLine, newLine);
          serial.println(currentLine);
          newLineIndex = 0; // Reset index
          newLine[0] = '\0'; // Clear the string
        }
      }
    }
    
  private:
    
    HardwareSerial serial;
    char newLine[128] = "";
    int newLineIndex = 0;
    
};
