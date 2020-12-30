package manuel;

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

    SNMPrecordItem(VarbindCollection varbindCollection){
        root = new ScrollPane();
        Scene itemScene = new Scene(root, 515, 200);
        itemStage = new Stage();
        itemStage.setScene(itemScene);
        itemStage.setTitle(varbindCollection.get(0).toString());
        this.setText(varbindCollection.get(0).toString());

        setupSNMPrecordItem(varbindCollection);

        this.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    itemStage.show();
                }
            }
        });
    }

    private void setupSNMPrecordItem(VarbindCollection varbindCollection){
        GridPane content = new GridPane();
        ArrayList<String> oidFileContent;

        root.setContent(content);
        content.setGridLinesVisible(true);
        content.setVgap(5);
        try {
            oidFileContent = File.getCSVContent(File.OID_FILE_PATH);
        } catch (IOException e) {return;}

        for (int i = 0; i < varbindCollection.size(); i++){
            content.add(new Label(oidFileContent.get(i)), 0, i);
            content.add(new Label(varbindCollection.get(i).toString()), 1, i);
        }
    }
}
