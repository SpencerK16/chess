package chess;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class ChessBoardAdapter implements JsonSerializer<ChessBoard>, JsonDeserializer<ChessBoard> {
    @Override
    public ChessBoard deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext
            jsonDeserializationContext) throws JsonParseException {
        ChessBoard results = new ChessBoard();
        JsonArray jsonEntries = jsonElement.getAsJsonArray();
        for (JsonElement entry : jsonEntries)
        {
            JsonObject entryObject = entry.getAsJsonObject();
            JsonElement keyjson = entryObject.get("key");
            JsonElement valuejson = entryObject.get("value");
            ChessPosition chessPosition = new Gson().fromJson(keyjson, ChessPosition.class);
            ChessPiece chessPiece = new Gson().fromJson(valuejson, ChessPiece.class);
            results.addPiece(chessPosition, chessPiece);
        }
        return results;
    }

    @Override
    public JsonElement serialize(ChessBoard board, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray entries = new JsonArray();
        for (Map.Entry<ChessPosition, ChessPiece> entry : board.entrySet())
        {
            JsonObject entryJson = new JsonObject();

            JsonElement keyjson = new Gson().toJsonTree(entry.getKey());
            JsonElement valuejson = new Gson().toJsonTree(entry.getValue());
            entryJson.add("key", keyjson);
            entryJson.add("value", valuejson);
            entries.add(entryJson);
        }
        return entries;
    }
}
