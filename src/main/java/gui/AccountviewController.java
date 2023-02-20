package gui;

import bank.*;
import bank.exceptions.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccountviewController implements Initializable {

    //List to observe changes to transactions list
    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
    private PrivateBank pB;
    private String account;
    private Scene scene;

    @FXML
    private TextField bankNameField, accountNameField, balanceField;
    @FXML
    private Button ascendingButton, descendingButton, incomingButton, outgoingButton;
    @FXML
    private ListView<Transaction> transactionListView;

    @FXML
    public void switchScene()
            throws IOException {

        Parent root = new FXMLLoader(getClass().getClassLoader().getResource("Mainview.fxml")).load();
        Stage stage = (Stage) bankNameField.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    private void deleteTransaction()
    {
        Transaction chosenTransaction = transactionListView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete transaction");
        alert.setHeaderText("Do you want to delete the following transaction?");
        alert.setContentText(chosenTransaction.toString());

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get()==ButtonType.OK)
        {
            try{
                pB.removeTransaction(account, chosenTransaction);
                updateListView(pB.getTransactions(account));
                balanceField.setText(String.format("%1.2f", pB.getAccountBalance(account)) + " €");

            }catch(TransactionDoesNotExistException e){
                e.printStackTrace();
                errorMessage(e.getMessage());
            }catch (AccountDoesNotExistException | IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void addTransaction()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New transaction: ");
        alert.setHeaderText("What type of transaction do you want to make?");

        ButtonType transferButton = new ButtonType("Transfer");
        ButtonType paymentButton = new ButtonType("Payment");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(transferButton, paymentButton, cancel);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get()==transferButton)
            newTransfer();
        else if(result.isPresent() && result.get()==paymentButton)
            newPayment();
    }

    private void newTransfer(){
        //new Window
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("New Transfer");
        dialog.setHeaderText("Please enter the data for the new transfer:");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResizable(false);

        GridPane grid = new GridPane();
        grid.setVgap(10); grid.setHgap(10);
        grid.setPadding(new Insets(20, 10, 10 ,10));

        TextField date = new TextField(), amount = new TextField(), description = new TextField(),
                sender = new TextField(), recipient = new TextField(); //text fields for all attributes needed

        //adding the elements
        grid.add(new Label("Date: "), 0, 0);
        grid.add(date,1,0);
        grid.add(new Label("Amount: "),0,1);
        grid.add(amount,1,1);
        grid.add(new Label("Description: "),0,2);
        grid.add(description,1,2);
        grid.add(new Label("Sender: "),0,3);
        grid.add(sender, 1,3);
        grid.add(new Label("Recipient: "),0,4);
        grid.add(recipient, 1,4);
        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.get()==ButtonType.OK){
            if(date.getText().equals("") ||
                amount.getText().equals("")||
                description.getText().equals("")||
                sender.getText().equals("")||
                recipient.getText().equals(""))
            {
                errorMessage("Empty input detected, please try again!");
                newTransfer();
            }
            else if(Double.parseDouble(amount.getText()) < 0){
                errorMessage("Negative transaction attempted!");
                newTransfer();
            }
            else if(sender.getText().equals(recipient.getText())){
                errorMessage("Sender and Recipient have the same name!");
                newTransfer();
            }
            else if(!sender.getText().equals(account) && !recipient.getText().equals(account)){
                errorMessage("Sender or Recipient must be account owners!");
                newTransfer();
            }
            else if(account.equals(sender.getText())){ //Sender is Account Owner
                try{
                    pB.addTransaction(account, new OutgoingTransfer(date.getText(), Double.parseDouble(amount.getText()), description.getText(), account, recipient.getText()));
                }catch(TransactionAlreadyExistException | AccountDoesNotExistException e){
                    errorMessage(e.getMessage());
                }catch(TransactionAttributeException | IOException e){
                    throw new RuntimeException(e);
                }
            }
            else{
                try{
                    pB.addTransaction(account, new IncomingTransfer(date.getText(), Double.parseDouble(amount.getText()), description.getText(), sender.getText(), account));
                }catch(TransactionAlreadyExistException | AccountDoesNotExistException e){
                    errorMessage(e.getMessage());
                }catch(TransactionAttributeException | IOException e){
                    throw new RuntimeException(e);
                }
            }
            updateListView(pB.getTransactions(account));
            balanceField.setText(String.format("%1.2f", pB.getAccountBalance(account))+ " €");
        }
    }
    private void newPayment(){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("New Payment");
        alert.setHeaderText("Please enter the data for the new payment (Bank interest is automatically applied.)");
        alert.setResizable(false);

        ButtonType deposit = new ButtonType("Deposit");
        ButtonType withdrawal = new ButtonType("Withdrawal");
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(deposit,withdrawal,cancel);

        GridPane grid = new GridPane();
        grid.setHgap(10); grid.setVgap(10);
        grid.setPadding(new Insets(20,10,10,10));

        TextField date = new TextField(), amount = new TextField(), description = new TextField(),
                incomingInterest = new TextField(), outgoingInterest = new TextField(); //text fields for all attributes needed

        //interests are only defined by the bank instance
        incomingInterest.setEditable(false); outgoingInterest.setEditable(false);
        incomingInterest.setText(String.format("%1.2f", pB.getIncomingInterest()) + " %");
        outgoingInterest.setText(String.format("%1.2f", pB.getOutgoingInterest()) + " %");

        //adding the elements
        grid.add(new Label("Date: "), 0, 0);
        grid.add(date,1,0);
        grid.add(new Label("Amount: "),0,1);
        grid.add(amount,1,1);
        grid.add(new Label("Description: "),0,2);
        grid.add(description,1,2);
        grid.add(new Label("Incoming Interest: "),0,3);
        grid.add(incomingInterest, 1,3);
        grid.add(new Label("Outgoing Interest: "),0,4);
        grid.add(outgoingInterest, 1,4);
        alert.getDialogPane().setContent(grid);

        Optional<ButtonType> result = alert.showAndWait();
        int paymentChoice = 1;

        if(result.isPresent() && result.get()==withdrawal) //if outgoing relating to owner
            paymentChoice = -1;

        if(result.isPresent() && result.get() == deposit ||
                result.isPresent() && result.get() == withdrawal)
        {
            if(date.getText().equals("") || amount.getText().equals("") || description.getText().equals(""))
            {
                errorMessage("Empty input detected, please try again!");
                newTransfer();
            }
            else{
                try
                {
                    pB.addTransaction(account, new Payment(date.getText(), Double.parseDouble(amount.getText())*paymentChoice,
                            description.getText()));
                }
                catch(AccountDoesNotExistException | TransactionAlreadyExistException e){
                    errorMessage(e.getMessage());
                }
                catch (TransactionAttributeException | IOException e){
                    throw new RuntimeException(e);
                }
            }
            updateListView(pB.getTransactions(account));
            balanceField.setText(String.format("%1.2f", pB.getAccountBalance(account)) + " €");
        }
    }

    public void errorMessage(String cause)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("System error");
        alert.setHeaderText(cause);
        alert.showAndWait();
    }

    private void updateListView(List<Transaction> list){
        transactionList.clear();
        transactionList.addAll(list);
        transactionListView.setItems(transactionList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pB = UserBankSingleton.getInstance().getBank();
        account = UserBankSingleton.getInstance().getUser();

        bankNameField.setText(pB.getName());
        accountNameField.setText(account);
        balanceField.setText(String.format("%1.2f", pB.getAccountBalance(account)) + " €");
        updateListView(pB.getTransactions(account));

        ascendingButton.setOnAction(actionEvent -> updateListView(pB.getTransactionsSorted(account, true)));
        descendingButton.setOnAction(actionEvent -> updateListView(pB.getTransactionsSorted(account, false)));
        incomingButton.setOnAction(actionEvent -> updateListView(pB.getTransactionsByType(account, true)));
        outgoingButton.setOnAction(actionEvent -> updateListView(pB.getTransactionsByType(account, false)));
    }
}
