package it.unisalento.smartcitywastemanagement.disposalms.utility;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.io.IOException;

public class GeoJsonDeserializer extends JsonDeserializer<GeoJsonPoint> {

    @Override
    public GeoJsonPoint deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        JsonNode coordinatesNode = node.get("coordinates");
        double x = coordinatesNode.get(0).asDouble();
        double y = coordinatesNode.get(1).asDouble();
        return new GeoJsonPoint(x, y);
    }
}

