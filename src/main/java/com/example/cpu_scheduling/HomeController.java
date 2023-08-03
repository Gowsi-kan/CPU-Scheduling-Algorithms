package com.example.cpu_scheduling;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.PortUnreachableException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import static javafx.scene.control.cell.TextFieldTableCell.*;

public class HomeController implements Initializable {
    static int quantumTime = 1;
    public Label description;
    public Label titleCPU;
    public Label titleCPU1;
    public Button viewOutputButton;

    public static int[] averageValues = new int[5];

    @FXML
    private Button addProcessButton;

    @FXML
    private Button removeProcessButton;
    List<Process> processes = new ArrayList<>();
    int pid = 1;
    @FXML
    private TableView<Process> tableView;

    @FXML
    private TableColumn<Process, Integer> arrivalTimeTable;

    @FXML
    private TableColumn<Process, Integer> burstTimeTable;

    @FXML
    private TableColumn<Process, Integer> priorityTable;

    @FXML
    private TableColumn<Process, Integer> processName;

    @FXML
    private TextField inputArrivalTime;

    @FXML
    private TextField inputBurstTime;

    @FXML
    private TextField inputPriority;

    @FXML
    private TextField inputQuantumTime;

    @FXML
    private Label outputTurnAroundTime;

    @FXML
    private Label outputWaitingTime;

    @FXML
    private Button computeButton;
    @FXML
    private TableColumn<Process, Integer> arrivalTimeOutput;
    @FXML
    private TableColumn<Process, Integer> burstTimeOutput;
    @FXML
    private TableColumn<Process, Integer> priorityOutput;
    @FXML
    private TableColumn<Process, Integer> processOutput;
    @FXML
    private TableColumn<Process, Integer> turnAroundOutput;
    @FXML
    private TableColumn<Process, Integer> waitingTimeOutput;

    @FXML
    private TableView<Process> tableViewOutput;

    @FXML
    private Button resetButton;

    @FXML
    private Button exitButton;

    @FXML
    private BarChart<String, Double> barChart;

    @FXML
    private CategoryAxis x;

    @FXML
    private NumberAxis y;

    static XYChart.Series series1 = new XYChart.Series();
    static XYChart.Series series2 = new XYChart.Series();

    @FXML
    private Label t1;

    @FXML
    private Label t2;

    @FXML
    private Label t3;

    @FXML
    private Label t4;

    @FXML
    private Label t5;

    @FXML
    private Label w1;

    @FXML
    private Label w2;

    @FXML
    private Label w3;

    @FXML
    private Label w4;

    @FXML
    private Label w5;

    @FXML
    private Label errorSymbol;

    @FXML
    private Button refreshButton;

    @FXML
    private Label output;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        processName.setCellValueFactory(new PropertyValueFactory<Process, Integer>("name"));
        arrivalTimeTable.setCellValueFactory(new PropertyValueFactory<Process, Integer>("arrivalTime"));
        burstTimeTable.setCellValueFactory(new PropertyValueFactory<Process, Integer>("burstTime"));
        priorityTable.setCellValueFactory(new PropertyValueFactory<Process, Integer>("priority"));

        tableView.setEditable(true);

        //selectSchedule.getItems().addAll(scheduels);

        processOutput.setCellValueFactory(new PropertyValueFactory<Process, Integer>("name"));
        arrivalTimeOutput.setCellValueFactory(new PropertyValueFactory<Process, Integer>("arrivalTime"));
        burstTimeOutput.setCellValueFactory(new PropertyValueFactory<Process, Integer>("burstTime"));
        priorityOutput.setCellValueFactory(new PropertyValueFactory<Process, Integer>("priority"));
        waitingTimeOutput.setCellValueFactory(new PropertyValueFactory<Process, Integer>("waitingTime"));
        turnAroundOutput.setCellValueFactory(new PropertyValueFactory<Process, Integer>("turnAroundTime"));

        inputQuantumTime.setText("1");

        double[][] averages = { {0, 0}, {0, 0}, {0, 0},  {0, 0},  {0, 0} };

        series1.setName("Average Waiting Time");
        series1.getData().add(new XYChart.Data<>("FCFS",averages[0][0]));
        series1.getData().add(new XYChart.Data<>("RR",averages[1][0]));
        series1.getData().add(new XYChart.Data<>("SPN",averages[2][0]));
        series1.getData().add(new XYChart.Data<>("SRTN",averages[3][0]));
        series1.getData().add(new XYChart.Data<>("PS",averages[4][0]));

        series2.setName("Average Turn Around Time");
        series2.getData().add(new XYChart.Data<>("FCFS",averages[0][1]));
        series2.getData().add(new XYChart.Data<>("RR",averages[1][1]));
        series2.getData().add(new XYChart.Data<>("SPN",averages[2][1]));
        series2.getData().add(new XYChart.Data<>("SRTN",averages[3][1]));
        series2.getData().add(new XYChart.Data<>("PS",averages[4][1]));

        barChart.getData().addAll(series1,series2);
    }

    @FXML
    void addProcessClicked(ActionEvent event) {
        int inputPrio = 1;
        if (!inputPriority.getText().isEmpty()){
            inputPrio = Integer.parseInt(inputPriority.getText());
        }
        Process process = new Process(pid,
                Integer.parseInt(inputBurstTime.getText()),
                inputPrio,
                Integer.parseInt(inputArrivalTime.getText()),
                Integer.parseInt("0"),Integer.parseInt("0"));
        processes.add(process);
        ObservableList<Process> p = tableView.getItems();
        p.add(process);
        tableView.setItems(p);
        tableView.setEditable(true);
        pid++;


//        ObservableList<Process> p = tableView.getItems();
//        p.add(new Process(1, 3, 9, 0,0,0));
//        p.add(new Process(2, 6, 2, 2,0,0));
//        p.add(new Process(3, 4, 1, 4,0,0));
//        p.add(new Process(4, 5, 8, 6,0,0));
//        p.add(new Process(5, 2, 3, 8,0,0));
//
//
//        processes.add(new Process(1, 3, 9, 0,0,0));
//        processes.add(new Process(2, 6, 2, 2,0,0));
//        processes.add(new Process(3, 4, 1, 4,0,0));
//        processes.add(new Process(4, 5, 8, 6,0,0));
//        processes.add(new Process(5, 2, 3, 8,0,0));
//        tableView.setItems(p);
//        tableView.setEditable(true);


        removeProcessButton.setDisable(false);
        computeButton.setDisable(false);
        quantumTime = Integer.parseInt(inputQuantumTime.getText());
    }

    @FXML
    void removeProcessClicked(ActionEvent event) {
        int selectedID = tableView.getSelectionModel().getSelectedIndex();
        if ( selectedID >= 0 && processes.size() > 0) {
            tableView.getItems().remove(selectedID);
            processes.remove(selectedID);
        }

        if (processes.size() == 0){
            removeProcessButton.setDisable(true);
            computeButton.setDisable(true);
        }
    }

    @FXML
    void addQuantumClicked(ActionEvent event) {
        int temp = Integer.parseInt(inputQuantumTime.getText());
        temp++;
        inputQuantumTime.setText(Integer.toString(temp));
    }

    @FXML
    void removeQuantumClicked(ActionEvent event) {
        int temp = Integer.parseInt(inputQuantumTime.getText());
        if (temp > 0) {
            temp--;
            inputQuantumTime.setText(Integer.toString(temp));
        }
    }

    @FXML
    void computeClicked(ActionEvent event) throws InterruptedException {

        List<Process> processes2 = processes;
        quantumTime = Integer.parseInt(inputQuantumTime.getText());

        findAverages fa = new findAverages(processes);
        SPN da = new SPN(processes2);

        fa.fcfs();
        fa.roundRobin(quantumTime);
        da.shortestProcessNext();

        fa.priorityScheduling();


        double[][] averages = fa.getAverage();
        double[][] averageSPN = da.getAverages();

        averages[2][0] = averageSPN[0][0];
        averages[2][1] = averageSPN[0][1];


        int k = 0;
        P[] array = new P[processes.size()];
        for (Process p : processes){
            array[k] = new P(p.getName(),p.getBurstTime(),p.getArrivalTime());
            k++;
        }

        double[] ave = SRTF.findavgTime(array,processes.size());
        averages[3][0] = ave[0];
        averages[3][1] = ave[1];

        DecimalFormat df = new DecimalFormat("#.00");

        w1.setText(Double.toString(Double.parseDouble(df.format(averages[0][0]))));
        w2.setText(Double.toString(Double.parseDouble(df.format(averages[1][0]))));
        w3.setText(Double.toString(Double.parseDouble(df.format(averages[2][0]))));
        w4.setText(Double.toString(Double.parseDouble(df.format(averages[3][0]))));
        w5.setText(Double.toString(Double.parseDouble(df.format(averages[4][0]))));

        t1.setText(Double.toString(Double.parseDouble(df.format(averages[0][1]))));
        t2.setText(Double.toString(Double.parseDouble(df.format(averages[1][1]))));
        t3.setText(Double.toString(Double.parseDouble(df.format(averages[2][1]))));
        t4.setText(Double.toString(Double.parseDouble(df.format(averages[3][1]))));
        t5.setText(Double.toString(Double.parseDouble(df.format(averages[4][1]))));


        for (int i = 0 ; i < 5 ; i++){
            System.out.println("Await     : " + averages[i][0]);
            System.out.println("Aturn     : " + averages[i][1]);
        }


        if (!barChart.getData().contains(series1)) {
            barChart.getData().add(series1);
        }

        if (!barChart.getData().contains(series2)) {
            barChart.getData().add(series2);
        }

        series1.getData().add(new XYChart.Data<>("FCFS", averages[0][0]));
        series1.getData().add(new XYChart.Data<>("RR", averages[1][0]));
        series1.getData().add(new XYChart.Data<>("SPN", averages[2][0]));
        series1.getData().add(new XYChart.Data<>("SRTN", averages[3][0]));
        series1.getData().add(new XYChart.Data<>("PS", averages[4][0]));

        series2.getData().add(new XYChart.Data<>("FCFS", averages[0][1]));
        series2.getData().add(new XYChart.Data<>("RR", averages[1][1]));
        series2.getData().add(new XYChart.Data<>("SPN", averages[2][1]));
        series2.getData().add(new XYChart.Data<>("SRTN", averages[3][1]));
        series2.getData().add(new XYChart.Data<>("PS", averages[4][1]));

        double[] bestAlgoAverages = { averages[0][0],averages[1][0],averages[2][0],averages[3][0],averages[4][0]};
        findMinimum(bestAlgoAverages,output);
    }


    @FXML
    void resetClicked(ActionEvent event) {
        tableView.getItems().clear();
        tableViewOutput.getItems().clear();
        processes.clear();
        pid = 1;
        inputQuantumTime.setText("1");

        t1.setText(null);
        t2.setText(null);
        t3.setText(null);
        t4.setText(null);
        t5.setText(null);

        w1.setText(null);
        w2.setText(null);
        w3.setText(null);
        w4.setText(null);
        w5.setText(null);

        barChart.getData().remove(series1);
        barChart.getData().remove(series2);

        computeButton.setDisable(true);
    }

    @FXML
    void exitClicked(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    public void keyReleased(KeyEvent keyEvent) {
        addProcessButton.setDisable(false);
        String arrivalTime = inputArrivalTime.getText();
        String burstTime = inputBurstTime.getText();
        String priority = inputPriority.getText();
        boolean isDisabled = (arrivalTime.isEmpty() || burstTime.isEmpty() && priority.isEmpty());
        addProcessButton.setDisable(isDisabled);
    }

    @FXML
    void clickFCFS(ActionEvent event) throws InterruptedException {
        SchedulingAlgorithms schedule = new SchedulingAlgorithms(processes);
        schedule.fcfs(tableViewOutput,outputWaitingTime,outputTurnAroundTime);
    }

    @FXML
    void clickPS(ActionEvent event) throws InterruptedException {
        SchedulingAlgorithms schedule = new SchedulingAlgorithms(processes);
        schedule.priorityScheduling(tableViewOutput,outputWaitingTime,outputTurnAroundTime);
    }

    @FXML
    void clickRR(ActionEvent event) throws InterruptedException {
        try {
            if (!"0".equals(inputQuantumTime.getText())) {
                errorSymbol.setVisible(false);
                SchedulingAlgorithms schedule = new SchedulingAlgorithms(processes);
                quantumTime = Integer.parseInt(inputQuantumTime.getText());
                schedule.roundRobin(tableViewOutput, outputWaitingTime, outputTurnAroundTime, quantumTime);
            } else {
                errorSymbol.setVisible(true);
            }

        } catch (Error e){
            System.out.println(e);
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void clickSPN(ActionEvent event) throws InterruptedException {
        SPN schedule = new SPN(processes);
        schedule.shortestProcessNext(tableViewOutput,outputWaitingTime,outputTurnAroundTime);
    }

    @FXML
    void clickSRTN(ActionEvent event) throws InterruptedException {
        SPN schedule = new SPN(processes);
        schedule.shortestRemainingTimeNext(tableViewOutput,outputWaitingTime,outputTurnAroundTime);
    }

    @FXML
    void refreshClicked(ActionEvent event) throws IOException {

        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home-interface.fxml"));
        Stage stage1 = new Stage();
        stage1.initStyle(StageStyle.TRANSPARENT);
        Scene scene = new Scene(fxmlLoader.load());
        stage1.setTitle("CPU Scheduler");
        stage1.setScene(scene);
        stage1.show();
    }

    public static void findMinimum(double[] avg,Label label){
        double min = avg[0];
        int element = 0;
        for (int i = 1; i < avg.length ; i++){
            if (min > avg[i]){
                min = avg[i];
                element = i;
            }
        }

        String bestAlgo = " ";

        if (element == 0)
            bestAlgo = "Best Algorithm is First Come First Serve";
        if (element == 1)
            bestAlgo = "Best Algorithm is Round Robin";
        if (element == 2)
            bestAlgo = "Best Algorithm is Shortest Process Next";
        if (element == 3)
            bestAlgo = "Best Algorithm is Shortest Remaining Time Next";
        if (element == 4)
            bestAlgo = "Best Algorithm is Priority Scheduling";


        label.setText(bestAlgo);
    }
}
