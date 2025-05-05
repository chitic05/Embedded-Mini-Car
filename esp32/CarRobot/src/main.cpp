#include <btManager.hpp>
#include <ESP32Servo.h>

Servo servo;
int forwardMotor = 12, reverseMotor = 14;
int m = 0, s = 0, steeringAngle;
btManager *bt = nullptr;
char *message = new char[128];

void processMessage(const char* message);
void drive();

void setup() {
  Serial.begin(9600);
  bt = new btManager(Serial);
  pinMode(forwardMotor, OUTPUT);
  pinMode(reverseMotor, OUTPUT);
  digitalWrite(forwardMotor, LOW);
  digitalWrite(reverseMotor, LOW);
  servo.attach(27);
  steeringAngle = 100;
  servo.write(steeringAngle);
}

void loop() {
  bt->Receieve(message);
  processMessage(message);
  drive();
}

void processMessage(const char* message){
      if(strcmp(message, "forward") == 0) m=1;
      else if(strcmp(message, "reverse") == 0) m=-1;
      else if(strcmp(message, "stop") == 0) m=0;
     

      if(strcmp(message, "left") == 0) s=-1;
      else if(strcmp(message, "right") == 0) s=1;
      else if(strcmp(message, "stopSteering") == 0) s=0;
      
    }

void drive(){
  switch(m){
    case 1:
      digitalWrite(forwardMotor, HIGH);
      digitalWrite(reverseMotor, LOW);
      break;
    case -1:
      digitalWrite(forwardMotor, LOW);
      digitalWrite(reverseMotor, HIGH);
      break;
    default:
      digitalWrite(forwardMotor, LOW);
      digitalWrite(reverseMotor, LOW);
      break;
  }

  switch(s){
    case 1:
      steeringAngle = 120;
      break;
    case -1:
      steeringAngle = 80;
      break;
    default:
      steeringAngle = 100;
      break;
  }
  servo.write(steeringAngle);
}
