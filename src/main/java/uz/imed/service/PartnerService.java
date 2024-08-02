package uz.imed.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.imed.repository.PartnerRepository;

import java.util.List;

@Service
public class PartnerService
{

    @Autowired
    private PartnerRepository partnerRepository;

//    @Autowired
//    private PartnerTranslationRepository partnerTranslationRepository;

//    public Partner createPartner(Partner partner, List<PartnerTranslation> translations)
//    {
//        // Save the partner entity
//        Partner savedPartner = partnerRepository.save(partner);
//
//        // Save the translations with the reference to the saved partner
//        for (PartnerTranslation translation : translations)
//        {
//            translation.setPartner(savedPartner);
//            partnerTranslationRepository.save(translation);
//        }
//
//        // Return the saved partner with its translations
//        savedPartner.setTranslations(translations);
//        return savedPartner;
//    }
}
