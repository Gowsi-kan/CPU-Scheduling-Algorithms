package com.example.cpu_scheduling;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SPN {

    static List<Process> processesAll = new ArrayList<>();

    SPN(List<Process> processesAll) {
        SPN.processesAll = processesAll;
    }

    public double[][] getAverages() {
        return averages;
    }

    double[][] averages = new double[2][2];

    public void shortestProcessNext(TableView<Process> tableViewOutput, Label outputWaitingTime, Label outputTurnAroundTime) {
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
                    .sorted(Comparator.comparingInt(p -> p.burstTime))
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

        System.out.println("Average Waiting Time: " + (double) totalWaitingTime / processesAll.size());
        System.out.println("Average Turnaround Time: " + (double) totalTurnaroundTime / processesAll.size() + "\n");

        outputWaitingTime.setText(Double.toString((double) totalWaitingTime / processesAll.size()));
        outputTurnAroundTime.setText(Double.toString((double) totalTurnaroundTime / processesAll.size()));

        averages[0][0] = (double) totalWaitingTime / processesAll.size();
        averages[0][1] = (double) totalTurnaroundTime / processesAll.size();
    }
    public void shortestProcessNext() {
        Process c = Collections.min(processesAll, Comparator.comparingInt(p -> (p.arrivalTime)));
        List<Process> processes = new ArrayList<>(processesAll.stream()
                .sorted(Comparator.comparingInt(p -> p.arrivalTime))
                .toList());
        int currentTime = c.arrivalTime;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        while (!processes.isEmpty()) {
            int finalCurrentTime = currentTime;
            List<Process> sorted = processes.stream()
                    .filter(p -> p.arrivalTime <= finalCurrentTime)
                    .sorted(Comparator.comparingInt(p -> p.burstTime))
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
        System.out.println("Average Turnaround Time: " + (double) totalTurnaroundTime / processesAll.size() + "\n");

        averages[0][0] = (double) totalWaitingTime / processesAll.size();
        averages[0][1] = (double) totalTurnaroundTime / processesAll.size();
    }

    public void shortestRemainingTimeNext(TableView<Process> tableViewOutput, Label outputWaitingTime, Label outputTurnAroundTime) {
        Process c = Collections.min(processesAll, Comparator.comparingInt(p -> (p.arrivalTime)));
        List<Process> processes = new ArrayList<>(processesAll.stream()
                .sorted(Comparator.comparingInt(p -> p.arrivalTime))
                .toList());
        int currentTime = c.arrivalTime;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        ObservableList<Process> pro = FXCollections.observableArrayList();

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

                pro.add(new Process(currentProcess.name,currentProcess.burstTime,currentProcess.priority,currentProcess.arrivalTime,currentProcess.waitingTime,currentProcess.turnAroundTime));
            }
        }

        tableViewOutput.setItems(pro);

        System.out.println("Average Waiting Time: " + (double) totalWaitingTime / processesAll.size());
        System.out.println("Average Turnaround Time: " + (double) totalTurnaroundTime / processesAll.size() + "\n");

        outputWaitingTime.setText(Double.toString((double) totalWaitingTime / processesAll.size()));
        outputTurnAroundTime.setText(Double.toString((double) totalTurnaroundTime / processesAll.size()));

        averages[1][0] = (double) totalWaitingTime / processesAll.size();
        averages[1][1] = (double) totalTurnaroundTime / processesAll.size();
    }
}
