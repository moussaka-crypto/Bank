package gui;
import bank.PrivateBank;
import bank.exceptions.AccountAlreadyExistsException;
import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAlreadyExistException;
import bank.exceptions.TransactionAttributeException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainviewController implements Initializable {
	
	private Scene scene;
	private PrivateBank pb = new PrivateBank("Sparkasse Aachen", 0.15, 0.13);

	private ObservableList<String> accountList = FXCollections.observableArrayList(); // Konstoliste im View

	@FXML
	private TextField bankNameField;
	@FXML
	private ListView<String> accountsView;

	public MainviewController() throws TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, IOException {
	}

	@FXML
	public void switchScene()
			throws IOException {
		//save chosen account
		UserBankSingleton userInstance = UserBankSingleton.getInstance();
		String userName = accountsView.getSelectionModel().getSelectedItem();// gets chosen account
		PrivateBank bankName = pb;

		//set instances for scene change
		userInstance.setUser(userName);
		userInstance.setBank(bankName);

		Parent root = new FXMLLoader(getClass().getClassLoader().getResource("Accountview.fxml")).load();

		Stage stage = (Stage) bankNameField.getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.setResizable(false);
		stage.show();
	}

	@FXML
	private void deleteAccount() {

		String chosenAccount = accountsView.getSelectionModel().getSelectedItem();

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Account");
		alert.setHeaderText("Do you want to delete the following account?");
		alert.setContentText(chosenAccount);

		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK)
		{
			try {
				pb.deleteAccount(chosenAccount);
				updateAccountList();
			}catch(AccountDoesNotExistException | IOException e) {
				e.printStackTrace();
				errorMessage(e.getMessage());
			}
		}
	}

	@FXML
	private void addAccount()
	{
		TextInputDialog inputDialog = new TextInputDialog("");
		inputDialog.setTitle("New Account");
		inputDialog.setHeaderText("Please enter the name of the new account: ");
		inputDialog.setContentText("Name: ");
		inputDialog.getDialogPane().setMinWidth(333);

		// Goto way to get the response value.
		Optional<String> result = inputDialog.showAndWait();
		if(result.isPresent())
		{
			if(Objects.equals(result.get(), "")){
				errorMessage("Incorrect Input!");
				addAccount();
			}
			else{
				try{
					pb.createAccount(result.get());
					updateAccountList();
				}catch(Exception e){
					e.printStackTrace();
					errorMessage(e.getMessage());
				}
			}
		}
	}

	@FXML
	private void errorMessage(String cause)
	{
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText(cause);
		alert.setContentText("Please try again!");
		alert.showAndWait();
	}

	@FXML
	private void updateAccountList()
	{
		accountList.clear();
		accountList.addAll(pb.getAllAccounts());
		accountsView.setItems(accountList);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		updateAccountList();
		bankNameField.setText(pb.getName());
	}
}
