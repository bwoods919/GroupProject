module com.cs_4306.groupproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.cs_4306.groupproject to javafx.fxml;
    exports com.cs_4306.groupproject;
}