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
public class SearchListItem {
    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public String exhibitName;
    public int order;
    public boolean selected;

    public SearchListItem(@NonNull String exhibitName, int order, boolean selected){
        this.exhibitName = exhibitName;
        this.order = order;
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "SearchListItem{" +
                "id=" + id +
                ", exhibitName='" + exhibitName + '\'' +
                ", order=" + order +
                ", selected=" + selected +
                '}';
    }

    public static List<SearchListItem> loadJSON(Context context, String path){
        try {
            InputStream inputStream = context.getAssets().open(path);
            Reader reader = new InputStreamReader(inputStream);
            Gson gson = new Gson();
            Type type = new TypeToken<List<SearchListItem>>(){}.getType();
            return gson.fromJson(reader, type);
        }
        catch (IOException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
