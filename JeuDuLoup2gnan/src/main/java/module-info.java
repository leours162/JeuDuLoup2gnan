module com.example.jeuduloup2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.xml;

    opens com.example.jeuduloup2 to javafx.fxml;
    exports com.example.jeuduloup2;
    exports com.example.jeuduloup2.View;
    opens com.example.jeuduloup2.View to javafx.fxml;
}