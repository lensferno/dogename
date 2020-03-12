package me.hety.dogename.main.configs.adapters;

import com.google.gson.*;
import javafx.beans.property.SimpleStringProperty;

import java.lang.reflect.Type;

public class StringPropertyAdapter implements JsonSerializer<SimpleStringProperty>, JsonDeserializer<SimpleStringProperty> {
    @Override
    public SimpleStringProperty deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if(jsonElement==null) {
            throw new JsonParseException("Json is wrong.");
        }else {
            return new SimpleStringProperty(jsonElement.getAsString());
        }
    }

    @Override
    public JsonElement serialize(SimpleStringProperty simpleStringProperty, Type type, JsonSerializationContext jsonSerializationContext) {
        if(simpleStringProperty==null) {
            throw new JsonParseException("Json is wrong.");
        }else {
            return new JsonPrimitive(simpleStringProperty.get());
        }
    }
}
