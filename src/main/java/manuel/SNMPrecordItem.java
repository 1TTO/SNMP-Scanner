package manuel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.soulwing.snmp.VarbindCollection;

import java.io.IOException;
import java.util.ArrayList;

public class SNMPrecordItem extends Label {
    private final Stage itemStage;
    private final ScrollPane root;
    private final GridPane content;

    SNMPrecordItem(VarbindCollection varbindCollection){
        root = new ScrollPane();
        itemStage = new Stage();
        content = new GridPane();
        itemStage.setScene(new Scene(root));
        itemStage.setTitle(varbindCollection.get(0).toString());
        this.setText(varbindCollection.get(0).toString());
        this.setAlignment(Pos.CENTER);

        setupSNMPrecordItem(varbindCollection);

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

    private void setupSNMPrecordItem(VarbindCollection varbindCollection){
        ArrayList<String> oidFileContent;

        root.setContent(content);
        content.setGridLinesVisible(true);
        content.setVgap(5);
        try {
            oidFileContent = File.getCSVContent(File.OID_FILE_PATH);
        } catch (IOException e) {return;}

        for (int i = 0; i < varbindCollection.size(); i++){
            Label description = new Label(oidFileContent.get(i));
            Label value = new Label(varbindCollection.get(i).toString());

            description.setPadding(new Insets(0, 5, 0, 5));
            value.setPadding(new Insets(0, 5, 0, 5));

            content.add(description, 0, i);
            content.add(value, 1, i);
        }
    }
}
