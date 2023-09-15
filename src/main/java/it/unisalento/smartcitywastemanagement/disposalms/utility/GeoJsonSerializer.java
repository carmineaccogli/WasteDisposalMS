package it.unisalento.smartcitywastemanagement.disposalms.utility;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.io.IOException;

public class GeoJsonSerializer extends JsonSerializer<GeoJsonPoint> {

    @Override
    public void serialize(GeoJsonPoint geoJsonPoint, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("type", geoJsonPoint.getType());
        jsonGenerator.writeArrayFieldStart("coordinates");
        jsonGenerator.writeNumber(geoJsonPoint.getX());
        jsonGenerator.writeNumber(geoJsonPoint.getY());
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
