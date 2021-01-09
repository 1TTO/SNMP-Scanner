package manuel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.soulwing.snmp.SnmpNotificationEvent;

public class SNMPtrapsListenerItem extends Label{
    private final Stage itemStage;
    private final ScrollPane root;
    private final VBox content;
    private final GridPane varbindsContent;

    /**
     * Returns a SNMPtrapsListenerItem for ListView
     * @param trap SnmpNotificationEvent of the trap
     */
    SNMPtrapsListenerItem(SnmpNotificationEvent trap){
        root = new ScrollPane();
        itemStage = new Stage();
        content = new VBox();
        varbindsContent = new GridPane();
        root.setFitToWidth(true);
        itemStage.setScene(new Scene(root));
        itemStage.setTitle(String.valueOf(trap.getSource()));
        this.setText(String.valueOf(trap.getSource()));
        this.setAlignment(Pos.CENTER);

        setupSNMPtrapsListenerItem(trap);

        this.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    itemStage.show();

                    if (content.getHeight() < 400) itemStage.setHeight(content.getHeight() + 50);
                    else itemStage.setHeight(400);
                    itemStage.setWidth(content.getWidth() + 20);
                }
            }
        });
    }

    /**
     * Sets up the SNMPtrapsListenerItem graphically
     * @param trap SnmpNotificationEvent of the trap
     */
    private void setupSNMPtrapsListenerItem(SnmpNotificationEvent trap){
        root.setContent(content);
        content.getChildren().add(new Label("Peer: " + trap.getSubject().getPeer()));
        content.getChildren().add(new Label("Type: " + trap.getSubject().getType()));
        content.getChildren().add(new Label(""));
        content.getChildren().add(varbindsContent);
        varbindsContent.setGridLinesVisible(true);
        varbindsContent.setVgap(5);

        for (int i = 0; i < trap.getSubject().getVarbinds().size(); i++){
            Label description = new Label(trap.getSubject().getVarbinds().get(i).getName());
            Label value = new Label(trap.getSubject().getVarbinds().get(i).toString());

            description.setPadding(new Insets(0, 5, 0, 5));
            value.setPadding(new Insets(0, 5, 0, 5));

            varbindsContent.add(description, 0, i);
            varbindsContent.add(value, 1, i);
        }
    }
}
