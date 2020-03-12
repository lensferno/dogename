package me.hety.dogename.main.configs.adapters;

import com.google.gson.*;
import javafx.beans.property.SimpleIntegerProperty;

import java.lang.reflect.Type;

public class IntegerPropertyAdapter implements JsonSerializer<SimpleIntegerProperty>, JsonDeserializer<SimpleIntegerProperty> {
    @Override
    public SimpleIntegerProperty deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if(jsonElement==null) {
            throw new JsonParseException("Json is wrong.");
        }else {
            return new SimpleIntegerProperty(jsonElement.getAsInt());
        }
    }

    @Override
    public JsonElement serialize(SimpleIntegerProperty simpleIntegerProperty, Type type, JsonSerializationContext jsonSerializationContext) {
        if(simpleIntegerProperty==null){
            throw new JsonParseException("Json is wrong.");
        }else {
            return new JsonPrimitive(simpleIntegerProperty.get());
        }
    }
}
