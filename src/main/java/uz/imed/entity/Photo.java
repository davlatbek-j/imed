package uz.imed.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
public class Photo extends BaseEntity
{

    String name;

    String filepath;

    String httpUrl;

    String type;

    public Photo(String name, String filepath, String httpUrl)
    {
        this.name = name;
        this.filepath = filepath;
        this.httpUrl = httpUrl;
    }
}
