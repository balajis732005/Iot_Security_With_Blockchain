#include <WiFi.h>
#include <HTTPClient.h>
#include <Wire.h>
#include <Adafruit_Sensor.h>
#include <Adafruit_BMP280.h>

const char* ssid = "BALAJI CLEVER CLOWN PHONE";
const char* password = "nohotspotforyou";

const char* serverUrl = "http://192.168.165.137:3000/sensor-data";

const int mq2Pin = 34; 

Adafruit_BMP280 bmp;

int gasValue = 0;
float pressure;
float temperature;

void setup() {
  Serial.begin(115200);

  Wire.begin(21, 22);

  if (!bmp.begin(0x76)) { 
    Serial.println("Could not find a valid BMP280 sensor, check wiring!");
    while (1);
  }

  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }
  Serial.println("Connected to WiFi");
}

void loop() {
  gasValue = analogRead(mq2Pin);
  
  pressure = bmp.readPressure() / 100.0F;
  temperature = bmp.readTemperature(); 

  if (WiFi.status() == WL_CONNECTED) {
    HTTPClient http;

    http.begin(serverUrl);

    http.addHeader("Content-Type", "application/json");

    String payload = "{\"gasValue\": " + String(gasValue) +
                     ", \"pressure\": " + String(pressure) +
                     ", \"temperature\": " + String(temperature) + "}";

    int httpResponseCode = http.POST(payload);

    if (httpResponseCode > 0) {
      String response = http.getString();
      Serial.println("Server Response: " + response);
    } else {
      Serial.println("Error in sending POST request");
    }

    http.end();
  }

  delay(5000);
}
