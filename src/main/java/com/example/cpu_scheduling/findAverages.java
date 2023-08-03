package com.example.cpu_scheduling;

import java.util.*;

public class findAverages {
    static List<Process> processesAll = new ArrayList<>();

    public void setProcessesAll(List<Process> processesAll){
        findAverages.processesAll = processesAll;
    }
    findAverages(List<Process> processesAll){
        findAverages.processesAll = processesAll;
    }

    static double[][] averages = new double[5][2];
    // First Come First Served (FCFS)
    public void fcfs() {
        List<Process> processes = new ArrayList<>(processesAll.stream().toList());

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        for (Process process : processes) {
            System.out.println("Process " + process.name + " starts at " + currentTime);
            process.waitingTime = currentTime - process.arrivalTime;
            currentTime += process.burstTime;
            System.out.println("Process " + process.name + " ends at " + currentTime);
            process.turnAroundTime = currentTime - process.arrivalTime;
        }

        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnAroundTime;
            process.waitingTime = 0;
            process.turnAroundTime = 0;
        }

        System.out.println("Average Waiting Time: " + (double) totalWaitingTime / processes.size());
        System.out.println("Average Turnaround Time: " + (double) totalTurnaroundTime / processes.size());
        averages[0][0] = (double) totalWaitingTime / processes.size();
        averages[0][1] = (double) totalTurnaroundTime / processes.size();

        averages[2][0] = 0.0;
        averages[2][1] = 0.0;
    }

    // Round Robin
    public void roundRobin(int timeQuantum) {
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

        System.out.println("Average Waiting Time: " + (double) totalWaitingTime / processesAll.size());
        System.out.println("Average Turnaround Time: " + (double) totalTurnaroundTime / processesAll.size());
        averages[1][0] = (double) totalWaitingTime / processes.size();
        averages[1][1] = (double) totalTurnaroundTime / processes.size();
    }

    // Shortest Process Next
//    public void shortestProcessNext() {
//        List<Process> processes = new ArrayList<>(processesAll.stream().toList());
//
//        Process c = Collections.min(processes, Comparator.comparingInt(p -> (p.arrivalTime)));
//        int currentTime = c.arrivalTime;
//        int totalWaitingTime = 0;
//        int totalTurnaroundTime = 0;
//
//        while (!processes.isEmpty()) {
//            int finalCurrentTime = currentTime;
//            List<Process> sorted = processes.stream()
//                    .filter(p -> p.arrivalTime <= finalCurrentTime)
//                    .sorted(Comparator.comparingInt(p -> p.burstTime))
//                    .toList();
//
//            if (sorted.isEmpty()) {
//                currentTime++;
//                continue;
//            }
//
//            Process p = sorted.get(0);
//            System.out.println("Process " + p.name + " starts at " + currentTime);
//            p.waitingTime = currentTime - p.arrivalTime;
//            totalWaitingTime += p.waitingTime;
//            currentTime += p.burstTime;
//            System.out.println("Process " + p.name + " ends at " + currentTime);
//            p.turnAroundTime = currentTime - p.arrivalTime;
//            totalTurnaroundTime += p.turnAroundTime;
//
//            processes.remove(p);
//        }
//
//        System.out.println("Average Waiting Time: " + (double) totalWaitingTime / processesAll.size());
//        System.out.println("Average Turnaround Time: " + (double) totalTurnaroundTime / processesAll.size());
//
//        averages[2][0] = (double) totalWaitingTime / processes.size();
//        averages[2][1] = (double) totalTurnaroundTime / processes.size();
//    }

    // Shortest Remaining Time Next
    public void shortestRemainingTimeNext() {
        List<Process> processes = new ArrayList<>(processesAll.stream().toList());

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        while (!processes.isEmpty()) {
            Process currentProcess = null;
            int shortestRemainingTime = Integer.MAX_VALUE;

            for (Process p : processes) {
                if (p.arrivalTime <= currentTime && p.remainingTime > 0 && p.remainingTime < shortestRemainingTime) {
                    currentProcess = p;
                    shortestRemainingTime = p.remainingTime;
                }
            }

            if (currentProcess == null) {
                currentTime++;
                continue;
            }

            System.out.println("Process " + currentProcess.name + " starts at " + currentTime);

            currentTime++;
            currentProcess.remainingTime--;

            if (currentProcess.remainingTime == 0) {
                System.out.println("Process " + currentProcess.name + " ends at " + currentTime);
                currentProcess.turnAroundTime = currentTime - currentProcess.arrivalTime;
                currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.burstTime;
                totalTurnaroundTime += currentProcess.turnAroundTime;
                totalWaitingTime += currentProcess.waitingTime;
                processes.remove(currentProcess);
            }
        }

        System.out.println("Average Waiting Time: " + (double) totalWaitingTime / processesAll.size());
        System.out.println("Average Turnaround Time: " + (double) totalTurnaroundTime / processesAll.size());

        averages[3][0] = (double) totalWaitingTime / processesAll.size();
        averages[3][1] = (double) totalTurnaroundTime / processesAll.size();
    }

    // Priority Scheduling
    public void priorityScheduling() {
        List<Process> processes = new ArrayList<>(processesAll.stream().toList());

        Process c = Collections.min(processes, Comparator.comparingInt(p -> (p.arrivalTime)));
        int currentTime = c.arrivalTime;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        while (!processes.isEmpty()) {
            int finalCurrentTime = currentTime;
            List<Process> sorted = processes.stream()
                    .filter(p -> p.arrivalTime <= finalCurrentTime)
                    .sorted(Comparator.comparingInt(p -> p.priority))
                    .toList();

            if (sorted.isEmpty()) {
                currentTime++;
                continue;
            }

            Process p = sorted.get(0);
            System.out.println("Process " + p.name + " starts at " + currentTime);
            p.waitingTime = currentTime - p.arrivalTime;
            totalWaitingTime += p.waitingTime;
            currentTime += p.burstTime;
            System.out.println("Process " + p.name + " ends at " + currentTime);
            p.turnAroundTime = currentTime - p.arrivalTime;
            totalTurnaroundTime += p.turnAroundTime;

            processes.remove(p);
        }

        System.out.println("Average Waiting Time: " + (double) totalWaitingTime / processesAll.size());
        System.out.println("Average Turnaround Time: " + (double) totalTurnaroundTime / processesAll.size());

        averages[4][0] = (double) totalWaitingTime / processesAll.size();
        averages[4][1] = (double) totalTurnaroundTime / processesAll.size();
    }

    public double[][] getAverage(){
        return averages;
    }
}