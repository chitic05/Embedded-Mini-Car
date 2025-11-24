# Embedded Mini Car

A small project that contains two main parts:

- An Android application (`AplicațieAndroid`) to control the car via Bluetooth.
- ESP32 firmware (`esp32/CarRobot`) that runs on the car's microcontroller (PlatformIO project).

## Repository layout

- `AplicațieAndroid/` — Android Studio project. App entry point: `app/src/main/java/com/example/myapplication/MainActivity.java`.
  - Bluetooth helper: `app/src/main/java/custom/BluetoothManager.java`.
- `esp32/CarRobot/` — PlatformIO project with ESP32 firmware.
  - Firmware entrypoint: `esp32/CarRobot/src/main.cpp`.
  - Headers: `esp32/CarRobot/include/btManager.hpp`.

## Prerequisites

- Android: Android Studio (or command-line Gradle), Android SDK and an emulator or device.
- ESP32: PlatformIO (recommended via VS Code) or the ESP-IDF toolchain. USB cable to flash the board.

## Android — Build & Run

Open `AplicațieAndroid` in Android Studio and run the `app` module.

From the command line (Linux/macOS):

```bash
cd AplicațieAndroid
./gradlew assembleDebug
./gradlew installDebug   # installs to a connected device/emulator
```

Notes:
- If Android Studio prompts for SDK components, install them.
- The app communicates over Bluetooth; ensure the phone's Bluetooth is enabled and permissions are granted.

## ESP32 (CarRobot) — Build & Flash

Using PlatformIO (recommended):

```bash
cd esp32/CarRobot
pio run               # build
pio run --target upload   # build and upload to a connected ESP32
```

If you have multiple environments in `platformio.ini`, use `-e <env>` to choose the target.

Using PlatformIO in VS Code provides an easier way to select the serial port and upload.

## Key files

- `AplicațieAndroid/app/src/main/java/com/example/myapplication/MainActivity.java` — app UI and control flow.
- `AplicațieAndroid/app/src/main/java/custom/BluetoothManager.java` — Bluetooth helper (pairing, connect, send/receive).
- `esp32/CarRobot/src/main.cpp` — main firmware logic for the car.
- `esp32/CarRobot/include/btManager.hpp` — ESP32 Bluetooth helper header.

## How it works (high level)

- Android app sends commands (via Bluetooth) to the ESP32.
- ESP32 firmware receives commands and actuates motors/sensors accordingly.

## Contributing

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/my-change`.
3. Make changes and test on the appropriate hardware or emulator.
4. Open a pull request with a clear description of changes and how to test.

If you add new hardware wiring notes or pin mappings, please update this README.

## Troubleshooting

- Android: If Bluetooth permissions or scanning fail, check the app permissions and location settings (Android requires location permission for scanning on some versions).
- ESP32: If upload fails, confirm the serial port and that the board is in programming mode; try pressing the ESP32 boot/EN buttons during upload if needed.

## License

This project does not include a license file. Add a `LICENSE` if you want to open-source the project. A common choice is the MIT License.

## Contact

Maintainer: repository owner (see GitHub `chitic05`).

If you'd like, I can add badges, a wiring diagram, or small CONTRIBUTING.md next.
