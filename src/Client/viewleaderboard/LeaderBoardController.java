package Client.viewleaderboard;

import Client.Constances.DBConfig;
import Model.LeaderModel;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.table.DefaultTableModel;
import javax.swing.text.TabableView;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Bhagya Rathnayake on 6/25/2017.
 */
public class LeaderBoardController extends Application implements Initializable {

    private LeaderModel leaderModel;
    private DBConfig dbConfig;
    private ObservableList<LeaderModel> list;
    private PreparedStatement preparedStatement;
    public LeaderBoardController()
    {
        leaderModel = new LeaderModel();
        dbConfig = new DBConfig();
        list = FXCollections.observableArrayList();
    }

    @FXML
    TableView<LeaderModel> tblLeaderBoard;

    @FXML
    private TableColumn<?,?> userName;

    @FXML
    private TableColumn<?,?> score;

    @FXML
    private TableColumn<?,?> date;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../viewleaderboard/leaderBoard.fxml"));
        primaryStage.setScene(new Scene(root,600,400));
        primaryStage.setResizable(false);
        //primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        getLeaderRecords();
    }


    public void getLeaderRecords()
    {
        ResultSet resultSet;
       try{
           preparedStatement = dbConfig.getConnection().prepareStatement("SELECT * FROM tbl_leaderboard ORDER BY SCORE DESC");
           resultSet=preparedStatement.executeQuery();
           while (resultSet.next())
           {
               LeaderModel leaders = new LeaderModel();
               leaders.setGameDate(resultSet.getDate("DATE").toString());
               leaders.setUserName(resultSet.getString("USERNAME"));
               leaders.setScore(resultSet.getInt("SCORE"));
               list.add(leaders);
           tblLeaderBoard.setItems(list);
           }
           System.out.println(list.size());
       }

       catch (Exception e)
       {
           System.out.println(e);
       }
    }

}