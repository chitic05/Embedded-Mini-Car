# Embedded Mini Car - Chiticontrol

A comprehensive embedded systems project featuring a four-wheeled mobile robot controlled via Bluetooth through a custom Android application. The project demonstrates integration of modern hardware and software technologies with a focus on custom Bluetooth communication implementation.

## Project Overview

**Chiticontrol** is a wireless control system for a four-wheeled robot featuring:
- **Rear-Wheel Drive (RWD)** propulsion system
- **Servo-controlled front-wheel steering** for efficient maneuverability
- **Custom-built Bluetooth communication** between Android app and ESP32 microcontroller
- **Modular architecture** with dedicated Bluetooth management classes

The project showcases hands-on integration of embedded systems, IoT, and mobile application development.

## Technologies Used

### C++
Used for ESP32 firmware development with full hardware control and low-level Bluetooth serial communication. Provides direct memory management and efficient resource utilization essential for embedded systems.

### Java & Android
The control application is developed in Java using Android Studio, providing an intuitive user interface for robot operation with Bluetooth connectivity.

### Android Studio
Official IDE for Android application development, featuring visual layout editing, integrated testing tools, and performance profiling.

### ESP32
A powerful microcontroller with integrated Wi-Fi and Bluetooth capabilities making it ideal for IoT and embedded applications. Provides dual-core processing, GPIO, UART, SPI, I2C and other peripherals.

## Repository Structure

```
Embedded-Mini-Car/
├── AplicațieAndroid/              # Android application (Gradle project)
│   └── app/src/main/
│       ├── java/com/example/myapplication/MainActivity.java
│       ├── java/custom/BluetoothManager.java
│       └── res/                   # UI resources and layouts
└── esp32/CarRobot/                # ESP32 firmware (PlatformIO project)
    ├── src/main.cpp               # Firmware entry point
    └── include/btManager.hpp      # Bluetooth manager header
```

## Getting Started

### Prerequisites

**Android Development:**
- Android Studio (or command-line Gradle)
- Android SDK with API 21+
- Android emulator or physical device with Bluetooth
- Appropriate permissions granted to the app

**ESP32 Development:**
- PlatformIO (recommended via VS Code)
- Or ESP-IDF toolchain
- Espressif ESP32 development board
- USB cable for flashing

### Android — Build & Run

#### Using Android Studio
1. Open `AplicațieAndroid` folder as an Android project
2. Run the `app` module (Build → Run)

#### From Command Line (Linux/macOS)
```bash
cd AplicațieAndroid
./gradlew assembleDebug
./gradlew installDebug   # installs to connected device/emulator
```

**Notes:**
- Install any missing Android SDK components when prompted
- Ensure Bluetooth is enabled and app permissions are granted
- Grant location permissions (required on Android 6.0+ for Bluetooth scanning)

### ESP32 (CarRobot) — Build & Flash

#### Using PlatformIO (Recommended)
```bash
cd esp32/CarRobot
pio run                    # build firmware
pio run --target upload    # build and upload to connected ESP32
```

#### In VS Code
- Open the PlatformIO extension panel
- Select your environment and serial port
- Use the build and upload buttons

**Notes:**
- If multiple environments exist in `platformio.ini`, use `-e <environment>` to select target
- VS Code integration provides visual serial port selection and easier debugging

## Operating the Robot

### Hardware Setup

1. **Power On:**
   - Connect ESP32 via micro-USB cable to a power source
   - Toggle the battery switch on
   - Two red LEDs will light up:
     - One on the ESP32 board
     - One on the motor driver
   - This indicates both components are operational

2. **Bluetooth Pairing:**
   - Enable Bluetooth on your Android phone
   - Pair with device named **"ESP-32"**

### Using the Application

1. Launch the **Chiticontrol** app
2. Grant the required permissions
3. Wait for **"Connected"** message at the top of the screen
4. Use the four directional buttons to control the robot:
   - **Forward** — Drive ahead
   - **Backward** — Drive in reverse
   - **Left** — Turn left
   - **Right** — Turn right

## Video Demonstration

Watch the robot in action:
[Chiticontrol Demo Video](https://youtu.be/GVRYfFZE36M?feature=shared)

## Bluetooth Communication

### Architecture

The project implements custom Bluetooth communication from scratch:

- **BluetoothManager.java** — Android-side Bluetooth handler
  - Manages Bluetooth device discovery and connection
  - Handles bidirectional serial communication
  - Implements permission handling for Android 6.0+

- **btManager.hpp** — ESP32-side Bluetooth handler
  - Initializes Bluetooth serial interface
  - Manages message reception and transmission
  - Implements line-buffered protocol

### Protocol

Simple text-based protocol:
- Commands transmitted as single characters: `F` (Forward), `B` (Backward), `L` (Left), `R` (Right)
- Newline (`\n`) character indicates end of message
- Buffer size: 128 bytes per message

## Project Features

✅ **Custom Bluetooth Implementation** — Built from scratch for both Android and ESP32  
✅ **Modular Design** — Dedicated manager classes for code reusability  
✅ **Real-time Control** — Responsive robot movement with instant feedback  
✅ **Scalable Architecture** — Easy to extend with additional sensors or features  
✅ **Educational Value** — Demonstrates embedded systems integration concepts  

## Troubleshooting

| Issue | Solution |
|-------|----------|
| Android app cannot find ESP32 | Ensure Bluetooth is enabled, ESP32 is powered on, and device name is "ESP-32" |
| Robot doesn't respond to commands | Verify the "Connected" status; try re-pairing the Bluetooth device |
| ESP32 won't upload firmware | Check USB cable connection and verify COM port in PlatformIO configuration |
| LEDs don't light up | Check power supply and battery connection; verify ESP32 is correctly connected to motor driver |

## Requirements Met

This project demonstrates:
- IoT device control via wireless protocol
- Custom low-level Bluetooth communication
- Hardware-software integration
- Mobile application development
- Embedded systems programming
- Modular code architecture

## Author

- **Chitic David-Alexandru**

## License

This project is provided for educational purposes.

## Key files

- `AplicațieAndroid/app/src/main/java/com/example/myapplication/MainActivity.java` — app UI and control flow.
- `AplicațieAndroid/app/src/main/java/custom/BluetoothManager.java` — Bluetooth helper (pairing, connect, send/receive).
- `esp32/CarRobot/src/main.cpp` — main firmware logic for the car.
- `esp32/CarRobot/include/btManager.hpp` — ESP32 Bluetooth helper header.

## How it works (high level)

- Android app sends commands (via Bluetooth) to the ESP32.
- ESP32 firmware receives commands and actuates motors accordingly.

## Contributing

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/my-change`.
3. Make changes and test on the appropriate hardware or emulator.
4. Open a pull request with a clear description of changes and how to test.

If you add new hardware wiring notes or pin mappings, please update this README.

## Troubleshooting

- Android: If Bluetooth permissions or scanning fail, check the app permissions and location settings (Android requires location permission for scanning on some versions).
- ESP32: If upload fails, confirm the serial port and that the board is in programming mode; try pressing the ESP32 boot/EN buttons during upload if needed.

## Contact

Maintainer: repository owner (see GitHub `chitic05`).
