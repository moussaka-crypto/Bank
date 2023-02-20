package bank;

import com.google.gson.*;
import java.lang.reflect.Type;

public class CustomSerializer implements JsonSerializer<Transaction> {
    /**
     * converts instances of Transaction to Json objects
     * @param tr the object that needs to be converted to Json.
     * @param t the actual type (fully generalized version) of the source object.
     * @param jsonSerializationContext serialization context
     * @return serialized json element
     */
    @Override
    public JsonElement serialize(Transaction tr, Type t, JsonSerializationContext jsonSerializationContext)
    {
        JsonObject jsonInstance = new JsonObject();
        jsonInstance.addProperty("CLASSNAME", tr.getClass().getSimpleName());

        if(tr.getClass().equals(Payment.class)){
            Payment payment = (Payment) tr;
            jsonInstance.add("INSTANCE", new Gson().toJsonTree(payment));
        }
        else if(tr.getClass().equals(IncomingTransfer.class))
        {
            IncomingTransfer iTr = (IncomingTransfer) tr;
            jsonInstance.add("INSTANCE", new Gson().toJsonTree(iTr));
        }
        else if(tr.getClass().equals(OutgoingTransfer.class))
        {
            OutgoingTransfer oTr = (OutgoingTransfer) tr;
            jsonInstance.add("INSTANCE", new Gson().toJsonTree(oTr));
        }

        //jsonInstance.add("INSTANCE", new Gson().toJsonTree(tr));
        return jsonInstance;
    }
}
