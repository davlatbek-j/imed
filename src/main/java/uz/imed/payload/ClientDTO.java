package uz.imed.payload;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import uz.imed.entity.Client;
import uz.imed.entity.ClientTranslation;
import uz.imed.entity.Photo;
import uz.imed.exception.NotFoundException;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClientDTO {

    Long id;

    String slug;

    Photo logo;

    Boolean active;

    String name;

    String description;

    String address;

    Integer orderNum;

    public ClientDTO(Client client, String lang) {
        this.id = client.getId();
        this.slug = client.getSlug();
        this.logo = client.getLogo();
        this.active = client.getActive();
        ClientTranslation clientTranslation = TranslationHelper.getTranslationByLang(lang, client.getTranslations());
        this.name = clientTranslation.getName();
        this.description = clientTranslation.getDescription();
        this.address = clientTranslation.getAddress();
        this.orderNum = client.getOrderNum();
    }

    static class TranslationHelper {
        static ClientTranslation getTranslationByLang(String lang, List<ClientTranslation> clientTranslations) {
            return clientTranslations
                    .stream()
                    .filter(translation -> translation.getLanguage().equals(lang))
                    .findFirst()
                    .orElseThrow(() -> new NotFoundException("Language not supported: " + lang));
        }
    }

}
