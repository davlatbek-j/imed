package uz.imed.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "photo")
public class Photo
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonIgnore
    String name;

    @JsonIgnore
    String filePath;

    @JsonProperty(value = "url", access = JsonProperty.Access.READ_ONLY)
    String httpUrl;

    @JsonIgnore
    String type;

    public Photo(String name, String filePath, String httpUrl)
    {
        this.name = name;
        this.filePath = filePath;
        this.httpUrl = httpUrl;
    }

}
