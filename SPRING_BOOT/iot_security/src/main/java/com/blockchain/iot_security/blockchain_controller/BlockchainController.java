package com.blockchain.iot_security.blockchain_controller;

import com.blockchain.iot_security.blockchain.Blockchain;
import com.blockchain.iot_security.sensor_data.SensorData;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class BlockchainController {

    private final Blockchain blockchain;

    public BlockchainController() {
        this.blockchain = new Blockchain();
    }

    @PostMapping("/sensor-data")
    public Map<String, String> addSensorData(@RequestBody SensorData sensorData) {
        String data = String.format("MQ2 Gas Value: %d, Pressure: %.2f, Temperature: %.2f",
                sensorData.getGasValue(), sensorData.getPressure(), sensorData.getTemperature());

        blockchain.addBlock(data);

        Blockchain.Block latestBlock = blockchain.getLatestBlock();
        System.out.println("Added new block to blockchain:");
        System.out.println("Index: " + latestBlock.index);
        System.out.println("Data: " + latestBlock.getData());
        System.out.println("Previous Hash: " + latestBlock.getPreviousHash());
        System.out.println("Hash: " + latestBlock.getHash());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Sensor data added to the blockchain.");
        return response;
    }

    @GetMapping("/latest-sensor-data")
    public Blockchain.Block getLatestSensorData() {
        Blockchain.Block latestBlock = blockchain.getLatestBlock();

        System.out.println("Fetching latest block from blockchain:");
        System.out.println("Index: " + latestBlock.index);
        System.out.println("Data: " + latestBlock.getData());
        System.out.println("Previous Hash: " + latestBlock.getPreviousHash());
        System.out.println("Hash: " + latestBlock.getHash());

        return latestBlock;
    }

    @GetMapping("/get-blockchain")
    public List<Blockchain.Block> getBlockchain() {
        System.out.println("Blockchain:");
        for (Blockchain.Block block : blockchain.getChain()) {
            System.out.println("Index: " + block.index);
            System.out.println("Data: " + block.getData());
            System.out.println("Previous Hash: " + block.getPreviousHash());
            System.out.println("Hash: " + block.getHash());
            System.out.println("-------------------------");
        }
        return blockchain.getChain();
    }
}
