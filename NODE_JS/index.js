const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const { Blockchain, Block } = require('./blockchain'); 

const app = express();
const PORT = 3000;

app.use(cors());
app.use(bodyParser.json());

const sensorBlockchain = new Blockchain();

app.post('/sensor-data', (req, res) => {

  console.log('POST request received');
  console.log('Request body:', req.body);

  const { gasValue, pressure, temperature } = req.body;

  if (gasValue === undefined || pressure === undefined || temperature === undefined) {
    console.log('Invalid sensor data received');
    return res.status(400).send({ message: 'Invalid sensor data' });
  }

  const sensorData = {
    gasValue,
    pressure,
    temperature,
    timestamp: new Date().toISOString(),
  };

  console.log(`Received MQ2 Gas Sensor Value: ${gasValue}`);
  console.log(`Received BMP280 Pressure: ${pressure} hPa`);

  const newBlock = new Block(sensorBlockchain.chain.length, Date.now(), sensorData);
  sensorBlockchain.addBlock(newBlock);

  sensorBlockchain.printBlockchain();

  res.send({ message: 'Sensor data received and stored in blockchain' });
});

app.get('/latest-sensor-data', (req, res) => {
  const latestBlock = sensorBlockchain.getLatestBlock();
  res.json(latestBlock.data);
});

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});