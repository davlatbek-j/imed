package uz.imed.service;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.imed.entity.Address;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.AddressRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;

    public ResponseEntity<ApiResponse<Address>> create(Address address) {
        ApiResponse<Address> response = new ApiResponse<>();
        Address save = addressRepository.save(address);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Address>> findById(Long id) {
        ApiResponse<Address> response = new ApiResponse<>();
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isEmpty()) {
            response.setMessage("Address is not found by id");
            return ResponseEntity.status(404).body(response);
        }
        Address address = optionalAddress.get();
        response.setData(address);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<Address>>> findAll() {
        ApiResponse<List<Address>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Address> all = addressRepository.findAll();
        all.forEach(address -> response.getData().add(address));
        response.setMessage("Found " + all.size() + " Address");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Address>> update(Long id, Address address) {
        ApiResponse<Address> response = new ApiResponse<>();
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isEmpty()) {
            response.setMessage("Address is not found by id");
            return ResponseEntity.status(404).body(response);
        }
        Address oldAddress = optionalAddress.get();
        oldAddress.setLocationUrl(address.getLocationUrl());
        Address save = addressRepository.save(oldAddress);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (addressRepository.findById(id).isEmpty()) {
            response.setMessage("Address is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        addressRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }


}
