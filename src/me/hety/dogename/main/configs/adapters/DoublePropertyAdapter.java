package me.hety.dogename.main.configs.adapters;

import com.google.gson.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.lang.reflect.Type;

public class DoublePropertyAdapter implements JsonSerializer<SimpleDoubleProperty>, JsonDeserializer<SimpleDoubleProperty> {

    @Override
    public SimpleDoubleProperty deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if(jsonElement==null) {
            throw new JsonParseException("Json is wrong.");
        }else {
            return new SimpleDoubleProperty(jsonElement.getAsDouble());
        }
    }

    @Override
    public JsonElement serialize(SimpleDoubleProperty simpleDoubleProperty, Type type, JsonSerializationContext jsonSerializationContext) {
        if(simpleDoubleProperty==null){
            throw new JsonParseException("Json is wrong.");
        }else {
            return new JsonPrimitive(simpleDoubleProperty.get());
        }
    }
}
