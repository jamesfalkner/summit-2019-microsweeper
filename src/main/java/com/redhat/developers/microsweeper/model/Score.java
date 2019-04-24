package com.redhat.developers.microsweeper.model;

import org.bson.Document;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Score extends PanacheEntity {

    public long scoreId;
    public String name;
    public String level;
    public int time;
    public boolean success;

    @Override
    public String toString() {
        return name + "/" + level + "/" + time + "/" + success + "/" + scoreId;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> obj = new HashMap<>();
        obj.put("name", name);
        obj.put("level", level);
        obj.put("time", time);
        obj.put("success", success);
        return obj;
    }

    public static Score fromDocument(Document d) {
        Score score = new Score();
        score.name = (d.getString("name"));
        score.level = (d.getString("level"));
        score.time = (d.getInteger("time"));
        score.success = (d.getBoolean("success"));
        return score;
    }

}
