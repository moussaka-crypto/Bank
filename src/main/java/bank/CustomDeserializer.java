package bank;

import bank.exceptions.TransactionAttributeException;
import com.google.gson.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CustomDeserializer implements JsonDeserializer<List<Transaction>>{
    /**
     * GsonBuilder kann nicht zwischen den verschiedenen Klassen unterscheiden, deswegen Custom Ser und Deser
     *
     * converts Json Objects to instances of Transaction objects
     * @param jsonElement The Json data being deserialized
     * @param type The type of the Object to deserialize to
     * @param jsonDeserializationContext deserialization context
     * @return list of transaction from json file
     */
    @Override
    public List<Transaction> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
    {
        JsonArray list = jsonElement.getAsJsonArray();
        List<Transaction> transactionList = new ArrayList<>();

        for (JsonElement l : list){

            JsonObject jsonObject = l.getAsJsonObject(); //list element as json obj

            String className = jsonObject.get("CLASSNAME").getAsString();
            if(jsonObject.has("INSTANCE")){

                JsonObject instance = jsonObject.get("INSTANCE").getAsJsonObject();

                switch (className) {
                    case "OutgoingTransfer" -> {
                        OutgoingTransfer outgoingTransfer;
                        try {
                            outgoingTransfer = new OutgoingTransfer(
                                    instance.get("date").getAsString(),
                                    instance.get("amount").getAsDouble(),
                                    instance.get("description").getAsString(),
                                    instance.get("sender").getAsString(),
                                    instance.get("recipient").getAsString()

                            );
                        } catch (TransactionAttributeException e) {
                            throw new RuntimeException(e);
                        }
                        transactionList.add(outgoingTransfer);
                    }
                    case "IncomingTransfer" -> {
                        IncomingTransfer incomingTransfer;
                        try {
                            incomingTransfer = new IncomingTransfer(
                                    instance.get("date").getAsString(),
                                    instance.get("amount").getAsDouble(),
                                    instance.get("description").getAsString(),
                                    instance.get("sender").getAsString(),
                                    instance.get("recipient").getAsString()
                            );
                        } catch (TransactionAttributeException e) {
                            throw new RuntimeException(e);
                        }
                        transactionList.add(incomingTransfer);
                    }
                    case "Payment" -> {
                        Payment payment;
                        try {
                            payment = new Payment(
                                    instance.get("date").getAsString(),
                                    instance.get("amount").getAsDouble(),
                                    instance.get("description").getAsString(),
                                    instance.get("incomingInterest").getAsDouble(),
                                    instance.get("outgoingInterest").getAsDouble()
                            );
                        } catch (TransactionAttributeException e) {
                            throw new RuntimeException(e);
                        }
                        transactionList.add(payment);
                    }
                }
            }
        }
        return transactionList;
    }
}