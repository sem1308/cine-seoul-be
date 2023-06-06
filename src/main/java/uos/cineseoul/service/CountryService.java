package uos.cineseoul.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uos.cineseoul.dto.insert.InsertCountryDTO;
import uos.cineseoul.entity.Country;
import uos.cineseoul.repository.CountryRepository;

@Service
@RequiredArgsConstructor
public class CountryService {
    private final CountryRepository countryRepository;

    @Transactional
    public Country insert(InsertCountryDTO insertCountryDTO) {
        Country country = Country
                .builder()
                        .countryCode(insertCountryDTO.getCountryCode())
                                .name(insertCountryDTO.getName())
                                        .build();
        return countryRepository.save(country);
    }
}
