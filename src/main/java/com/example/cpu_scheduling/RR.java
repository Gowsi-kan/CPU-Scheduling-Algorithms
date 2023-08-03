package com.example.cpu_scheduling;

import java.util.ArrayList;
import java.util.List;

public class RR {

    public void setProcessesAll(List<Process> processesAll) {
        RR.processesAll = processesAll;
    }

    public void setQuantumTime(int quantumTime) {
        RR.quantumTime = quantumTime;
    }

    public void calculateAverage(){
        RR.roundRobin(RR.quantumTime);
    }

    static List<Process> processesAll = new ArrayList<>();
    public static int quantumTime = 0;
    RR(List<Process> processesAll,int quantumTime){
        RR.processesAll = processesAll;
        RR.quantumTime = quantumTime;
        RR.roundRobin(RR.quantumTime);
    }

    RR(){

    }
    public double[][] getAverages() {
        return averages;
    }

    static double[][] averages = new double[1][2];

    public static void roundRobin(int timeQuantum) {
        List<Process> processes = new ArrayList<>(processesAll.stream().toList());

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int completed = 0;

        while (completed < processes.size()) {
            for (Process process : processes) {
                if (process.remainingTime > 0 && process.arrivalTime <= currentTime) {
                    System.out.println("Process " + process.name + " starts at " + currentTime);
                    if (process.remainingTime <= timeQuantum) {
                        currentTime += process.remainingTime;
                        System.out.println("Process " + process.name + " ends at " + currentTime);
                        process.remainingTime = 0;
                        process.waitingTime = currentTime - process.burstTime - process.arrivalTime;
                        totalWaitingTime += process.waitingTime;
                        process.turnAroundTime = currentTime - process.arrivalTime;
                        totalTurnaroundTime += process.turnAroundTime;
                        completed++;
                    } else {
                        currentTime += timeQuantum;
                        process.remainingTime -= timeQuantum;
                        System.out.println(
                                "Process " + process.name + " interrupted. Remaining time: " + process.remainingTime);
                    }
                }
            }
        }

        averages[0][0] = (double) totalWaitingTime / processes.size();
        averages[0][1] = (double) totalTurnaroundTime / processes.size();
    }

}
