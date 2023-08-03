module com.example.cpu_scheduling {
    requires javafx.controls;
    requires javafx.fxml;
            
            requires com.dlsc.formsfx;
            requires com.almasb.fxgl.all;
    
    opens com.example.cpu_scheduling to javafx.fxml;
    exports com.example.cpu_scheduling;
}