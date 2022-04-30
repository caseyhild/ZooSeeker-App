package com.example.zooseeker_cse_110_team_27;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
@Entity(tableName = "search_list_items")
public class Exhibit {



    @NonNull
    public String id;
    public String kind;
    public String name;
    public List<String> tags;

    public Exhibit(@NonNull String id, String kind, String name, List<String> tags) {
        this.id = id;
        this.kind = kind;
        this.name = name;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Exhibit{" +
                "id='" + id + '\'' +
                ", kind='" + kind + '\'' +
                ", name='" + name + '\'' +
                ", tags=" + tags +
                '}';
    }

    public static List<Exhibit> loadJSONForSearching(Context context, String path){
        try {
            InputStream inputStream = context.getAssets().open(path);
            Reader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();
            Type type = new TypeToken<List<Exhibit>>(){}.getType();
            return gson.fromJson(reader, type);
        }
        catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}