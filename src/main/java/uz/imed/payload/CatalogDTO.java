package uz.imed.payload;




import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Catalog;
import uz.imed.exeptions.LanguageNotSupportException;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CatalogDTO {

    String name;



    public CatalogDTO(Catalog catalog, String lang){
        switch (lang.toLowerCase()){
            case "uz":
            {
                this.name= catalog.getNameUz();
            }
            case "ru":
            {
                this.name= catalog.getNameRu();
            }
            case "eng":
            {
                this.name= catalog.getNameEng();
            }
            default:
            {
                throw new LanguageNotSupportException("Language not supported: " + lang);
            }
        }
    }

}