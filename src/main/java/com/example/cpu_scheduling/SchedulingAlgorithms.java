package com.example.cpu_scheduling;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.text.DecimalFormat;
import java.util.*;

import static java.lang.System.exit;

public class SchedulingAlgorithms {
    static List<Process> processesAll = new ArrayList<>();

    DecimalFormat df = new DecimalFormat("#.00");

    public void setProcessesAll(List<Process> processesAll){
        SchedulingAlgorithms.processesAll = processesAll;
    }
    SchedulingAlgorithms(List<Process> processesAll){
        SchedulingAlgorithms.processesAll = processesAll;
    }

    double[][] averages = new double[5][2];

    // First Come First Served (FCFS)
    public void fcfs(TableView<Process> tableViewOutput, Label outputWaitingTime, Label outputTurnAroundTime) {

        Process c = Collections.min(processesAll, Comparator.comparingInt(p -> (p.arrivalTime)));
        List<Process> processes = processesAll.stream()
                .sorted(Comparator.comparingInt(p -> p.arrivalTime))
                .toList();
        int currentTime = c.arrivalTime;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        ObservableList<Process> p = FXCollections.observableArrayList();

        for (Process process : processes) {
            System.out.println("Process " + process.name + " starts at " + currentTime);
            process.waitingTime =currentTime - process.arrivalTime;
            currentTime += process.burstTime;
            System.out.println("Process " + process.name + " ends at " + currentTime);
            process.turnAroundTime = currentTime - process.arrivalTime;

            p.add(new Process(process.getName(),process.getBurstTime(),process.getPriority(),process.getArrivalTime(),process.getWaitingTime(),process.getTurnAroundTime()));
        }
        tableViewOutput.setItems(p);

        for (Process process : processes) {
            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnAroundTime;
        }

        System.out.println("Average Waiting Time: " + df.format((double) totalWaitingTime / processesAll.size()));
        System.out.println("Average Turnaround Time: " + df.format((double) totalTurnaroundTime / processesAll.size()) + "\n");

        outputWaitingTime.setText((df.format((double) totalWaitingTime / processes.size())));
        outputTurnAroundTime.setText((df.format((double) totalTurnaroundTime / processes.size())));

        averages[0][0] = (double) totalWaitingTime / processes.size();
        averages[0][1] = (double) totalTurnaroundTime / processes.size();

        averages[2][0] = 0.0;
        averages[2][1] = 0.0;
    }

    // Round Robin
    public void roundRobin(TableView<Process> tableViewOutput, Label outputWaitingTime, Label outputTurnAroundTime,int timeQuantum) throws InterruptedException {

        Process c = Collections.min(processesAll, Comparator.comparingInt(p -> (p.arrivalTime)));
        List<Process> processes = processesAll.stream()
                    .sorted(Comparator.comparingInt(p -> p.arrivalTime))
                    .toList();
        int currentTime = c.arrivalTime;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int completed = 0;

        ObservableList<Process> p = FXCollections.observableArrayList();

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
                        p.add(new Process(process.name,process.burstTime,process.priority,process.arrivalTime,process.waitingTime,process.turnAroundTime));
                    } else {
                        currentTime += timeQuantum;
                        process.remainingTime -= timeQuantum;
                        System.out.println("Process " + process.name + " interrupted. Remaining time: " + process.remainingTime);
                    }
                }
            }
        }

        tableViewOutput.setItems(p);

        System.out.println("Time Quantum: " + timeQuantum);
        System.out.println("Average Waiting Time: " + df.format((double) totalWaitingTime / processesAll.size()));
        System.out.println("Average Turnaround Time: " + df.format((double) totalTurnaroundTime / processesAll.size()) + "\n");

        outputWaitingTime.setText((df.format((double) totalWaitingTime / processes.size())));
        outputTurnAroundTime.setText((df.format((double) totalTurnaroundTime / processes.size())));

        averages[1][0] = (double) totalWaitingTime / processes.size();
        averages[1][1] = (double) totalTurnaroundTime / processes.size();
    }

    // Shortest Process Next
//    public void shortestProcessNext(TableView<Process> tableViewOutput, Label outputWaitingTime, Label outputTurnAroundTime) {
//        List<Process> processes = new ArrayList<>(processesAll.stream().toList());
//
//        Process c = Collections.min(processes, Comparator.comparingInt(p -> (p.arrivalTime)));
//        int currentTime = c.arrivalTime;
//        int totalWaitingTime = 0;
//        int totalTurnaroundTime = 0;
//
//        ObservableList<Process> pro = FXCollections.observableArrayList();
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
//            pro.add(new Process(p.name,p.burstTime,p.priority,p.arrivalTime,p.waitingTime,p.turnAroundTime));
//
//            processes.remove(p);
//        }
//
//        tableViewOutput.setItems(pro);
//
//        System.out.println("Average Waiting Time: " + df.format((double) totalWaitingTime / processesAll.size()));
//        System.out.println("Average Turnaround Time: " + df.format((double) totalTurnaroundTime / processesAll.size()) + "\n");
//
//        outputWaitingTime.setText((df.format((double) totalWaitingTime / processes.size())));
//        outputTurnAroundTime.setText((df.format((double) totalTurnaroundTime / processes.size())));
//
//        averages[2][0] = (double) totalWaitingTime / processes.size();
//        averages[2][1] = (double) totalTurnaroundTime / processes.size();
//    }

    // Shortest Remaining Time Next
//    public void shortestRemainingTimeNext(TableView<Process> tableViewOutput, Label outputWaitingTime, Label outputTurnAroundTime) {
//        List<Process> processes = new ArrayList<>(processesAll.stream().toList());
//
//        int currentTime = 0;
//        int totalWaitingTime = 0;
//        int totalTurnaroundTime = 0;
//
//        ObservableList<Process> pro = FXCollections.observableArrayList();
//
//        while (!processes.isEmpty()) {
//            Process currentProcess = null;
//            int shortestRemainingTime = Integer.MAX_VALUE;
//
//            for (Process p : processes) {
//                if (p.arrivalTime <= currentTime && p.remainingTime > 0 && p.remainingTime < shortestRemainingTime) {
//                    currentProcess = p;
//                    shortestRemainingTime = p.remainingTime;
//                }
//            }
//
//            if (currentProcess == null) {
//                currentTime++;
//                continue;
//            }
//
//            System.out.println("Process " + currentProcess.name + " starts at " + currentTime);
//
//            currentTime++;
//            currentProcess.remainingTime--;
//
//            if (currentProcess.remainingTime == 0) {
//                System.out.println("Process " + currentProcess.name + " ends at " + currentTime);
//                currentProcess.turnAroundTime = currentTime - currentProcess.arrivalTime;
//                currentProcess.waitingTime = currentProcess.turnAroundTime - currentProcess.burstTime;
//                totalTurnaroundTime += currentProcess.turnAroundTime;
//                totalWaitingTime += currentProcess.waitingTime;
//                processes.remove(currentProcess);
//
//                pro.add(new Process(currentProcess.name,currentProcess.burstTime,currentProcess.priority,currentProcess.arrivalTime,currentProcess.waitingTime,currentProcess.turnAroundTime));
//            }
//        }
//
//        tableViewOutput.setItems(pro);
//
//        System.out.println("Average Waiting Time: " + df.format((double) totalWaitingTime / processesAll.size()));
//        System.out.println("Average Turnaround Time: " + df.format((double) totalTurnaroundTime / processesAll.size()) + "\n");
//
//        outputWaitingTime.setText((df.format((double) totalWaitingTime / processes.size())));
//        outputTurnAroundTime.setText((df.format((double) totalTurnaroundTime / processes.size())));
//
//        averages[3][0] = (double) totalWaitingTime / processesAll.size();
//        averages[3][1] = (double) totalTurnaroundTime / processesAll.size();
//    }

    // Priority Scheduling
    public void priorityScheduling(TableView<Process> tableViewOutput, Label outputWaitingTime, Label outputTurnAroundTime) {

        Process c = Collections.min(processesAll, Comparator.comparingInt(p -> (p.arrivalTime)));
        List<Process> processes = new ArrayList<>(processesAll.stream()
                .sorted(Comparator.comparingInt(p -> p.arrivalTime))
                .toList());
        int currentTime = c.arrivalTime;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        ObservableList<Process> pro = FXCollections.observableArrayList();

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

            pro.add(new Process(p.name,p.burstTime,p.priority,p.arrivalTime,p.waitingTime,p.turnAroundTime));

            processes.remove(p);
        }

        tableViewOutput.setItems(pro);

        System.out.println("Average Waiting Time: " + df.format((double) totalWaitingTime / processesAll.size()));
        System.out.println("Average Turnaround Time: " + df.format((double) totalTurnaroundTime / processesAll.size()) + "\n");

        outputWaitingTime.setText((df.format((double) totalWaitingTime / processesAll.size())));
        outputTurnAroundTime.setText((df.format((double) totalTurnaroundTime / processesAll.size())));

        averages[4][0] = (double) totalWaitingTime / processesAll.size();
        averages[4][1] = (double) totalTurnaroundTime / processesAll.size();
    }

    public double[][] getAverage(){
        return averages;
    }
}