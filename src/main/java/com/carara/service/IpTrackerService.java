package com.carara.service;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * This class is responsible for tracking IP addresses, counting the number of requests
 * from each IP, and maintaining a list of the top 100 IPs by request count.
 */
@Service
public class IpTrackerService {

    private Map<String, Integer> ipCountMap;
    private Queue<Map.Entry<String, Integer>> minHeap;
    private static final int MAX_SIZE = 100;
    private static final Logger logger = Logger.getLogger(IpTrackerService.class.getName());


    public IpTrackerService() {
        ipCountMap = new HashMap<>();
        minHeap = new PriorityQueue<>(MAX_SIZE, Comparator.comparingInt(Map.Entry::getValue));
    }

    /**
     * Stores the IP address, updating its request count and the top 100 IPs list.
     *
     * @param ipAddress The IP address to be stored
     */
    public void requestHandled(String ipAddress) {
        ipCountMap.compute(ipAddress, (k, v) -> v == null ? 1 : v + 1);

        Map.Entry<String, Integer> entry = new AbstractMap.SimpleEntry<>(ipAddress, ipCountMap.get(ipAddress));

        minHeap.removeIf(e -> e.getKey().equals(entry.getKey()) && !e.equals(entry));

        if (minHeap.size() < MAX_SIZE) {
            minHeap.offer(entry);
        } else {
            if (entry.getValue() > minHeap.peek().getValue()) {
                minHeap.poll();
                minHeap.offer(entry);
            }
        }
        logger.log(Level.INFO, "IP address {0} has been stored.", ipAddress);
    }

    /**
     * Returns a sorted list of the top 100 IP addresses by request count.
     *
     * @return A List of Map.Entry objects representing the top 100 IP addresses and their request counts
     */
    public List<Map.Entry<String, Integer>> top100() {
        logger.info("Top 100 IP addresses have been retrieved.");
        return minHeap.stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    /**
     * Clears the HashMap map and the PriorityQueue, effectively resetting the IPAddressTracker instance.
     */
    public void clear() {
        ipCountMap.clear();
        minHeap.clear();
        logger.info("IP address tracker has been cleared.");
    }
}

