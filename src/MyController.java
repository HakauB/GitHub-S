import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class MyController implements Initializable
{
	
	@FXML Button listRepoBtn;
	@FXML TableView<Row> data_table;
	@FXML TableColumn<Row, String> enabled;
	@FXML TableColumn<Row, String> repositories;
	@FXML TableColumn<Row, String> current_version;
	@FXML TableColumn<Row, String> latest_version;
	@FXML TableColumn<Row, String> last_pulled;
	@FXML TableColumn<Row, String> description;
	
	private static FileWalker w;
	
	private final ObservableList<String> repoList = FXCollections.observableArrayList(
    		new String("A"),
    		new String("B"),
    		new String("C")
    		); 
	 
	ObservableList<Row> observableList = FXCollections.observableArrayList();
	
	//ObservableList<Row> observableList = 
	public void buildList()
	{
		for(Git g : GitRepoBuilder.getrepositoryGits())
		{
			try {
				System.out.println(g.getRepository().getRepositoryState().toString());
				Config conf = g.getRepository().getConfig();
				String url = conf.getString("remote", "origin", "url");
				observableList.add(new Row(g.getRepository().getRepositoryState().toString(), g.getRepository().getDirectory().getAbsolutePath(), "v1.0", "v1.3", "5:00pm", url, g));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
		            	
	@FXML 
	private void listRepos()
	{
		observableList.clear();
		buildList();
		
		enabled.setCellValueFactory(
		        new PropertyValueFactory<Row,String>("enabled"));
		repositories.setCellValueFactory(
		        new PropertyValueFactory<Row,String>("repositories"));
		current_version.setCellValueFactory(
		        new PropertyValueFactory<Row,String>("current_version"));
		latest_version.setCellValueFactory(
		        new PropertyValueFactory<Row,String>("latest_version"));
		last_pulled.setCellValueFactory(
		        new PropertyValueFactory<Row,String>("last_pulled"));
		description.setCellValueFactory(
		        new PropertyValueFactory<Row,String>("description"));

		
		data_table.setItems(observableList);
		System.out.println("IT WORKED!!");
	}
	
	@FXML
	private void pullAllRepos()
	{
		Puller p = new Puller();
		p.pullAll();
	}
	
	@FXML
	private void pullSingleRepo()
	{
		Row temp = data_table.getSelectionModel().getSelectedItem();
		if(temp == null) return;
		Puller.pullSingle(temp.getGit());
	}
	
	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources)
	{

	}
}