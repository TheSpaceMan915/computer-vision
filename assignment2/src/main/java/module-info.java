module app {
    requires javafx.controls;

    requires org.apache.logging.log4j;
    requires org.controlsfx.controls;
    requires opencv;
    requires static lombok;

    opens app to javafx.controls;
    exports app;
}